package com.dianping.cat.eclipselink;

import com.dianping.cat.message.Transaction;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.queries.InsertObjectQuery;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.tools.profiler.PerformanceProfiler;

/**
 * Created by DONGJA3 on 5/19/2017.
 */
class CatQueryProfiler extends PerformanceProfiler {

    @Override
    public Object profileExecutionOfQuery(DatabaseQuery query, Record row, AbstractSession session) {
        Object result = session.internalExecuteQuery(query, (AbstractRecord) row);
        Transaction t = CalSqlTransactionContext.getTransaction();
        if(t==null){
            return result;
        }
        String txnDataItem=null;

        if(query instanceof UpdateObjectQuery){
            txnDataItem=  "[U]" + query.getReferenceClass().getSimpleName();
        }else if(query instanceof InsertObjectQuery){
            txnDataItem="[I]" + query.getReferenceClass().getSimpleName();
        }else  if(query instanceof DeleteObjectQuery){
            txnDataItem="[D]" + query.getReferenceClass().getSimpleName();
        }else{
            if(query.getSQLString()==null){
                txnDataItem=query.toString();
            }else{
                txnDataItem=query.getSQLString();
            }
        }
        if(txnDataItem==null){
            return result;
        }

        Object data = t.getData();
        if(data==null){
            t.addData(txnDataItem+"\n");
            return result;
        }

        String dataString = data.toString();
        if(!dataString.contains(txnDataItem)){
            t.addData(txnDataItem+"\n");
        }

        return result;
    }

    @Override
    public void endOperationProfile(String operationName) {

    }

}
