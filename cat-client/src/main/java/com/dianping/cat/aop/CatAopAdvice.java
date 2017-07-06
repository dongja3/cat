package com.dianping.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.eclipselink.CalSqlTransactionContext;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


public class CatAopAdvice {
    private String txnType="method";
    /**
     * 切面注解方式埋点 实现以切面方式快速对系统进行埋点:
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
     <aop:aspect ref="catAopAdvice">
     <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
     <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
     </aop:aspect>
     </aop:config>
     */
    public void aroundMethod(ProceedingJoinPoint pjp) {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();

        Transaction t = Cat.newTransaction("method", getMethodName(method) );
        try {
            pjp.proceed();
            t.setStatus(Transaction.SUCCESS);
        } catch (Throwable e) {
            t.setStatus(e);
            Cat.logError(e);
        } finally {
            t.complete();
        }
    }

    /**
     * 监控EclipseLink SQL 通过AOP 获得SQL 的Call Method
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
     <aop:aspect ref="catAopAdvice">
     <aop:pointcutid="catAroundSqlCallMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
     <aop:around pointcut-ref="catAroundSqlCallMethod" method="aroundSqlCallMethod()"/>
     </aop:aspect>
     </aop:config>
     * */
    public void aroundSqlCallMethod(ProceedingJoinPoint pjp) {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        Transaction t = Cat.newTransaction("DbFacade", getMethodName(method));
        CalSqlTransactionContext.setCallMethod(getMethodName(method));
        try {
            pjp.proceed();
            t.setStatus(Transaction.SUCCESS);
        } catch (Throwable e) {
            t.setStatus(e);
            Cat.logError(e);
        } finally {
            t.complete();
        }
    }

    private String getMethodName(Method method){
        return method.getDeclaringClass().getSimpleName() +"."+ method.getName();
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

}
