package com.dianping.cat.layercontext;

import com.dianping.cat.Cat;
import com.google.gson.Gson;
import org.unidal.lookup.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by DONGJA3 on 6/20/2017.
 * http://www.cnblogs.com/xing901022/p/6237874.html
 */
public class RemoteCallLog {
    private static Gson GSON = new Gson();

    /**
     * 分布式调用追踪起点
     * prs 端调用该方法，并将返回的String 放到 request Header 里面，Header的KEY必须是LayersCatContext.CAT_LAYER_CONTEXT
     * @param request
     * @return
     */
    public static String createClientContext(HttpServletRequest request){
        Cat.Context ctx = new LayersCatContext();

        //catUri 目前来自PIWIK http request header
        String catUri = request.getHeader(LayersCatContext.CAT_URI);
        if(StringUtils.isEmpty(catUri)){
            return GSON.toJson(ctx);
        }

        Cat.logRemoteCallClient(ctx);
        ctx.addProperty(LayersCatContext.CAT_URI, catUri);
        String jsonString = GSON.toJson(ctx);
        System.out.println(jsonString);
        return jsonString;
    }

    /**
     * 分布式调用追踪
     * Dom 端调用该方法，实现分布式调用追踪，目前该方法被CAT filter 自动调用，app 无需调用此方法
     * */
    public static void logRemoteCallService(HttpServletRequest request){
        String layerContext = request.getHeader(LayersCatContext.CAT_LAYER_CONTEXT);
        System.out.println("LayerContext:" + layerContext);
        if(StringUtils.isEmpty(layerContext)){
            return;
        }

        try {
            LayersCatContext ctx = GSON.fromJson(layerContext, LayersCatContext.class);
            if (ctx.getCatUri() == null) {
                return;
            }
            Cat.logRemoteCallServer(ctx);
        }catch (Exception e){
            Cat.logError(e);
        }
    }
}
