<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/jsp/client/header.jsp" %>

<table border="1" align="center" width="538" >
	 <tr>
	   <th>序号</th>
	   <th>订单号</th>
	   <th>总件数</th>
	   <th>总金额</th>
	   <th>状态</th>
	 </tr>
	 <c:forEach items="${orders}" var="o" varStatus="vs">
    		<tr class="${vs.index%2==0?'odd':'even'}">
	 			<td>${vs.count}</td>
	 			<td>${o.ordernum}</td>
	 			<td>${o.quantity}</td>
	 			<td>${o.money}</td>
	 			<td>
		 			<c:if test="${o.status==0}">未付款</c:if>
		 			<c:if test="${o.status==1}">已付款</c:if>
		 			<c:if test="${o.status==2}">已发货</c:if>
		 			<c:if test="${o.status==3}">已收货</c:if>
		 			<c:if test="${o.status==4}">关闭</c:if>
	 			</td>
	 		</tr>
	 </c:forEach>
</table>
<br>
<br>
<br>
<br>
<br>