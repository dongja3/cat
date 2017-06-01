<%@ page session="false" language="java" pageEncoding="UTF-8" %>

<dt><h5 class="text-success">1.引入oocl_piwik_config.js</h5></dt>
<p class="detailContent">默认transaction Type 是method</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik.js"></script>
</xmp>

<dt><h5 class="text-success">2.APP自定义oocl_piwik_config.js，并放在oocl_piwik.js之前</h5></dt>
<xmp class="well">
oocl_piwik_config ={
    piwik_url: 'http://{domain or ip}/piwik',
    piwik_sites: [
        {
          url: 'https://tmsqa.weikayun.com',
          siteId:1,
          cookieid:'SSO_IRIS4_USERID_COOKIE_DEV'
        },
        {
       url: 'https://tmsqa.weikayun.com',
          siteId:2,
          cookieid:'SSO_IRIS4_USERID_COOKIE_DEV'
        }
    ]
}
</xmp>

<dt><h5 class="text-success">3.PIWIK EXTJS 插件</h5></dt>
<p class="detailContent">使用该插件后将自动监控Ajax Call</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik_ext.js"></script>
</xmp>


<dt><h5 class="text-success">4.Business Function 监控</h5></dt>
<p class="detailContent">Business function 是按照"/"分组的，用户在Ajax Call 之前调用API可以自定义business function</p>
<xmp class="well">
oocl_piwik_tracker.setupContext('search/userids');
</xmp>

