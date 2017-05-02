package com.dianping.cat.report.page.chain.service;

import com.dianping.cat.Cat;
import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.consumer.chain.ChainReportMerger;
import com.dianping.cat.consumer.chain.ChainStatisticsComputer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultNativeParser;
import com.dianping.cat.core.dal.*;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.service.AbstractReportService;
import org.unidal.dal.jdbc.DalException;
import org.unidal.dal.jdbc.DalNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Created by DONGJA3 on 4/26/2017.
 */
public class ChainReportService  extends AbstractReportService<ChainReport> {
    @Override
    public ChainReport makeReport(String domain, Date start, Date end) {
        ChainReport report = new ChainReport(domain);
        report.setStartTime(start);
        report.setEndTime(end);
        return report;
    }

    @Override
    public ChainReport queryDailyReport(String domain, Date start, Date end) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        String name = ChainAnalyzer.ID;
        ChainReportMerger merger = new ChainReportMerger(new ChainReport(domain));
        for (; startTime < endTime; startTime = startTime + TimeHelper.ONE_DAY) {
            try {
                DailyReport report = m_dailyReportDao.findByDomainNamePeriod(domain, name, new Date(startTime), DailyReportEntity.READSET_FULL);
                 ChainReport  chainReport = queryFromDailyBinary(report.getId(), domain);
                chainReport.accept(merger);
            } catch (DalNotFoundException e) {
                // ignore
            } catch (Exception e) {
                Cat.logError(e);
            }
        }
        ChainReport chainReport = merger.getChainReport();
        ChainStatisticsComputer visitor = new ChainStatisticsComputer();
        visitor.visitChainReport(chainReport);
        chainReport.setStartTime(start);
        chainReport.setEndTime(end);
        return chainReport;
    }

    @Override
    public ChainReport queryHourlyReport(String domain, Date start, Date end) {
        ChainReportMerger merger = new ChainReportMerger(new ChainReport(domain));
        long startTime = start.getTime();
        long endTime = end.getTime();
        String name = ChainAnalyzer.ID;

        for (; startTime < endTime; startTime = startTime + TimeHelper.ONE_HOUR) {
            List<HourlyReport> reports = null;
            try {
                reports = m_hourlyReportDao.findAllByDomainNamePeriod(new Date(startTime), domain, name, HourlyReportEntity.READSET_FULL);
            } catch (DalException e) {
                Cat.logError(e);
            }
            if (reports != null) {
                for (HourlyReport report : reports) {
                    try {
                        ChainReport reportModel = queryFromHourlyBinary(report.getId(), domain);
                        reportModel.accept(merger);
                    } catch (DalNotFoundException e) {
                        // ignore
                    } catch (Exception e) {
                        Cat.logError(e);
                    }
                }
            }
        }
        ChainReport chainReport = merger.getChainReport();
        ChainStatisticsComputer visitor = new ChainStatisticsComputer();
        visitor.visitChainReport(chainReport);
        chainReport.setStartTime(start);
        chainReport.setEndTime(end);
        return chainReport;
    }

    @Override
    public ChainReport queryMonthlyReport(String domain, Date start) {
        ChainReport chainReport = new ChainReport(domain);

        try {
            MonthlyReport entity = m_monthlyReportDao.findReportByDomainNamePeriod(start, domain, ChainAnalyzer.ID, MonthlyReportEntity.READSET_FULL);
            chainReport = queryFromMonthlyBinary(entity.getId(), domain);
        } catch (DalNotFoundException e) {
            // ignore
        } catch (Exception e) {
            Cat.logError(e);
        }
        return chainReport;
    }

    @Override
    public ChainReport queryWeeklyReport(String domain, Date start) {
        ChainReport chainReport = new ChainReport(domain);
        try {
            WeeklyReport entity = m_weeklyReportDao.findReportByDomainNamePeriod(start, domain, ChainAnalyzer.ID, WeeklyReportEntity.READSET_FULL);
            chainReport = queryFromWeeklyBinary(entity.getId(), domain);
        } catch (DalNotFoundException e) {
            // ignore
        } catch (Exception e) {
            Cat.logError(e);
        }
        return chainReport;
    }


    private ChainReport queryFromHourlyBinary(int id, String domain) throws DalException {
        HourlyReportContent content = m_hourlyReportContentDao.findByPK(id, HourlyReportContentEntity.READSET_FULL);

        if (content != null) {
            return DefaultNativeParser.parse(content.getContent());
        } else {
            return new ChainReport(domain);
        }
    }

    private ChainReport queryFromMonthlyBinary(int id, String domain) throws DalException {
        MonthlyReportContent content = m_monthlyReportContentDao.findByPK(id, MonthlyReportContentEntity.READSET_FULL);

        if (content != null) {
            return DefaultNativeParser.parse(content.getContent());
        } else {
            return new ChainReport(domain);
        }
    }

    private ChainReport queryFromWeeklyBinary(int id, String domain) throws DalException {
        WeeklyReportContent content = m_weeklyReportContentDao.findByPK(id, WeeklyReportContentEntity.READSET_FULL);

        if (content != null) {
            return DefaultNativeParser.parse(content.getContent());
        } else {
            return new ChainReport(domain);
        }
    }
    private ChainReport queryFromDailyBinary(int id, String domain) throws DalException {
        DailyReportContent content = m_dailyReportContentDao.findByPK(id, DailyReportContentEntity.READSET_FULL);

        if (content != null) {
            return DefaultNativeParser.parse(content.getContent());
        } else {
            return new ChainReport(domain);
        }
    }
}
