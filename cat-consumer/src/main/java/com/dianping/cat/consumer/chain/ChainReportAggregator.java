package com.dianping.cat.consumer.chain;

import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.BaseVisitor;
import com.dianping.cat.consumer.config.AllReportConfigManager;

/**
 * Created by DONGJA3 on 4/21/2017.
 */
public class ChainReportAggregator extends BaseVisitor {
    private ChainReport m_report;

    private AllReportConfigManager m_configManager;
    public ChainReportAggregator(ChainReport report, AllReportConfigManager configManager) {
        m_report = report;
        m_configManager = configManager;
    }
    @Override
    public void visitChainReport(ChainReport chainReport) {
//        m_report.setDetails(m_report.getDetails() + chainReport.getDetails());
    }
}
