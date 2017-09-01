<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@include file="/jsp/client/header.jsp"%>
    
    <img src="<c:url value='/images/${book.path}/${book.filename}'/>" alt="${book.filename}"/><br/>
    ${book}<br/><br/>
    <a href="${pageContext.request.contextPath}/client/buy?bookId=${book.id}">放入购物车</a>
    <a href="javascript:window.history.back()">返回</a>
  </body>
</html>
