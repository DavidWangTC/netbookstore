<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@include file="/jsp/mheader.jsp"%>
    
    <form enctype="multipart/form-data" action="${pageContext.request.contextPath}/manage/addBookSubmit" method="post">
    	<table border="1" width="438" align="center">
    		<tr>
    			<td>*书名：</td>
    			<td>
    				<input type="text" name="name" />
    			</td>
    		</tr>
    		<tr>
    			<td>作者:</td>
    			<td>
    				<input type="text" name="author"/>
    			</td>
    		</tr>
    		<tr>
    			<td>*价格:</td>
    			<td>
    				<input type="text" name="price"/>
    			</td>
    		</tr>
    		<tr>
    			<td>图片:</td>
    			<td>
    				<input type="file" name="file"/>
    			</td>
    		</tr>
    		<tr>
    			<td>描述：</td>
    			<td>
    				<textarea rows="3" cols="38" name="description"></textarea>
    			</td>
    		</tr>
    		<tr>
    			<td>*所属分类：</td>
    			<td>
    				<select name="categoryId">
    					<c:forEach items="${cs}" var="c">
    						<option value="${c.id}">${c.name}</option>
    					</c:forEach>
    				</select>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="2">
    				<input type="submit" value="保存"/>
    			</td>
    		</tr>
    	</table>
    </form>
  </body>
</html>
