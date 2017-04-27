package com.dianping.cat.report.task.heartbeat;

import com.dianping.cat.consumer.heartbeat.HeartbeatAnalyzer;
import com.dianping.cat.helper.TimeHelper;
import com.dianping.cat.report.task.TaskBuilder;
import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

public class HeartBeatReportBuilderTest extends ComponentTestCase {

	@Test
	public void testDailyTask() {
		TaskBuilder builder = lookup(TaskBuilder.class, HeartbeatAnalyzer.ID);

		builder.buildDailyTask("heartbeat", "cat", TimeHelper.getYesterday());
	}

}
