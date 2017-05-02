package com.dianping.cat.report.page.chain.task;

import com.dianping.cat.Cat;
import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.ChainReportMerger;
import com.dianping.cat.consumer.chain.ChainStatisticsComputer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultNativeBuilder;
import com.dianping.cat.core.dal.*;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.page.chain.service.ChainReportService;
import com.dianping.cat.report.task.TaskBuilder;
import com.dianping.cat.report.task.TaskHelper;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.lookup.annotation.Inject;

import java.util.Date;

/**
 * Created by DONGJA3 on 4/26/2017.
 */
public class ChainReportBuilder  implements TaskBuilder, LogEnabled {
    public static final String ID = ChainAnalyzer.ID;

    @Inject
    protected ChainReportService m_reportService;

    private Logger m_logger;
    @Override
    public void enableLogging(Logger logger) {
        this.m_logger=logger;
    }

    @Override
    public boolean buildDailyTask(String name, String domain, Date period) {
        m_logger.info("Start build daily Chain...");
        try {
            Date end = TaskHelper.tomorrowZero(period);
            ChainReport chainReport = queryReportsByDuration(domain, period, end, TimeHelper.ONE_HOUR);

            DailyReport report = new DailyReport();

            report.setCreationDate(new Date());
            report.setDomain(domain);
            report.setIp(NetworkInterfaceManager.INSTANCE.getLocalHostAddress());
            report.setName(name);
            report.setPeriod(period);
            report.setType(1);
            byte[] binaryContent = DefaultNativeBuilder.build(chainReport);
            m_logger.info("Chain daily report:"+ report);
            return m_reportService.insertDailyReport(report, binaryContent);
        } catch (Exception e) {
            m_logger.error(e.getMessage(), e);
            Cat.logError(e);
            return false;
        }
    }


    @Override
    public boolean buildHourlyTask(String name, String domain, Date period) {
        m_logger.info("Build hourly Chain...");
        return true;
    }

    @Override
    public boolean buildMonthlyTask(String name, String domain, Date period) {
        Date end = null;

        if (period.equals(TimeHelper.getCurrentMonth())) {
            end = TimeHelper.getCurrentDay();
        } else {
            end = TaskHelper.nextMonthStart(period);
        }
        ChainReport chainReport = queryReportsByDuration(domain, period, end, TimeHelper.ONE_DAY);
        MonthlyReport report = new MonthlyReport();

        report.setCreationDate(new Date());
        report.setDomain(domain);
        report.setIp(NetworkInterfaceManager.INSTANCE.getLocalHostAddress());
        report.setName(name);
        report.setPeriod(period);
        report.setType(1);
        byte[] binaryContent = DefaultNativeBuilder.build(chainReport);
        return m_reportService.insertMonthlyReport(report, binaryContent);
    }

    @Override
    public boolean buildWeeklyTask(String name, String domain, Date period) {
        Date end = null;

        if (period.equals(TimeHelper.getCurrentWeek())) {
            end = TimeHelper.getCurrentDay();
        } else {
            end = new Date(period.getTime() + TimeHelper.ONE_WEEK);
        }

        ChainReport chainReport = queryReportsByDuration(domain, period, end, TimeHelper.ONE_DAY);
        WeeklyReport report = new WeeklyReport();

        report.setCreationDate(new Date());
        report.setDomain(domain);
        report.setIp(NetworkInterfaceManager.INSTANCE.getLocalHostAddress());
        report.setName(name);
        report.setPeriod(period);
        report.setType(1);

        byte[] binaryContent = DefaultNativeBuilder.build(chainReport);
        return m_reportService.insertWeeklyReport(report, binaryContent);
    }


    private ChainReport queryReportsByDuration(String domain, Date start, Date end, long loopTimeSpan) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        ChainReportMerger merger = new ChainReportMerger(new ChainReport(domain));
        for (; startTime < endTime; startTime = startTime + loopTimeSpan) {
            ChainReport report = m_reportService.queryReport(domain, new Date(startTime), new Date(startTime + TimeHelper.ONE_DAY));
            report.accept(merger);
        }
        ChainReport chainReport = merger.getChainReport();
        ChainStatisticsComputer visitor = new ChainStatisticsComputer();
        visitor.visitChainReport(chainReport);
        chainReport.setStartTime(start);
        chainReport.setEndTime(end);
        return chainReport;
    }

}
