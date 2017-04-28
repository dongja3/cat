package com.dianping.cat.report.page.chain;

import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.report.ReportPage;
import org.unidal.web.mvc.view.Viewer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XmlViewer implements Viewer<ReportPage, Action, Context, Model> {
	@Override
	public void view(Context ctx, Model model) throws ServletException, IOException {
		ChainReport report = model.getReport();
		HttpServletResponse res = ctx.getHttpServletResponse();

		if (report != null) {
			ServletOutputStream out = res.getOutputStream();

			res.setContentType("text/xml");
			out.print(report.toString());
		} else {
			res.sendError(404, "Not found!");
		}
	}
}
