package com.dianping.cat.report.page.chain;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.mvc.AbstractReportModel;
import com.dianping.cat.report.ReportPage;
import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DONGJA3 on 4/27/2017.
 */
@ModelMeta(ChainAnalyzer.ID)
public class Model  extends AbstractReportModel<Action , ReportPage, Context> {
    @EntityMeta
    ChainReport m_report;
    public Model(Context ctx) {
        super(ctx);
    }

    @Override
    public String getDomain() {
        if (m_report == null) {
            return getDisplayDomain();
        } else {
            return m_report.getDomain();
        }
    }

    @Override
    public List<String> getDomains() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(getDomain());
        return arrayList;
    }

    @Override
    public Action getDefaultAction() {
        return Action.HOURLY_REPORT;
    }

    public ChainReport getReport() {
        return m_report;
    }

    public void setReport(ChainReport m_report) {
        this.m_report = m_report;
    }
}
