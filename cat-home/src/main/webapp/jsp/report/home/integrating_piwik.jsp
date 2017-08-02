<%@ page session="false" language="java" pageEncoding="UTF-8" %>

<dt><h5 class="text-success">1.引入oocl_piwik.js</h5></dt>
<p class="detailContent">默认transaction Type 是method</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik.js"></script>
</xmp>

<dt><h5 class="text-success">2.用户自定义oocl_piwik_config.js，并放在oocl_piwik.js之前</h5></dt>
<p class="detailContent">如果userid存放在cookie中，直接使用cookie名称</p>
<xmp class="well">oocl_piwik_config.js文件配置如下：
oocl_piwik_config = {
	piwik_sites: [
	  {
		piwik_url: 'http://{domain or ip}/piwik',//对应的piwik地址
		url: '',//被监控的网页的地址
		siteId: ,//piwik中网站对应的id
		disable:,//本网站禁用piwik,bool类型,此项可无
		cookieid: ''//userid对应的cookie名
	  }
	],
	ignoreServicePrefix: [],//url中该部分及之前的内容被忽略,此项可无,如：'/piwik/fwk_api/',localhost/piwik/fwk_api/test.html?userid=1,最终只得到test.html
	canIgnorRequest: function (request) {},//配置哪些请求被忽略不追踪,此项可无，input:request,return:true/false
	replaceTitle:function(title){}//配置哪些title需要被替换及替换规则,此项可无,input:title,return:替换后的title
}

</xmp>
<p class="detailContent">如userid不是存在cookie中，请自行编写函数用于获取userid:</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		piwik_url: 'http://{domain or ip}/piwik',//对应的piwik地址
		url: '',//被监控的网站的地址
		siteId: ,//piwik中网站对应的id
		disable:,//本网站禁用piwik,bool类型,此项可无
		cookieid_callback: ''//获取userid的方法名
	  }
	],
	ignoreServicePrefix: [],//url中该部分及之前的内容被忽略,此项可无,如：/piwik/fwk_api/,localhost/piwik/fwk_api/test.html?userid=1,最终只得到test.html
	canIgnorRequest: function (request) {},//配置哪些请求被忽略不追踪,此项可无，input:request,return:true/false
	replaceTitle:function(title){}//配置哪些title需要被替换及替换规则,此项可无,input:title,return:替换后的title
}

</xmp>

<dt><h5 class="text-success">3.PIWIK EXTJS 插件</h5></dt>
<p class="detailContent">使用该插件后将自动监控基于Ext.Ajax的Ajax call;</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik_ext.js"></script>
</xmp>


<dt><h5 class="text-success">4.PIWIK ANGULAR 插件</h5></dt>
<p class="detailContent">使用该插件后将自动监控Ajax Call，在Angular插件中提供了对module绑定拦截器的函数，请对每个需要监控的module加入这段代码：
oocl_piwik_tracker.setInterceptor(module);</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik_angular_1.4.js"></script>
</xmp>



<dt><h5 class="text-success">5.Business Function 监控</h5></dt>
<p class="detailContent">Business function 是按照"/"分组的，用户在Ajax Call 之前调用API可以自定义business function</p>
<xmp class="well">
oocl_piwik_tracker.setupContext('bfname');
</xmp>

<dt><h5 class="text-success">6.完整示例</h5></dt>
<p class="detailContent">按顺序引用对应的js文件</p>
<xmp class="well">
<script src="./piwik/oocl_piwik_config.js"></script>
<script src="http://192.168.0.24/piwik/oocl_piwik.js"></script>
<!--ext 和 angularjs 的插件按需引用-->
<script src="http://192.168.0.24/piwik/oocl_piwik_angular_1.4.js"></script>
<script src="http://192.168.0.24/piwik/oocl_piwik_ext.js"></script>
</xmp>

<p class="detailContent">如果cookie中没有保存userid，oocl_piwik_config.js文件配置如下,piwik_sites数组可对应不同环境如QA,PP,PRD</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		  piwik_url: 'http://192.168.0.24/piwik',
		  url: 'http://qatest.com',
		  siteId:1,
		  disable:false,
		  cookieid_callback: getUserId
	  },
	  {
		  piwik_url: 'http://192.168.0.24/piwik',
		  url: 'http://pptest.com',
		  siteId:2,
		  disable:false,
		  cookieid_callback: getUserId
	  }
	],
	 ignoreServicePrefix: [
	  '/fwk_api/test'
	],
	canIgnorRequest: function (request) {
		return !!request.cache;
	},
	replaceTitle:function(title){
		return title.replace('!/', '');
	}
}
</xmp>
<p class="detailContent">如果cookie中保存了userid，oocl_piwik_config.js文件配置如下,piwik_sites数组可对应不同环境如QA,PP,PRD</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		  piwik_url: 'http://192.168.0.24/piwik',
		  url: 'http://qatest.com',
		  siteId:1,
		  disable:false,
		  cookieid: 'test.sid'
	  },
	  {
		  piwik_url: 'http://192.168.0.24/piwik',
		  url: 'http://test.com',
		  siteId:2,
		  disable:false,
		  cookieid: 'test.sid'
	  }
	],
	 ignoreServicePrefix: [
	  '/fwk_api/test'
	],
	canIgnorRequest: function (request) {
		return !!request.cache;
	},
	replaceTitle:function(title){
		return title.replace('!/', '');
	}
}
</xmp>


