<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@include file="/jsp/mheader.jsp"%>

    <table border="1" width="438" align="center">
    	<tr>
    		<th>序号</th>
    		<th>图书类别</th>
    		<th>类别描述</th>
    		<th>操作</th>
    	</tr>
    	<c:forEach items="${page.records}" var="c" varStatus="vs">
    		<tr class="${vs.index%2==0?'odd':'even'}">
	    		<td>${vs.count}</td>
	    		<td>${c.name}</td>
	    		<td>${c.description}</td>
	    		<td>
	    			<a href="${pageContext.request.contextPath}/jsp/editCategory.jsp?id=${c.id}&name=${c.name}&description=${c.description}">修改</a>
	    			<a href="javascript:del('${c.id}')">删除</a>
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
	function del(id) {
		var sure = window.confirm("确定要删除吗？");
		if(sure){
			window.location.href="${pageContext.request.contextPath}/manage/deleteCategory?id="+id;
		}
	}
	</script>
  </body>
</html>
