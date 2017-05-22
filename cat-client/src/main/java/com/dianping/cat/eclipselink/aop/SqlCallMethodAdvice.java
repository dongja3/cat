package com.dianping.cat.eclipselink.aop;

import com.dianping.cat.eclipselink.SqlTransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Created by DONGJA3 on 5/22/2017.
 <bean id="sqlCallMethodAdvice" class="com.dianping.cat.eclipselink.aop.SqlCallMethodAdvice" />
 <aop:config>
 <aop:aspectref="sqlCallMethodAdvice">
 <aop:pointcutid="sqlCallMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
 <aop:before pointcut-ref="sqlCallMethod" method="beforeSqlCall()"/>
 </aop:aspect>
 </aop:config>
 */
public class SqlCallMethodAdvice {
    public void beforeSqlCall(ProceedingJoinPoint pjp) {
        MethodSignature joinPointObject = (MethodSignature) pjp.getSignature();
        Method method = joinPointObject.getMethod();
        SqlTransactionContext.setCallMethod(method.getName());
    }
}
