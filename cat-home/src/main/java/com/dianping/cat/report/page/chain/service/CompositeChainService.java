package com.dianping.cat.report.page.chain.service;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.ChainReportMerger;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.report.service.BaseCompositeModelService;
import com.dianping.cat.report.service.BaseRemoteModelService;
import com.dianping.cat.report.service.ModelRequest;
import com.dianping.cat.report.service.ModelResponse;

import java.util.List;

public class CompositeChainService extends BaseCompositeModelService<ChainReport> {
	public CompositeChainService() {
		super(ChainAnalyzer.ID);
	}

	@Override
	protected BaseRemoteModelService<ChainReport> createRemoteService() {
		return new RemoteChainService();
	}

	@Override
	protected ChainReport merge(ModelRequest request, List<ModelResponse<ChainReport>> responses) {
		if (responses.size() == 0) {
			return null;
		}
		ChainReportMerger merger = new ChainReportMerger(new ChainReport(request.getDomain()));
		for (ModelResponse<ChainReport> response : responses) {
			ChainReport model = response.getModel();
			if (model != null) {
				model.accept(merger);
			}
		}

		return merger.getChainReport();
	}
}
