package com.dianping.cat.consumer.chain;

import com.dianping.cat.Cat;
import com.dianping.cat.Constants;
import com.dianping.cat.analysis.AbstractMessageAnalyzer;
import com.dianping.cat.config.server.ServerFilterConfigManager;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.DefaultReportManager;
import com.dianping.cat.report.ReportManager;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.lookup.annotation.Inject;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

/**
 * Created by DONGJA3 on 4/21/2017.
 */
public class ChainAnalyzer extends AbstractMessageAnalyzer<ChainReport> implements LogEnabled {
    @Inject
    private ChainDelegate m_delegate;

    @Inject(ID)
    private ReportManager<ChainReport> m_reportManager;

    @Inject
    private ServerFilterConfigManager m_serverFilterConfigManager;

    public static final String ID = "chain";


    @Override
    public void doCheckpoint(boolean atEnd) {
        if (atEnd && !isLocalMode()) {
            m_reportManager.storeHourlyReports(getStartTime(), DefaultReportManager.StoragePolicy.FILE_AND_DB, m_index);
        } else {
            m_reportManager.storeHourlyReports(getStartTime(), DefaultReportManager.StoragePolicy.FILE, m_index);
        }
    }

    @Override
    public ReportManager<?> getReportManager() {
        return m_reportManager;
    }

    @Override
    public ChainReport getReport(String domain) {
        if (!Constants.ALL.equals(domain)) {
            try {
                return queryReport(domain);
            } catch (Exception e) {
                try {
                    return queryReport(domain);
                    // for concurrent modify exception
                } catch (ConcurrentModificationException ce) {
                    Cat.logEvent("ConcurrentModificationException", domain, Event.SUCCESS, null);
                    return new ChainReport();
                }
            }
        } else {
            Map<String, ChainReport> reports = m_reportManager.getHourlyReports(getStartTime());
            return m_delegate.createAggregatedReport(reports);
        }
    }

    @Override
    protected void loadReports() {
        m_reportManager.loadHourlyReports(getStartTime(), DefaultReportManager.StoragePolicy.FILE, m_index);
    }

    @Override
    protected void process(MessageTree tree) {
        String domain = tree.getDomain();
        ChainReport report = m_reportManager.getHourlyReport(getStartTime(), domain, true);
        Message message = tree.getMessage();
        if (message instanceof Transaction) {
            Transaction root = (Transaction) message;
            processTransaction(report, root);
        }
    }

    protected void processTransaction(ChainReport report,Transaction t) {
       TransactionChain rootChain = report.findOrCreateChain(t.getName());

        rootChain.setSum(rootChain.getSum()+t.getDurationInMillis());
        rootChain.incCallCount();
        rootChain.setAvg(rootChain.getSum() / rootChain.getCallCount());
        rootChain.setLevel(0);
        rootChain.setDependency(1);
        rootChain.setTimeRatio(1);

        List<Message> children = t.getChildren();
        for (Message child : children) {
            if (child instanceof Transaction) {
                add2TransactionChain((Transaction)child,rootChain,1, rootChain);
            }
        }

    }

    private void add2TransactionChain(Transaction t, TransactionChain parentTxnChain, int level,TransactionChain root){
        TransactionChain tc = parentTxnChain.findOrCreateChain(t.getName());
        tc.setSum(tc.getSum() + t.getDurationInMillis());
        tc.incCallCount();
        tc.setAvg(tc.getSum() / tc.getCallCount());
        tc.setLevel(level);

        float dependency = (float)tc.getCallCount()/(float)root.getCallCount();
        if(dependency>1.0){
            dependency=(float)1;
        }
        tc.setDependency(dependency);
        tc.setTimeRatio(tc.getAvg()/root.getAvg());

        level++;
        for(Message childMessage : t.getChildren()){
            if (childMessage instanceof Transaction) {
                add2TransactionChain((Transaction) childMessage, tc,level,root);
            }
        }
    }

    @Override
    public int getAnanlyzerCount() {
        return 2;
    }

    @Override
    public void enableLogging(Logger logger) {
        m_logger = logger;
    }

    private ChainReport queryReport(String domain) {
        long period = getStartTime();
        ChainReport report = m_reportManager.getHourlyReport(period, domain, false);
        return report;
    }

}
