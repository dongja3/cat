package com.dianping.cat.report.task.chain;

import com.dianping.cat.consumer.chain.ChainReportMerger;
import com.dianping.cat.consumer.chain.ChainStatisticsComputer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.chain.model.transform.DefaultXmlBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.unidal.helper.Files;
import org.unidal.lookup.ComponentTestCase;

/**
 * Created by DONGJA3 on 4/27/2017.
 */
public class ChainReportMergerTest  extends ComponentTestCase {
    @Test
    public void testMerge() throws Exception {
        String oldXml = Files.forIO().readFrom(getClass().getResourceAsStream("ChainReport.xml"), "utf-8");
        ChainReport report1 = DefaultSaxParser.parse(oldXml);
        ChainReport report2 = DefaultSaxParser.parse(oldXml);
        String expected = Files.forIO().readFrom(getClass().getResourceAsStream("ChainReportMergeResult.xml"),
                "utf-8");
        ChainReportMerger merger = new ChainReportMerger(new ChainReport(report1.getDomain()));

        report1.accept(merger);
        report2.accept(merger);

        ChainReport report = merger.getChainReport();
        ChainStatisticsComputer visitor = new ChainStatisticsComputer();
        visitor.visitChainReport(report);
        String actual = new DefaultXmlBuilder().buildXml(report);
       // System.out.println(actual);
         Assert.assertEquals("Check the merge result!", expected.replace("\r", ""), actual.replace("\r", ""));

    }
}
