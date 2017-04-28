<%@ page session="false" language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="res" uri="http://www.unidal.org/webres"%>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<jsp:useBean id="ctx" type="com.dianping.cat.report.page.chain.Context" scope="request" />
<jsp:useBean id="model"	type="com.dianping.cat.report.page.chain.Model" scope="request" />
<c:set var="report" value="${model.report}"/>

<a:report title="Chain Report">

<jsp:body>
<res:useJs value="${res.js.local['baseGraph.js']}" target="head-js"/>

<table class="groups">
	<c:forEach var="chain" items="${report.chains.values()}">
			<tr class="left">
            		<th> ${chain.transactionName}</th>
            		<th> ${chain.callCount}</th>
            		<th> ${chain.avg}</th>
            		<th> ${chain.dependency}</th>
            		<th> ${chain.timeRatio}</th>
			</tr>
	 </c:forEach>
</table>
<script type="text/javascript" src="/cat/js/appendHostname.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		appendHostname(${model.ipToHostnameStr});
	});
</script>


<font color="white">${lastIndex}</font>
<res:useJs value="${res.js.local.transaction_js}" target="bottom-js" />
</jsp:body>
</a:report>
