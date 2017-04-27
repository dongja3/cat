package com.dianping.cat.report.task.chain;

import com.dianping.cat.consumer.chain.ChainAnalyzer;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.task.TaskBuilder;
import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

/**
 * Created by DONGJA3 on 4/27/2017.
 */
public class ChainReportBuilderTest  extends ComponentTestCase {
    @Test
    public void testDailyTask() {
        TaskBuilder builder = lookup(TaskBuilder.class, ChainAnalyzer.ID);

        builder.buildDailyTask("Chain", "cat", TimeHelper.getYesterday());
    }

    @Test
    public void testHourlyTask() {
        TaskBuilder builder = lookup(TaskBuilder.class, ChainAnalyzer.ID);
        builder.buildHourlyTask("Chain", "cat", TimeHelper.getYesterday());
    }

    @Test
    public void testWeeklyTask() {
        TaskBuilder builder = lookup(TaskBuilder.class, ChainAnalyzer.ID);
        builder.buildWeeklyTask("Chain", "cat", TimeHelper.getYesterday());
    }

    @Test
    public void testMonthlyTask() {
        TaskBuilder builder = lookup(TaskBuilder.class, ChainAnalyzer.ID);
        builder.buildMonthlyTask("Chain", "cat", TimeHelper.getYesterday());
    }
}
