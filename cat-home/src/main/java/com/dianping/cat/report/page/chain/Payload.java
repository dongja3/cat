package com.dianping.cat.report.page.chain;

import com.dianping.cat.mvc.AbstractReportPayload;
import com.dianping.cat.report.ReportPage;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

/**
 * Created by DONGJA3 on 4/27/2017.
 */
public class Payload  extends AbstractReportPayload<Action,ReportPage> {

    @FieldMeta("op")
    private Action m_action;

    @FieldMeta("name")
    private String m_name;

    @FieldMeta("xml")
    private boolean m_xml;

    public Payload() {
        super(ReportPage.CHAIN);
    }


    public String getName() {
        return m_name;
    }

    public void setAction(String action) {
        m_action = Action.getByName(action, Action.HOURLY_REPORT);
    }

    public void setName(String name) {
        m_name = name;
    }

    @Override
    public Action getAction() {
        return m_action;
    }

    @Override
    public void setPage(String page) {
        m_page = ReportPage.getByName(page, ReportPage.CHAIN);
    }


    @Override
    public void validate(ActionContext<?> ctx) {
        if (m_action == null) {
            m_action = Action.HOURLY_REPORT;
        }
    }

    public void setXml(boolean xml) {
        m_xml = xml;
    }

    public boolean isXml(){
        return m_xml;
    }
}
