<%@ page session="false" language="java" pageEncoding="UTF-8" %>


<dl>
  <dt><h5 class="text-success">1.Web.xml中新增filter</h5></dt>
  <dd>
	<h5 class="text-danger detailContent"><strong>Filter放在url-rewrite-filter 之后的第一个，如果不是会导致URL的个数无限多，比如search/1/2,search/2/3等等，无法监控，后端存储压力也变大。</strong></h5>
	<xmp class="well">
    <filter>
        <filter-name>cat-filter</filter-name>
        <filter-class>com.dianping.cat.servlet.CatFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>cat-filter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
    </filter-mapping>
	</xmp>
  </dd>


<dt><h5 class="text-success">2.切面注解方式埋点 实现以切面方式快速对系统进行埋点</h5></dt>
<p class="detailContent">默认transaction Type 是method</p>
<xmp class="well">
<bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
<aop:config>
    <aop:aspect ref="catAopAdvice">
        <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
        <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
    </aop:aspect>
</aop:config>
</xmp>

<p class="detailContent">如果需要自定义Transaction Type,那么需要重新定义一个CatAopAdviceBean</p>
<xmp class="well">
<bean id="custCatAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" >
    <property name="txnType" value="CustomizedValue"/>
</bean>
<aop:config>
    <aop:aspect ref="custCatAopAdvice">
        <aop:pointcutid="catAroundMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
        <aop:around pointcut-ref="catAroundMethod" method="aroundMethod"/>
    </aop:aspect>
</aop:config>
</xmp>

<dt><h5 class="text-success">3.监控EclipseLink SQL 有两种方式</h5></dt>
 <p class="text-danger">1) 使用AOP监控</p>
<xmp class="well">
<bean id="catAopAdvice" class="com.dianping.cat.aop.CatAopAdvice" />
<aop:config>
    <aop:aspect ref="catAopAdvice">
        <aop:pointcutid="catAroundSqlCallMethod"expression="execution(* com.springinaction.springidol.Performer.perform(..))"/>
        <aop:around pointcut-ref="catAroundSqlCallMethod" method="aroundSqlCallMethod()"/>
    </aop:aspect>
</aop:config>
</xmp>

 <p class="text-danger">2) 不使用AOP，用户需要自定义SQL的Call Method,如果用户不自定义，那么所有SQL的Call Method 都是UNKNOWN</p>
<xmp class="well">
CalSqlTransactionContext.setCallMethod("ClassName.CallMethod");
</xmp>

<h5 class="text-danger detailContent">两种监控方式都需要配置Cat 自定义的EclipseLink Session Event Listener</h5>
 <xmp class="well">
  META-INF\persistence.xml
  <property name="eclipselink.session-event-listener" value="com.dianping.cat.eclipselink.CatSessionEventAdapter" />
</xmp>
  <dt><h5 class="text-success">4.配置domain (优先读取A配置)</h5></dt>
   <p class="text-danger">A) 在资源文件中新建app.properties文件</p>
   <dd><p class="detailContent">在resources资源文件META-INF下，注意是<span class="text-danger">src/main/resources/META-INF/</span>文件夹，
  而不是<span class="text-danger">webapps下的那个META-INF</span>,添加<span class="text-danger">app.properties</span>，加上domain配置，如：<span class="text-danger">app.name=tmsqa</span></p>
  <p class="text-danger"> B) 在资源文件中新建client.xml文件</p>
  <dd><p class="detailContent">在resources资源文件META-INF下，新建cat文件夹，注意是<span class="text-danger">src/main/resources/META-INF/cat/client.xml</span>文件，
  而不是<span class="text-danger">webapps下的那个META-INF</span></p>
  <xmp class="well">
<config mode="client">
    <domain id="tmsqa"/>
</config>
  </xmp>
  </dd>
  <dt><h5 class="text-success">5./data/appdatas/cat/目录下，新建一个client.xml文件</h5></dt>
  <dd>
  <p class="detailContent">如果系统是windows环境，则在eclipse运行的盘，比如D盘，新建/data/appdatas/cat/目录，新建client.xml文件</p>
	
  <p>项目文件中srouce中的client.xml,此文件代表了这个项目我是谁,比如项目的名字Cat。</p>
  <p>/data/appdatas/cat/client.xml</p>
  
  <xmp class="well">
<config mode="client">
    <servers>
        <server ip="10.30.206.170" port="2280" />
    </servers>
</config>
  </xmp>
  <p class="text-danger">alpha、beta这个配置需要自己在此目录添加</p>
  <p class="text-danger">预发以及生产环境这个配置需要通知到对应OP团队，让他们统一添加，自己上线时候做下检查即可</p>
  <p>a、10.30.206.170:2280端口是指向测试环境的cat地址</p>
  <p>b、<span class="text-danger">配置可以加入CAT的开关，用于关闭CAT消息发送,将enabled改为false，如下表示将mobile-api这个项目关闭</span></p>
  <xmp>
  	<config mode="client">
          <servers>
             <server ip="10.30.206.170" port="2280" />
         </servers>
         <domain id="mobile-api" enabled="false"/>
      </config>
  </xmp>
  </dd>
  <dt><h5 class="text-success">6.CAT的Log4j集成 【建议所有Log都打到CAT，这样才能更快发现问题】</h5></dt>
  <p class="detailContent text-danger">业务程序的所有异常都通过记录到CAT中，方便看到业务程序的问题，建议在Root节点中添加次appendar</p>
  <p>a)在Log4j2的xml中，加入CatAppender></p>
  <xmp class="well">
<RollingFile append="true" fileName="$logpath$/cat.log" filePattern="$logpath$/cat.log.%i" name="catAppender">
    <Policies>
        <SizeBasedTriggeringPolicy size="30MB"/>
    </Policies>
    <DefaultRolloverStrategy max="3"/>
    <PatternLayout pattern="%-4r [%t] %-5p %c %x %d{DATE} - %m%n"/>
</RollingFile>
  </xmp>

  <p>b)在Log4j2的xml中，加入CatLogger></p>
   <xmp class="well">
<AsyncLogger additivity="false" level="debug" name="com.dianping.api.location">
    <appender-ref ref="catAppender"/>
</AsyncLogger>
   </xmp>

  <p>c)在Root的节点中加入catAppender</p>
  <xmp class="well">
<Root>
     <appender-ref ref="catAppender" />
</Root>
  </xmp>


