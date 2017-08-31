<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
	<%@include file="/jsp/mheader.jsp"%>
    
    <form action="${pageContext.request.contextPath}/manage/editCategory" method="post">
    <table border="1" width="438" align="center">
    	<tr>
    		<th>ID</th>
    		<th>图书类别</th>
    		<th>类别描述</th>
    		<th>操作</th>
    	</tr>
    	<%-- 
    	String id = new String(request.getParameter("id").getBytes("ISO-8859-1"),"utf-8");
    	String name = new String(request.getParameter("name").getBytes("ISO-8859-1"),"utf-8");
    	String description = new String(request.getParameter("description").getBytes("ISO-8859-1"),"utf-8");
    	--%>
    	
    	<%
    		String id = request.getParameter("id");
    		String name = request.getParameter("name");
    		String description = request.getParameter("description");
    	%>
   		<tr>
    		<td><input type="text" name="id" value="<%=id %>" readonly="readonly"></td>
    		<td><input type="text" name="name" value="<%=name %>" /></td>
    		<td><input type="text" name="description" value="<%=description %>" /></td>
    		<td align="center">
    			<input type="submit" value="确定"/>
    		</td>
    	</tr>

    </table>
    </form>
  </body>
</html>
