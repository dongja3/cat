package com.dianping.cat.aop;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 <bean id="catAopAroundAdvice" class="com.dianping.cat.aop.CatAopAroundAdvice" />
 <aop:config>
 <aop:aspectref="catAopAroundAdvice">
 <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
 <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
 </aop:aspect>
 </aop:config>
 */
public class CatAopAroundAdvice {
    public void aroundMethod(ProceedingJoinPoint pjp) {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        Transaction t = Cat.newTransaction("method", method.getName());
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
}
