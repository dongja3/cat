package com.dianping.cat.report.page.chain;

import com.dianping.cat.report.ReportPage;
import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<ReportPage, Action, com.dianping.cat.report.page.chain.Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		Action action = model.getAction();
		switch (action) {
		case HOURLY_REPORT:
			return JspFile.HOURLY_REPORT.getPath();
		case HISTORY_REPORT:
			return JspFile.HISTORY_REPORT.getPath();
//		case HISTORY_GROUP_REPORT:
//			return JspFile.HISTORY_GROUP_REPORT.getPath();
//		case HOURLY_GROUP_REPORT:
//			return JspFile.HOURLY_GROUP_REPORT.getPath();
		}

		throw new RuntimeException("Unknown action: " + action);
	}
}
