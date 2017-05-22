package com.dianping.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


public class CatAopAdvice {
    /**
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
     <aop:aspectref="catAopAdvice">
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
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
     <aop:aspectref="catAopAdvice">
     <aop:pointcutid="catAroundSqlCallMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
     <aop:around pointcut-ref="catAroundSqlCallMethod" method="aroundSqlCallMethod()"/>
     </aop:aspect>
     </aop:config>
     * */
    public void aroundSqlCallMethod(ProceedingJoinPoint pjp) {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        Transaction t = Cat.newTransaction("JpaQuery", getMethodName(method));
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
}
