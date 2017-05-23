切面注解方式埋点 实现以切面方式快速对系统进行埋点:
默认transaction Type 是method
```
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
          <aop:aspect ref="catAopAdvice">
               <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
               <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
          </aop:aspect>
     </aop:config>
```
如果需要自定义Transaction Type,那么需要重新定义一个CatAopAdviceBean
```
      <bean id="custCatAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" >
          <property name="txnType" value="CustomizedValue"/>
      </bean>
      <aop:config>
           <aop:aspect ref="custCatAopAdvice">
                <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
                <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
           </aop:aspect>
      </aop:config>

```