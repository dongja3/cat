package com.dianping.cat.servlet;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.configuration.client.entity.Server;
import com.dianping.cat.layercontext.RemoteCallLog;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageManager;
import com.dianping.cat.message.internal.DefaultTransaction;
import org.unidal.helper.Joiners;
import org.unidal.helper.Joiners.IBuilder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class CatFilter implements Filter {

	private List<Handler> m_handlers = new ArrayList<Handler>();
	private static String[] ignoreUrlPrefixArray;
	private static String[] ignoreUrlSuffixArray;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	      ServletException {
		Context ctx = new Context((HttpServletRequest) request, (HttpServletResponse) response, chain, m_handlers);
		ctx.handle();
	}

	protected String getOriginalUrl(ServletRequest request) {
		return ((HttpServletRequest) request).getRequestURI();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		m_handlers.add(CatHandler.ENVIRONMENT);
		m_handlers.add(CatHandler.LOG_SPAN);
		m_handlers.add(CatHandler.LOG_CLIENT_PAYLOAD);
		m_handlers.add(CatHandler.ID_SETUP);
		m_handlers.add(CatHandler.PIWIK);
		String ignoreUrlPrefixes = filterConfig.getInitParameter("ignoreUrlPrefix");
		String ignoreUrlSuffixes = filterConfig.getInitParameter("ignoreUrlSuffix");

		if(ignoreUrlPrefixes!=null){
			ignoreUrlPrefixArray = ignoreUrlPrefixes.split(";");
		}

		if(ignoreUrlSuffixes!=null){
			ignoreUrlSuffixArray=ignoreUrlSuffixes.split(";");
		}

	}

	private static enum CatHandler implements Handler {
		ENVIRONMENT {

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				RemoteCallLog.logRemoteCallService(req);
				boolean top = !Cat.getManager().hasContext();

				ctx.setTop(top);

				if (top) {
					ctx.setType(CatConstants.TYPE_URL);

					setTraceMode(req);
				} else {
					ctx.setType(CatConstants.TYPE_URL_FORWARD);
				}

				ctx.handle();
			}

			protected void setTraceMode(HttpServletRequest req) {
				String traceMode = "X-CAT-TRACE-MODE";
				String headMode = req.getHeader(traceMode);

				if ("true".equals(headMode)) {
					Cat.getManager().setTraceMode(true);
				}
			}
		},

		ID_SETUP {
			private String m_servers;

			private String getCatServer() {
				if (m_servers == null) {
					DefaultMessageManager manager = (DefaultMessageManager) Cat.getManager();
					List<Server> servers = manager.getConfigManager().getServers();

					m_servers = Joiners.by(',').join(servers, new IBuilder<Server>() {
						@Override
						public String asString(Server server) {
							String ip = server.getIp();
							Integer httpPort = server.getHttpPort();

							return ip + ":" + httpPort;
						}
					});
				}

				return m_servers;
			}

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				boolean isTraceMode = Cat.getManager().isTraceMode();
				HttpServletResponse res = ctx.getResponse();

				if (isTraceMode) {
					String id = Cat.getCurrentMessageId();

					res.setHeader("X-CAT-ROOT-ID", id);
					res.setHeader("X-CAT-SERVER", getCatServer());
				}

				ctx.handle();
			}
		},

		LOG_CLIENT_PAYLOAD {
			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				String type = ctx.getType();

				if (ctx.isTop()) {
					logRequestClientInfo(req, type);
					logRequestPayload(req, type);
				} else {
					logRequestPayload(req, type);
				}

				ctx.handle();
			}

			protected void logRequestClientInfo(HttpServletRequest req, String type) {
				StringBuilder sb = new StringBuilder(1024);
				String ip = "";
				String ipForwarded = req.getHeader("x-forwarded-for");

				if (ipForwarded == null) {
					ip = req.getRemoteAddr();
				} else {
					ip = ipForwarded;
				}

				sb.append("IPS=").append(ip);
				sb.append("&VirtualIP=").append(req.getRemoteAddr());
				sb.append("&Server=").append(req.getServerName());
				sb.append("&Referer=").append(req.getHeader("referer"));
				sb.append("&Agent=").append(req.getHeader("user-agent"));

				Cat.logEvent(type, type + ".Server", Message.SUCCESS, sb.toString());
			}

			protected void logRequestPayload(HttpServletRequest req, String type) {
				StringBuilder sb = new StringBuilder(256);

				sb.append(req.getScheme().toUpperCase()).append('/');
				sb.append(req.getMethod()).append(' ').append(req.getRequestURI());

				String qs = req.getQueryString();

				if (qs != null) {
					sb.append('?').append(qs);
				}

				Cat.logEvent(type, type + ".Method", Message.SUCCESS, sb.toString());
			}
		},

		LOG_SPAN {

			public static final char SPLIT = '/';

			private void customizeStatus(Transaction t, HttpServletRequest req) {
				Object catStatus = req.getAttribute(CatConstants.CAT_STATE);

				if (catStatus != null) {
					t.setStatus(catStatus.toString());
				} else {
					t.setStatus(Message.SUCCESS);
				}
			}

			private void customizeUri(Transaction t, HttpServletRequest req) {

				if (t instanceof DefaultTransaction) {
					Object catPageType = req.getAttribute(CatConstants.CAT_PAGE_TYPE);

					if (catPageType instanceof String) {
						((DefaultTransaction) t).setType(catPageType.toString());
					}

					Object catPageUri = req.getAttribute(CatConstants.CAT_PAGE_URI);

					if (catPageUri instanceof String) {
						((DefaultTransaction) t).setName(catPageUri.toString());
					}
				}

			}

			private String getRequestURI(HttpServletRequest req) {
				String url = req.getRequestURI();
				int length = url.length();
				String headerUri = req.getHeader("cat_uri");
				if(headerUri!=null){
					return headerUri;
				}
				StringBuilder sb = new StringBuilder(length);
				for (int index = 0; index < length;) {
					char c = url.charAt(index);

					if (c == SPLIT && index < length - 1) {
						sb.append(c);

						StringBuilder nextSection = new StringBuilder();
						boolean isNumber = false;
						boolean first = true;

						for (int j = index + 1; j < length; j++) {
							char next = url.charAt(j);

							if ((first || isNumber == true) && next != SPLIT) {
								isNumber = isNumber(next);
								first = false;
							}

							if (next == SPLIT) {
								if (isNumber) {
									sb.append("{num}");
								} else {
									sb.append(nextSection.toString());
								}
								index = j;

								break;
							} else if (j == length - 1) {
								if (isNumber) {
									sb.append("{num}");
								} else {
									nextSection.append(next);
									sb.append(nextSection.toString());
								}
								index = j + 1;
								break;
							} else {
								nextSection.append(next);
							}
						}
					} else {
						sb.append(c);
						index++;
					}
				}

				return sb.toString();
			}

			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				HttpServletRequest req = ctx.getRequest();
				String uri = getRequestURI(req);
				uri = removeJsonGetParameter(uri);
				Transaction t=null;
				if(uri!=null){
					 t= Cat.newTransaction(ctx.getType(), uri);
				}

				try {
					ctx.handle();
					if(t!=null){
						customizeStatus(t, req);
					}

				} catch (ServletException e) {
					if(t!=null){
						t.setStatus(e);
					}

					Cat.logError(e);
					throw e;
				} catch (IOException e) {
					if(t!=null){
						t.setStatus(e);
					}
					Cat.logError(e);
					throw e;
				} catch (Throwable e) {
					if(t!=null){
						t.setStatus(e);
					}
					Cat.logError(e);
					throw new RuntimeException(e);
				} finally {
					if(t!=null){
						customizeUri(t, req);
						t.complete();
					}

				}
			}

			private String removeJsonGetParameter(String uri){

				try {
					uri = URLDecoder.decode(uri,"UTF-8");
				} catch (UnsupportedEncodingException e) {
				   return uri;
				}

				if(ignoreUrlSuffixArray!=null){
					for(String suffix : ignoreUrlSuffixArray){
						if(uri.indexOf(suffix)>0){
							uri=uri.substring(0,uri.indexOf(suffix));
							return uri;
						}
					}
				}

				if(ignoreUrlPrefixArray!=null){
					int index;
					for(String prefix : ignoreUrlPrefixArray){
						if(prefix==null||prefix.trim().length()==0){
							continue;
						}
						index = uri.indexOf(prefix);
						if(index<0){
							continue;
						}
						uri = uri.substring(index+prefix.length());
					}
				}

				if(uri.indexOf("{")==-1){
					return uri;
				}
				return uri.substring(0,uri.indexOf("{")-1);
			}

			private boolean isNumber(char c) {
				return (c >= '0' && c <= '9') || c == '.' || c == '-' || c == ',';
			}
		},

		PIWIK{
			@Override
			public void handle(Context ctx) throws IOException, ServletException {
				boolean isTraceMode = Cat.getManager().isTraceMode();
				HttpServletResponse res = ctx.getResponse();

				if (isTraceMode) {
				}

				ctx.handle();
			}
		};
	}

	protected static class Context {
		private FilterChain m_chain;

		private List<Handler> m_handlers;

		private int m_index;

		private HttpServletRequest m_request;

		private HttpServletResponse m_response;

		private boolean m_top;

		private String m_type;

		public Context(HttpServletRequest request, HttpServletResponse response, FilterChain chain, List<Handler> handlers) {
			m_request = request;
			m_response = response;
			m_chain = chain;
			m_handlers = handlers;
		}

		public HttpServletRequest getRequest() {
			return m_request;
		}

		public HttpServletResponse getResponse() {
			return m_response;
		}

		public String getType() {
			return m_type;
		}

		public void handle() throws IOException, ServletException {
			if (m_index < m_handlers.size()) {
				Handler handler = m_handlers.get(m_index++);

				handler.handle(this);
			} else {
				m_chain.doFilter(m_request, m_response);
			}
		}

		public boolean isTop() {
			return m_top;
		}

		public void setTop(boolean top) {
			m_top = top;
		}

		public void setType(String type) {
			m_type = type;
		}
	}

	protected static interface Handler {
		public void handle(Context ctx) throws IOException, ServletException;
	}

}
