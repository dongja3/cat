package com.dianping.cat.consumer.chain;

import com.dianping.cat.Cat;
import com.dianping.cat.Constants;
import com.dianping.cat.config.server.ServerFilterConfigManager;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.config.AllReportConfigManager;
import com.dianping.cat.report.ReportDelegate;
import com.dianping.cat.task.TaskManager;
import org.unidal.lookup.annotation.Inject;

import java.util.Map;

import static com.dianping.cat.Constants.ALL;

/**
 * Created by DONGJA3 on 4/21/2017.
 */
public class ChainDelegate implements ReportDelegate<ChainReport> {
    @Inject
    private TaskManager m_taskManager;

    @Inject
    private ServerFilterConfigManager m_configManager;

    @Inject
    private AllReportConfigManager m_transactionManager;

    @Override
    public void afterLoad(Map<String, ChainReport> reports) {

    }

    @Override
    public void beforeSave(Map<String, ChainReport> reports) {

    }

    @Override
    public byte[] buildBinary(ChainReport report) {
        return new byte[0];
    }

    @Override
    public ChainReport parseBinary(byte[] bytes) {
        return null;
    }

    @Override
    public String buildXml(ChainReport report) {
        return null;
    }

    @Override
    public String getDomain(ChainReport report) {
        return null;
    }

    @Override
    public ChainReport makeReport(String domain, long startTime, long duration) {
        ChainReport report = new ChainReport(domain);
        return report;
    }

    @Override
    public ChainReport mergeReport(ChainReport old, ChainReport other) {
        return null;
    }

    @Override
    public ChainReport parseXml(String xml) throws Exception {
        return null;
    }

    @Override
    public boolean createHourlyTask(ChainReport report) {
        return false;
    }


    public ChainReport createAggregatedReport(Map<String, ChainReport> reports) {
        if (reports.size() > 0) {
            ChainReport first = reports.values().iterator().next();
            ChainReportAggregator visitor = new ChainReportAggregator(first, m_transactionManager);

            try {
                for (ChainReport report : reports.values()) {
                    String domain = report.getDomain();

                    if (!domain.equals(Constants.ALL)) {
                        visitor.visitChainReport(report);
                    }
                }
            } catch (Exception e) {
                Cat.logError(e);
            }
            return first;
        } else {
            return new ChainReport(ALL);
        }
    }
}
