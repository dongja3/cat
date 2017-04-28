package com.dianping.cat.report.page.chain.service;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.ChainReportMerger;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultSaxParser;
import com.dianping.cat.mvc.ApiPayload;
import com.dianping.cat.report.ReportBucket;
import com.dianping.cat.report.ReportBucketManager;
import com.dianping.cat.report.service.LocalModelService;
import com.dianping.cat.report.service.ModelPeriod;
import com.dianping.cat.report.service.ModelRequest;
import org.unidal.lookup.annotation.Inject;

import java.util.List;

public class LocalChainService extends LocalModelService<ChainReport> {

	public static final String ID = ChainAnalyzer.ID;

	@Inject
	private ReportBucketManager m_bucketManager;

	public LocalChainService() {
		super(ChainAnalyzer.ID);
	}


	@Override
	public String buildReport(ModelRequest request, ModelPeriod period, String domain, ApiPayload payload)
	      throws Exception {
		List<ChainReport> reports = super.getReport(period, domain);
		ChainReport report = null;

		if (reports != null) {
			report = new ChainReport(domain);
			ChainReportMerger merger = new ChainReportMerger(report);
			for (ChainReport tmp : reports) {
				tmp.accept(merger);
			}

			report = merger.getChainReport();
		}

		if ((report == null) && period.isLast()) {
			long startTime = request.getStartTime();
			report = getReportFromLocalDisk(startTime, domain);
		}
		return filterReport(payload, report);

	}

	private ChainReport getReportFromLocalDisk(long timestamp, String domain) throws Exception {
		ChainReport report = new ChainReport(domain);
		ChainReportMerger  merger = new ChainReportMerger(report);

		for (int i = 0; i < ANALYZER_COUNT; i++) {
			ReportBucket bucket = null;
			try {
				bucket = m_bucketManager.getReportBucket(timestamp, ChainAnalyzer.ID, i);
				String xml = bucket.findById(domain);

				if (xml != null) {
					ChainReport tmp = DefaultSaxParser.parse(xml);

					tmp.accept(merger);
				}
			} finally {
				if (bucket != null) {
					m_bucketManager.closeBucket(bucket);
				}
			}
		}
		return report;
	}



	private String filterReport(ApiPayload payload, ChainReport report) {
		String name = payload.getName();
		ChainReportFilter filter = new ChainReportFilter(name);
		return filter.buildXml(report);
	}

	public static class ChainReportFilter extends com.dianping.cat.consumer.chain.model.transform.DefaultXmlBuilder {

		public ChainReportFilter(String name) {
			super(true, new StringBuilder(DEFAULT_SIZE));
		}

	}



}
