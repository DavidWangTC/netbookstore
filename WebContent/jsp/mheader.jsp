<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>网上书店后台管理</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css">
 	<script type="text/javascript" src="${pageContext.request.contextPath}/js/util.js"></script>
  </head>
  
  <body>
    <br/>
    <br/>
    <h1>后台管理</h1>
    <br/>
    <br/>
    <c:if test="${sessionScope.manager==null}">
    	<a href="${pageContext.request.contextPath}/jsp/mlogin.jsp">请登录</a>
    </c:if>
    <c:if test="${sessionScope.manager!=null}">
    	欢迎您:${sessionScope.manager.name}
    <a href="${pageContext.request.contextPath}/manage/managerLogout">注销</a>
    </c:if>
    &nbsp;&nbsp;
    <a href="${pageContext.request.contextPath}/manage/addCategory">添加分类</a>
    <a href="${pageContext.request.contextPath}/manage/showPageCategorys">展示分类</a>
    <a href="${pageContext.request.contextPath}/manage/addBookUI">添加图书</a>
    <a href="${pageContext.request.contextPath}/manage/showPageBooks">展示图书</a>
    <br/>
  <br/>