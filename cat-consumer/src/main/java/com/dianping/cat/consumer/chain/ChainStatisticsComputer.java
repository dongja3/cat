package com.dianping.cat.consumer.chain;

import com.dianping.cat.consumer.chain.model.entity.ChainReport;
import com.dianping.cat.consumer.chain.model.entity.TransactionChain;
import com.dianping.cat.consumer.chain.model.transform.BaseVisitor;

/**
 * Created by DONGJA3 on 4/26/2017.
 */
public class ChainStatisticsComputer extends BaseVisitor {

    public void visitChain(TransactionChain chain,long  rootCallCount,double rootTimeSum) {
        chain.setDependency((float) chain.getCallCount()/(float) rootCallCount);
        chain.setTimeRatio(chain.getSum() / rootTimeSum);
        chain.setAvg(chain.getSum()/chain.getCallCount());
        for (TransactionChain child : chain.getChildren()) {
            visitChain(child,rootCallCount,rootTimeSum);
        }
    }

    @Override
    public void visitChainReport(ChainReport chainReport) {
        for (TransactionChain transactionChain : chainReport.getChains().values()) {
            visitChain(transactionChain,transactionChain.getCallCount(), transactionChain.getSum());
        }
    }
}
