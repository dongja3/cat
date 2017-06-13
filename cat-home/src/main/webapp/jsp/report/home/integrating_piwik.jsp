<%@ page session="false" language="java" pageEncoding="UTF-8" %>

<dt><h5 class="text-success">1.引入oocl_piwik.js</h5></dt>
<p class="detailContent">默认transaction Type 是method</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik.js"></script>
</xmp>

<dt><h5 class="text-success">2.用户自定义oocl_piwik_config.js，并放在oocl_piwik.js之前</h5></dt>
<xmp class="well">
oocl_piwik_config.js文件配置如下：
oocl_piwik_config = {
	  piwik_sites: [
      {
	      piwik_url: 'http://{domain or ip}/piwik',//url to piwik
	      url: '',//your url here
	      siteId: ,//website id in piwik
	      cookieid: ''//cookie name to get userid
	  }
	]
}

如userid不是存在cookie中，请自行编写callback函数用于获取userid:
oocl_piwik_config = {
  piwik_sites: [
    {
          piwik_url: 'http://{domain or ip}/piwik',//url to piwik
	      url: '',//your url here
          siteId: ,//website id in piwik
          cookieid_callback:callback//function to get userid
    }
  ]
}
</xmp>

<dt><h5 class="text-success">3.PIWIK EXTJS 插件</h5></dt>
<p class="detailContent">使用该插件后将自动监控Ajax Call;</p>
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
oocl_piwik_tracker.setupContext('search/userids');
</xmp>

