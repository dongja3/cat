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

import java.util.HashSet;
import java.util.Set;

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
        Set<String> sqlSet = new HashSet<String>();

        if(query instanceof UpdateObjectQuery){
            sqlSet.add("[U]" + query.getReferenceClass().getSimpleName());
        }else if(query instanceof InsertObjectQuery){
            sqlSet.add("[I]" + query.getReferenceClass().getSimpleName());
        }else  if(query instanceof DeleteObjectQuery){
            sqlSet.add("[D]" + query.getReferenceClass().getSimpleName());
        }else{
            if(query.getSQLString()==null){
                sqlSet.add(query.toString());
            }else{
                sqlSet.add(query.getSQLString()+"\n");
            }
        }

        for(String sql : sqlSet){
            t.addData(sql+"\n");
        }

        return result;
    }

    @Override
    public void endOperationProfile(String operationName) {

    }

}
