package com.dianping.cat.consumer.chain;

import com.dianping.cat.Cat;
import com.dianping.cat.Constants;
import com.dianping.cat.config.server.ServerFilterConfigManager;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultNativeBuilder;
import com.dianping.cat.consumer.chain.model.transform.DefaultNativeParser;
import com.dianping.cat.consumer.chain.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.config.AllReportConfigManager;
import com.dianping.cat.report.ReportDelegate;
import com.dianping.cat.task.TaskManager;
import org.unidal.lookup.annotation.Inject;

import java.util.Date;
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

    private ChainStatisticsComputer m_computer = new ChainStatisticsComputer();
    @Override
    public void afterLoad(Map<String, ChainReport> reports) {

    }

    @Override
    public void beforeSave(Map<String, ChainReport> reports) {

        if (reports.size() > 0) {
            ChainReport all = createAggregatedReport(reports);
            reports.put(all.getDomain(), all);
        }
    }

    @Override
    public byte[] buildBinary(ChainReport report){
      return DefaultNativeBuilder.build(report);
    }

    @Override
    public ChainReport parseBinary(byte[] bytes) {
        return DefaultNativeParser.parse(bytes);
    }

    @Override
    public String buildXml(ChainReport report) {
        report.accept(m_computer);
        return report.toString();
    }

    @Override
    public String getDomain(ChainReport report) {
        return report.getDomain();
    }

    @Override
    public ChainReport makeReport(String domain, long startTime, long duration) {
        ChainReport report = new ChainReport(domain);
        report.setStartTime(new Date(startTime));
        report.setEndTime(new Date(startTime + duration - 1));
        return report;
    }

    @Override
    public ChainReport mergeReport(ChainReport old, ChainReport other) {
        ChainReportMerger merger = new ChainReportMerger(old);
        other.accept(merger);
        return old;
    }

    @Override
    public ChainReport parseXml(String xml) throws Exception {
        ChainReport report = DefaultSaxParser.parse(xml);
        return report;
    }

    @Override
    public boolean createHourlyTask(ChainReport report) {
        String domain = report.getDomain();
        if (domain.equals(Constants.ALL)) {
            return m_taskManager.createTask(report.getStartTime(), domain, ChainAnalyzer.ID, TaskManager.TaskProlicy.ALL_EXCLUED_HOURLY);
        } else if (m_configManager.validateDomain(domain)) {
            return m_taskManager.createTask(report.getStartTime(), report.getDomain(), ChainAnalyzer.ID, TaskManager.TaskProlicy.ALL);
        } else {
            return true;
        }
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
