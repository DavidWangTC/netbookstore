<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@include file="/jsp/mheader.jsp"%>
    
    <table border="1" width="938" align="center">
    	<tr>
    		<th>序号</th>
    		<th>图片</th>
    		<th>书名</th>
    		<th>作者</th>
    		<th>单价</th>
    		<th>描述</th>
    		<th>所属分类</th>
    		<th>操作</th>
    	</tr>
    	<c:forEach items="${page.records}" var="b" varStatus="vs">
    		<tr class="${vs.index%2==0?'odd':'even'}">
	    		<td>${vs.count}</td>
	    		<td>
	    			<img src="${pageContext.request.contextPath}/images/${b.path}/${b.filename}" alt="${b.filename}"/>
	    		</td>
	    		<td>${b.name }</td>
	    		<td>${b.author }</td>
	    		<td>${b.price }</td>
	    		<td>${b.description }</td>
	    		<td>${b.category.name }</td>
	    		<td>
	    			<a href="${pageContext.request.contextPath}/manage/editBook?id=${b.id}">修改</a>
	    			<a href="javascript:deleteBook('${b.id}')">删除</a>
	    		</td>
	    	</tr>
    	</c:forEach>
    	<tr>
    		<td align="center" colspan="8">
    			<%@include file="/jsp/page.jsp"%>
    		</td>
    	</tr>
    </table>
    
    <script type="text/javascript">
    function deleteBook(id){
		var sure = window.confirm("确定要删除该项吗？");
		if(sure){
			window.location.href="${pageContext.request.contextPath}/manage/deleteBook?id="+id;
		}
	}
	</script>
    
    
  </body>
</html>
