<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- User</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<table border="1" style="MARGIN-RIGHT:auto;MARGIN-LEFT:auto;">
			<tr>
				<td width="100">Username:</td>
				<td width="300"><s:property value="user.username"/></td>
			</tr>	
			<tr>
				<td>Nickname:</td>
				<td><s:property value="user.nickname"/></td>
			</tr>
			<tr>
				<td>School:</td>
				<td><s:property value="user.school"/></td>
			</tr>
			<tr>
				<td>QQ:</td>
				<td><s:property value="user.qq"/></td>
			</tr>
			<tr>
				<td>Email:</td>
				<td><s:property value="user.email"/></td>
			</tr>
			<tr>
				<td class="form_title">Blog:</td>
				<td height="100	"><s:property value="user.blog"/></td>
			</tr>
		</table>
		<s:include value="/bottom.jsp" />
	</body>
</html>
