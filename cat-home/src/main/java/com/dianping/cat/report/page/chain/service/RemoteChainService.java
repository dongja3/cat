package com.dianping.cat.report.page.chain.service;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultSaxParser;
import com.dianping.cat.report.service.BaseRemoteModelService;
import org.xml.sax.SAXException;

import java.io.IOException;

public class RemoteChainService extends BaseRemoteModelService<ChainReport> {
	public RemoteChainService() {
		super(ChainAnalyzer.ID);
	}

	@Override
	protected ChainReport buildModel(String xml) throws SAXException, IOException {
		return DefaultSaxParser.parse(xml);
	}
}
