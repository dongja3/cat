package com.dianping.cat.report.page.chain.service;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.service.BaseHistoricalModelService;
import com.dianping.cat.report.service.ModelRequest;
import org.unidal.lookup.annotation.Inject;

import java.util.Date;

public class HistoricalChainService extends BaseHistoricalModelService<ChainReport> {

	@Inject
	private ChainReportService m_reportService;

	public HistoricalChainService() {
		super(ChainAnalyzer.ID);
	}

	@Override
	protected ChainReport buildModel(ModelRequest request) throws Exception {
		String domain = request.getDomain();
		long date = request.getStartTime();
		ChainReport report = getReportFromDatabase(date, domain);

		return report;
	}

	private ChainReport getReportFromDatabase(long timestamp, String domain) throws Exception {
		return m_reportService.queryReport(domain, new Date(timestamp), new Date(timestamp + TimeHelper.ONE_HOUR));
	}

}
