监控EclipseLink SQL 有两种方式

1) 使用AOP监控
     <bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
     <aop:config>
     <aop:aspectref="catAopAdvice">
     <aop:pointcutid="catAroundSqlCallMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
     <aop:around pointcut-ref="catAroundSqlCallMethod" method="aroundSqlCallMethod()"/>
     </aop:aspect>
     </aop:config>

  2) 不使用AOP，用户需要自定义SQL的Call Method,如果用户不自定义，那么所有SQL的Call Method 都是UNKNOWN
  CalSqlTransactionContext.setCallMethod("ClassName.CallMethod");

两种监控方式都需要配置Cat 自定义的EclipseLink Session Event Listener
  META-INF\persistence.xml
  <property name="eclipselink.session-event-listener" value="com.dianping.cat.eclipselink.CatSessionEventAdapter" />

