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
        Transaction t = SqlTransactionContext.getTransaction();
        if(t==null){
            return result;
        }

        if(query instanceof UpdateObjectQuery){
            t.addData("[U]" +query.getReferenceClass().getSimpleName()+"\n" );
        }else if(query instanceof InsertObjectQuery){
            t.addData("[I]" +query.getReferenceClass().getSimpleName() +"\n");
        }else  if(query instanceof DeleteObjectQuery){
            t.addData("[D]" +query.getReferenceClass().getSimpleName() +"\n");
        }else{
            if(query.getSQLString()==null){
                t.addData(query.toString()+"\n");
            }else{
                t.addData(query.getSQLString()+"\n");
            }
        }

        return result;
    }

    @Override
    public void endOperationProfile(String operationName) {

    }

}
