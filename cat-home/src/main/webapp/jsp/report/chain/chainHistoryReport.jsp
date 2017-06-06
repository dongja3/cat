<%@ page session="false" language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="w" uri="http://www.unidal.org/web/core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="res" uri="http://www.unidal.org/webres"%>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<jsp:useBean id="ctx" type="com.dianping.cat.report.page.chain.Context" scope="request" />
<jsp:useBean id="payload" type="com.dianping.cat.report.page.chain.Payload" scope="request" />
<jsp:useBean id="model"	type="com.dianping.cat.report.page.chain.Model" scope="request" />
<c:set var="report" value="${model.report}"/>

<a:historyReport title="History Report">
	<jsp:attribute name="subtitle">${w:format(payload.historyStartDate,'yyyy-MM-dd HH:mm:ss')} to ${w:format(payload.historyDisplayEndDate,'yyyy-MM-dd HH:mm:ss')}</jsp:attribute>
<jsp:body>
<res:useJs value="${res.js.local['baseGraph.js']}" target="head-js"/>

<table class="machines">
	<tr class="left">
	<th>
	    Minimal Call Count:
	</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount<=0}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=0&reportType=${payload.reportType}"}" class="current">ALL</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=0&reportType=${payload.reportType}"}">ALL</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==3}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=3&reportType=${payload.reportType}"}" class="current">3</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=3&reportType=${payload.reportType}">3</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==5}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=5&reportType=${payload.reportType}" class="current">5</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=5&reportType=${payload.reportType}">5</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==10}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=10&reportType=${payload.reportType}" class="current">10</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=10&reportType=${payload.reportType}">10</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==20}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=20&reportType=${payload.reportType}" class="current">20</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=20&reportType=${payload.reportType}">20</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==50}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=50&reportType=${payload.reportType}" class="current">50</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=50&reportType=${payload.reportType}">50</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
		<th>
				&nbsp;[<c:choose>
				<c:when test="${payload.minCallCount==100}">
					<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=100&reportType=${payload.reportType}" class="current">100</a>
				</c:when>
				<c:otherwise>
						<a href="?op=history&domain=${model.domain}&date=${model.date}&minCallCount=100&reportType=${payload.reportType}">100</a>
				</c:otherwise>
			</c:choose>]&nbsp;
		</th>
	</tr>
</table>

<c:if test="${report.transactionChains.size()==0}">
	<table class='table table-striped table-condensed table-hover '  style="width:100%;">
		<tr>
			<th class="left"><a href="?op=history&domain=${model.domain}&date=${model.date}&sort=name">Transaction</a></th>
			<th class="right"><a href="?op=history&domain=${model.domain}&date=${model.date}&sort=calCount">Call Count</a></th>
			<th class="right"><a href="?op=history&domain=${model.domain}&date=${model.date}&sort=avg">Avg(ms)</a></th>
		</tr>
		<c:forEach var="chain" items="${report.chains}">
				<tr>
					<td class="left"><a href="?op=history&domain=${model.domain}&date=${model.date}&name=${chain.transactionName}&reportType=${payload.reportType}"}"> ${chain.transactionName}</a></td>
					<td class="right"> ${chain.callCount}</td>
					<td class="right">${w:format(chain.avg,'###,##0.0')}</td>
				</tr>
		 </c:forEach>
	</table>
</c:if>

<c:if test="${report.transactionChains.size()>0}">
	<table class='table table-striped table-condensed table-hover '  style="width:100%;">
		<tr>
		  <th class="left">Level</th>
			<th class="left">Transaction</th>
			<th class="right">Call Count</th>
			<th class="right">Avg(ms)</th>
			<th class="right">Dependency</th>
			<th class="right">Time Cost Ratio</th>
			<th class="right">Mark</th>
		</tr>
		<c:forEach var="chain" items="${report.transactionChains}">
				<tr>
					<td class="left"> ${chain.level}</td>
					<td class="left"> ${chain.transactionName}</td>
					<td class="right"> ${chain.callCount}</td>
					<td class="right">${w:format(chain.avg,'###,##0.0')}</td>
            		<td class="right">${w:format(chain.dependency,'0.00%')}</td>
            		<td class="right">${w:format(chain.timeRatio,'0.00%')}</td>
					<td class="right"> ${chain.remark}</td>
				</tr>
		 </c:forEach>
	</table>
</c:if>

<script type="text/javascript" src="/cat/js/appendHostname.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		appendHostname(${model.ipToHostnameStr});
	});
</script>


<font color="white">${lastIndex}</font>
<res:useJs value="${res.js.local.transaction_js}" target="bottom-js" />
</jsp:body>
</a:historyReport>
