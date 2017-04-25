package com.dianping.cat.consumer.chain;

import com.dianping.cat.Constants;
import com.dianping.cat.analysis.MessageAnalyzer;
import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.message.spi.internal.DefaultMessageTree;
import org.junit.Before;
import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DONGJA3 on 4/21/2017.
 */
public class ChainAnalyzerTest extends ComponentTestCase {
    private long m_timestamp;

    private ChainAnalyzer m_analyzer;

    private String m_domain = "group";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        m_timestamp = System.currentTimeMillis() - System.currentTimeMillis() % (3600 * 1000);
        m_analyzer = (ChainAnalyzer) lookup(MessageAnalyzer.class, ChainAnalyzer.ID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        Date date = sdf.parse("20120101 00:00");

        m_analyzer.initialize(date.getTime(), Constants.HOUR, Constants.MINUTE * 5);
        m_timestamp = System.currentTimeMillis() - System.currentTimeMillis() % (3600 * 1000);
    }

    @Test
    public void testProcess() throws Exception {
        for (int i = 1; i <=100; i++) {
            MessageTree tree = generateMessageTree(i);
            m_analyzer.process(tree);
        }

        ChainReport report = m_analyzer.getReport(m_domain);
        System.out.println(report);


//        report.accept(new TransactionStatisticsComputer());
//
//        String expected = Files.forIO().readFrom(getClass().getResourceAsStream("transaction_analyzer.xml"), "utf-8");
//        Assert.assertEquals(expected.replaceAll("\r", ""), report.toString().replaceAll("\r", ""));
    }

    protected MessageTree generateMessageTree(int i) {
        MessageTree tree = new DefaultMessageTree();

        tree.setMessageId("" + i);
        tree.setDomain(m_domain);
        tree.setHostName("group001");
        tree.setIpAddress("192.168.1.1");

        String txnName = "T" + (i%2);
        DefaultTransaction t1 = new DefaultTransaction("A-1", txnName, null);
        DefaultTransaction t11 = new DefaultTransaction("A-1", txnName  +"_1", null);
        DefaultTransaction t12 = new DefaultTransaction("A-1", txnName  +"_2", null);
        DefaultTransaction t111 = new DefaultTransaction("A-1", txnName +"_1_1", null);
        DefaultTransaction t112 = new DefaultTransaction("A-1", txnName +"_1_2", null);

        t111.setStatus(Message.SUCCESS);
        t111.complete();
        t111.setDurationInMillis(10000);
        t111.setTimestamp(m_timestamp + 10000);

        t112.setStatus(Message.SUCCESS);
        t112.complete();
        t112.setDurationInMillis(20000);
        t112.setTimestamp(m_timestamp + 20000);


        t11.setStatus(Message.SUCCESS);
        t11.addChild(t111);
        t11.addChild(t112);
        t11.complete();
        t11.setDurationInMillis(10000);
        t11.setTimestamp(m_timestamp + 10000);

        t12.setStatus(Message.SUCCESS);
        t12.complete();
        t12.setDurationInMillis(20000);
        t12.setTimestamp(m_timestamp + 20000);

        t1.setStatus(Message.SUCCESS);
        t1.addChild(t11);
        t1.addChild(t12);
        t1.complete();
        t1.setDurationInMillis(80000);
        t1.setTimestamp(m_timestamp + 80000);
        tree.setMessage(t1);
//        System.out.println(tree);
        return tree;
    }
}
