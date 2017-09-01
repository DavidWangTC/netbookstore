<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@include file="/jsp/client/header.jsp"%>
     <a href="${pageContext.request.contextPath}/client/home">所有分类：</a>
     <c:forEach items="${cs}" var="c">
     	<a href="${pageContext.request.contextPath}/client/showPageBooksByCategory?categoryId=${c.id}">${c.name}</a>
     </c:forEach>
     <br/>
     <br/>
     <table border="0" width="838" align="center">
    	<tr>
    		<c:forEach items="${page.records }" var="b">
    			<td align="center">
    				<img width="83" height="118" src="${pageContext.request.contextPath}/images/${b.path}/${b.filename}" alt="${b.filename}"/><br/>
    				书名：${b.name}<br/>
    				作者：${b.author}<br/>
    				单价：￥${b.price }<br/>
    				<a href="${pageContext.request.contextPath}/client/showBookDetail?bookId=${b.id}">去看看</a>
    			</td>
    		</c:forEach>
    	</tr>
    	<tr>
    		<td align="center" colspan="3">
    			<%@include file="/jsp/client/mpage.jsp"%>
    		</td>
    	</tr>
    </table>
    
  </body>
</html>
