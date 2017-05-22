切面注解方式埋点 实现以切面方式快速对系统进行埋点:

     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAroundAdvice" />
     <aop:config>
     <aop:aspectref="catAopAdvice">
     <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
     <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
     </aop:aspect>
     </aop:config>

