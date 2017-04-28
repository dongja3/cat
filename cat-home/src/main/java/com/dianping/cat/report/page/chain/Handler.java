package com.dianping.cat.report.page.chain;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain;
import com.dianping.cat.mvc.PayloadNormalizer;
import com.dianping.cat.report.ReportPage;
import com.dianping.cat.report.page.chain.service.ChainReportService;
import com.dianping.cat.report.service.ModelRequest;
import com.dianping.cat.report.service.ModelResponse;
import com.dianping.cat.report.service.ModelService;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by DONGJA3 on 4/27/2017.
 */
public class Handler implements PageHandler<Context> {

    @Inject(type = ModelService.class, value = ChainAnalyzer.ID)
    private ModelService<ChainReport> m_service;

    @Inject
    private ChainReportService m_reportService;

    @Inject
    private JspViewer m_jspViewer;

    @Inject
    private XmlViewer m_xmlViewer;

    @Inject
    private PayloadNormalizer m_normalizePayload;

    @Override
    @PayloadMeta(Payload.class)
    @InboundActionMeta(name="chain")
    public void handleInbound(Context context) throws ServletException, IOException {

    }

    @Override
    @OutboundActionMeta(name = "chain")
    public void handleOutbound(Context ctx) throws ServletException, IOException {
        Model model = new Model(ctx);
        Payload payload = ctx.getPayload();
        normalize(model, payload);
        String domain = payload.getDomain();
        String name = payload.getName();
        Action action = payload.getAction();
        ChainReport report;
        switch(action){
            case HOURLY_REPORT:
                 report = getHourlyReport(payload);
                break;
//            case HOURLY_GROUP_REPORT:
//                report = getHourlyReport(payload);
//                break;
            case HISTORY_REPORT:
                report = m_reportService.queryReport(domain, payload.getHistoryStartDate(), payload.getHistoryEndDate());
                break;
//            case HISTORY_GROUP_REPORT:
//                report = m_reportService.queryReport(domain, payload.getHistoryStartDate(), payload.getHistoryEndDate());
//                break;
            default:
                report = getHourlyReport(payload);
        }
        report = filterReport(name,report);
        model.setReport(report);
        if (payload.isXml()) {
            m_xmlViewer.view(ctx, model);
        } else {
            m_jspViewer.view(ctx, model);
        }
    }


    private ChainReport getHourlyReport(Payload payload) {
        String domain = payload.getDomain();
        ModelRequest request = new ModelRequest(domain, payload.getDate());
        if (m_service.isEligable(request)) {
            ModelResponse<ChainReport> response = m_service.invoke(request);
            ChainReport report = response.getModel();
            return report;
        } else {
            throw new RuntimeException("Internal error: no eligible chain service registered for " + request + "!");
        }
    }

    private ChainReport filterReport(String name, ChainReport chainReport){
        if(name==null){
            return chainReport;
        }
        ChainReport report = new ChainReport(chainReport.getDomain());
        for(TransactionChain chain : chainReport.getChains().values()){
            if(chain.getTransactionName().equals(name)){
                report.addChain(chain);
                break;
            }
        }
        return report;
    }


    private void normalize(Model model, Payload payload) {
        m_normalizePayload.normalize(model, payload);
        model.setPage(ReportPage.CHAIN);
        model.setAction(payload.getAction());
    }
}
