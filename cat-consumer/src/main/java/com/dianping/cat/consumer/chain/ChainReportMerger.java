package com.dianping.cat.consumer.chain;

import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain;
import com.dianping.cat.consumer.chain.model.transform.DefaultMerger;

/**
 * Created by DONGJA3 on 4/26/2017.
 */
public class ChainReportMerger extends DefaultMerger {
    public ChainReportMerger(ChainReport chainReport){
        super(chainReport);
    };
    @Override
    protected void mergeChain(TransactionChain to, TransactionChain from) {
        to.incCallCount(from.getCallCount());
        to.setSum(to.getSum() + from.getSum());
        to.setLevel(from.getLevel());
    }


}
