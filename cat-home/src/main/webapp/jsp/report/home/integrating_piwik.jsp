<%@ page session="false" language="java" pageEncoding="UTF-8" %>

<dt><h5 class="text-success">1.引入oocl_piwik.js</h5></dt>
<p class="detailContent">默认transaction Type 是method</p>
<xmp class="well">
EXT：<script src="http://101.37.202.78/piwik/oocl_piwik.js"></script>
Angular:<script src="http://dongja3-w7/piwik/oocl_piwik.js"></script>
</xmp>

<dt><h5 class="text-success">2.APP自定义oocl_piwik_config.js，并放在oocl_piwik.js之前</h5></dt>
<xmp class="well">
EXT中oocl_piwik_config.js配置如下：
oocl_piwik_config = {
  piwik_sites: [
    {
      piwik_url: 'http://101.37.202.78/piwik',
      url: 'https://tmsqa.weikayun.com',
      siteId: 1,
      cookieid_callback:callback//function to get userid
    }
  ]
}
因为cookie中没有userid，所以oocl_piwik_config.js中的callback函数请自行编写用于获取userid。

Angular中oocl_piwik_config.js配置如下：
oocl_piwik_config = {
	  piwik_sites: [
      {
	      piwik_url: 'http://dongja3-w7/piwik',
	      url: 'http://zhangte4-w7:3000',
	      siteId: 3,
	      cookieid: 'connect.sid'
	    }
	  ]
	}
</xmp>

<dt><h5 class="text-success">3.PIWIK EXTJS/ANGULAR 插件</h5></dt>
<p class="detailContent">使用该插件后将自动监控Ajax Call</p>
<xmp class="well">
EXT插件：<script src="http://101.37.202.78/piwik/oocl_piwik_ext.js"></script>
Angular插件:<script src="http://dongja3-w7/piwik/oocl_piwik_angular_1.4.js"></script>
</xmp>


<dt><h5 class="text-success">4.Business Function 监控</h5></dt>
<p class="detailContent">Business function 是按照"/"分组的，用户在Ajax Call 之前调用API可以自定义business function</p>
<xmp class="well">
oocl_piwik_tracker.setupContext('search/userids');
</xmp>

