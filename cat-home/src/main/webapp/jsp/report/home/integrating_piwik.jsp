<%@ page session="false" language="java" pageEncoding="UTF-8" %>

<dt><h5 class="text-success">1.Include oocl_piwik.js</h5></dt>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik.js"></script>
</xmp>

<dt><h5 class="text-success">2.Users define oocl_piwik_config.js,and include it before oocl_piwik.js</h5></dt>
<p class="detailContent">If userid is stored in cookie，please use cookie name</p>
<xmp class="well">oocl_piwik_config.js may be like this：
oocl_piwik_config = {
	piwik_sites: [
	  {
		piwik_url: 'http://{domain or ip}/piwik',//url to piwik
		url: '',//your url here,notice that if your site uses https,your piwik server should also support https
		siteId: ,//website id in piwik
		disable:,//forbid piwik on this site,true or false
		cookieid: ''//cookie name for userid
	  }
	],
	ignoreServicePrefix:[],//ignore string in the url,eg.:'/piwik/fwk_api/',localhost/piwik/fwk_api/test.html?userid=1,finally get test.html
	canIgnorRequest: function (request) {},//which kind of request should be ignored,input:request,return:true/false
	replaceTitle:function(title){}//which kind of title should be replaced,input:title,return: the title after replaced
}

</xmp>
<p class="detailContent">If userid is not stored in cookie，please write function to get userid by yourself:</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		piwik_url: 'http://{domain or ip}/piwik',//url to piwik
		url: '',//your url here,notice that if your site uses https,your piwik server should also support https
		siteId: ,//website id in piwik
		disable:,//forbid piwik on this site,true or false
		cookieid: ''//cookie name for userid
	  }
	],
	ignoreServicePrefix:[],//ignore string in the url,eg.:'/piwik/fwk_api/',localhost/piwik/fwk_api/test.html?userid=1,finally get test.html
	canIgnorRequest: function (request) {},//which kind of request should be ignored,input:request,return:true/false
	replaceTitle:function(title){}//which kind of title should be replaced,input:title,return: the title after replaced
}

</xmp>

<dt><h5 class="text-success">3.Piwik Extjs Plugin</h5></dt>
<p class="detailContent">After using this plugin,it will monitor the all ajax call based on Ext.Ajax;</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik_ext.js"></script>
</xmp>


<dt><h5 class="text-success">4.Piwik Angular Plugin</h5></dt>
<p class="detailContent">After using this plugin,it will bind interceptor to module and monitor all ajax call.Please add this code to every module that needs to be monitored:oocl_piwik_tracker.setInterceptor(moduleName);</p>
<xmp class="well">
<script src="http://{domain or ip}/piwik/oocl_piwik_angular_1.4.js"></script>
</xmp>



<dt><h5 class="text-success">5.Business Function Monitor</h5></dt>
<p class="detailContent">Business function is grouped by '/'，user can invoke api to define yourself business function name before ajax call</p>
<xmp class="well">
oocl_piwik_tracker.setupContext('bfname');
</xmp>

<dt><h5 class="text-success">6.Form Monitor</h5></dt>
<p class="detailContent">Piwik can monitor the elements whose tagName is form or the elemants that have an attribute named data-piwik-form.Piwik will monitor the form submit event.If you don`t fire the submit event when submit a form,please add this code to the submit function:</p>
<xmp class="well">
_paq.push(['FormAnalytics::trackFormSubmit', buttonElement]);
</xmp>
<p class="detailContent">please add these code to the callback function that be called when server returns success:</p>
<xmp class="well">
_paq.push(['FormAnalytics::trackFormConversion', buttonElement]);
</xmp>

<dt><h5 class="text-success">7.Completed Example</h5></dt>
<p class="detailContent">include js file in this order</p>
<xmp class="well">
<script src="./piwik/oocl_piwik_config.js"></script>
<script src="http://192.168.0.24/piwik/oocl_piwik.js"></script>
<!--ext and angularjs should be used according to your requirement-->
<script src="http://192.168.0.24/piwik/oocl_piwik_angular_1.4.js"></script>
<script src="http://192.168.0.24/piwik/oocl_piwik_ext.js"></script>
</xmp>

<p class="detailContent">If cookie does`t store userid，oocl_piwik_config.js is as follows,piwik_sites is an array that can be deployed to different environment such as QA,PP,PRD</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		  piwik_url: 'http://testdomain.com/piwik',
		  url: 'http://qatest.com',
		  siteId:1,
		  disable:false,
		  cookieid_callback: getUserId
	  },
	  {
		  piwik_url: 'http://testdomain.com/piwik',
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
<p class="detailContent">If cookie stores userid，oocl_piwik_config.js is as follows,piwik_sites is an array that can be deployed to different environment such as QA,PP,PRD</p>
<xmp class="well">
oocl_piwik_config = {
	piwik_sites: [
      {
		  piwik_url: 'http://testdomain.com/piwik',
		  url: 'http://qatest.com',
		  siteId:1,
		  disable:false,
		  cookieid: 'test.sid'
	  },
	  {
		  piwik_url: 'http://testdomain.com/piwik',
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


