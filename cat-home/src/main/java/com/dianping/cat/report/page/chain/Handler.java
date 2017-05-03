package com.dianping.cat.report.page.chain;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain2;
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
        ChainReport filterReport= new ChainReport(chainReport.getDomain());
        filterReport.setStartTime(chainReport.getStartTime());
        filterReport.setEndTime(chainReport.getEndTime());
        if(name==null){
            for(String txnName : chainReport.getChains().keySet()){
                TransactionChain chain = chainReport.getChains().get(txnName);
                if(chain.getChildren().size()>0){
                    filterReport.addChain(chain);
                }
            }
            return filterReport;
        }
        for(TransactionChain chain : chainReport.getChains().values()){
            if(chain.getTransactionName().equals(name)){
                addChain2(chain,filterReport,chain.getCallCount(),chain.getSum());
                break;
            }
        }
        return filterReport;
    }

    private void addChain2(TransactionChain chain1, ChainReport report,long rootCallCount, double rootSum){
        TransactionChain2 chain2 = new TransactionChain2();
        chain2.setSum(chain1.getSum());
        chain2.setCallCount(chain1.getCallCount());
        double dependency = (float) chain1.getCallCount() / (float) rootCallCount;
        chain2.setDependency(dependency);
        chain2.setTimeRatio(chain1.getSum() / rootSum);
        StringBuilder sb = new StringBuilder();
        if(chain1.getLevel()>0){
            if(chain2.getDependency()>0.85){
                sb.append("[强依赖]");
            }
            if(chain2.getTimeRatio()>0.5){
                sb.append("[瓶颈]");
            }
        }
        chain2.setRemark(sb.toString());

        sb = new StringBuilder();
        for(int i=0;i<chain1.getLevel();i++){
            sb.append("~");
        }
        chain2.setDomain(chain1.getDomain());
        chain2.setAvg(chain1.getSum()/chain1.getCallCount());
        chain2.setLevel(chain1.getLevel());
        chain2.setTransactionName(sb.toString()+ chain1.getTransactionName());
        report.addChain2(chain2);
        for(TransactionChain child : chain1.getChildren()){
            addChain2(child,report,rootCallCount,rootSum);
        }
    }




    private void normalize(Model model, Payload payload) {
        m_normalizePayload.normalize(model, payload);
        model.setPage(ReportPage.CHAIN);
        model.setAction(payload.getAction());
    }
}
