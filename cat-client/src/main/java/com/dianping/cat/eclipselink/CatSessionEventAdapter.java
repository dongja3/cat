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
        Transaction t = SqlTransactionContext.getTransaction();
        if(t!=null){
            SqlTransactionContext.clear();
            m_logger.warn(" SqlTransactionContext  not cleared");
        }
        Session session = event.getSession();
        t = Cat.newTransaction("JpaQuery", SqlTransactionContext.getCallMethod());
        SqlTransactionContext.setSqlTransaction(t);
        if (session instanceof ClientSession) {
            PerformanceProfiler pp = new CatQueryProfiler();
            event.getSession().setProfiler(pp);
            pp.setSession(event.getSession());
        }
    }
    @Override
    public void postReleaseClientSession (SessionEvent event) {
        completeCatTransaction();
    }
    @Override
    public void postRollbackTransaction (SessionEvent event) {
        completeCatTransaction();
    }

    public void postCommitUnitOfWork(SessionEvent event){
        completeCatTransaction();
    }
    private void completeCatTransaction(){
        Transaction t = SqlTransactionContext.getTransaction();
        if(t==null){
            return;
        }
        if(t.isCompleted()){
            SqlTransactionContext.clear();
            return;
        }
        t.setStatus(Transaction.SUCCESS);
        t.complete();
        SqlTransactionContext.clear();
    }


    @Override
    public void enableLogging(Logger logger) {
        m_logger = logger;
    }
}
