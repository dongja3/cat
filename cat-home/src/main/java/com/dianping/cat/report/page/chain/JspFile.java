package com.dianping.cat.report.page.chain;

public enum JspFile {

	HISTORY_REPORT("/jsp/report/chain/chainHistoryReport.jsp"),

	HOURLY_REPORT("/jsp/report/chain/chain.jsp"),

	HISTORY_GROUP_REPORT("/jsp/report/chain/chainHistoryGroupReport.jsp"),

	HOURLY_GROUP_REPORT("/jsp/report/chain/chainGroup.jsp");

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
