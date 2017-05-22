package com.dianping.cat.eclipselink;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.server.ClientSession;
import org.eclipse.persistence.tools.profiler.PerformanceProfiler;

/**
 * Created by DONGJA3 on 5/18/2017.
 * http://wiki.eclipse.org/EclipseLink/FAQ/JPA#How_to_get_the_SQL_for_a_Query.3F
 */
public class CatSessionEventAdapter extends SessionEventAdapter implements LogEnabled{
    private Logger m_logger;

    @Override
    public void postAcquireClientSession(SessionEvent event) {
        String callMethod = CalSqlTransactionContext.getCallMethod();
        Transaction t =null;
        if(callMethod!=null){
            t = Cat.newTransaction("JpaQuery", callMethod);
            CalSqlTransactionContext.setSqlTransaction(t);
        }else{
            t = CalSqlTransactionContext.getTransaction();
        }

        Session session = event.getSession();
        if (session instanceof ClientSession) {
            PerformanceProfiler pp = new CatQueryProfiler();
            event.getSession().setProfiler(pp);
            pp.setSession(event.getSession());
        }
    }
    @Override
    public void postReleaseClientSession (SessionEvent event) {
        completeCatTransaction(false);
    }
    @Override
    public void postRollbackTransaction (SessionEvent event) {
        completeCatTransaction(true);
    }

    public void postCommitUnitOfWork(SessionEvent event){
        completeCatTransaction(false);
    }
    private void completeCatTransaction(boolean isRollback){
        if(null== CalSqlTransactionContext.getCallMethod()){
            return;
        }
        Transaction t = CalSqlTransactionContext.getTransaction();
        if(t==null){
            return;
        }
        if(t.isCompleted()){
            CalSqlTransactionContext.clear();
            return;
        }
        if(isRollback){
            t.setStatus(new Exception("rollback"));
        }else{
            t.setStatus(Transaction.SUCCESS);
        }

        t.complete();
        CalSqlTransactionContext.clear();
    }


    @Override
    public void enableLogging(Logger logger) {
        m_logger = logger;
    }
}
