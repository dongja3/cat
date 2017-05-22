package com.dianping.cat.eclipselink;

import com.dianping.cat.message.Transaction;

/**
 * Created by DONGJA3 on 5/19/2017.
 */
public class CalSqlTransactionContext {
    private  static ThreadLocal<Transaction> sqlTransaction=new ThreadLocal<Transaction>();
    private  static ThreadLocal<String> callMethod = new ThreadLocal<String>();

    protected static void setSqlTransaction(Transaction t){
        sqlTransaction.set(t);
    }

    public  static void setCallMethod(String callMtd){
        callMethod.set(callMtd);
    }

    protected  static Transaction getTransaction(){
        return sqlTransaction.get();
    }

    protected  static String getCallMethod(){
       String callMethodName = callMethod.get();
        if(callMethodName==null){
            return "UNKNOWN";
        }
        return callMethodName;
    }

    protected static void clear(){
        sqlTransaction.remove();
        callMethod.remove();
    }
}
