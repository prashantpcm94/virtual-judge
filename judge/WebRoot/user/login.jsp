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
		<form action="user/login.action" method="post">
			<table border="0" style="MARGIN-RIGHT:auto;MARGIN-LEFT: auto;">
				<tr>
					<td>Username :</td>
					<td><input type="text" name="username" class="input_login" /> *</td>
				</tr>
				<tr>
					<td>Password :</td>
					<td><input type="password" name="password" class="input_login" /> *</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" value="${redir}" name="redir" />
						<input class="bnt1" type="submit" value="Login" />
					</td>
				</tr>
			</table>
			<center><s:actionerror /></center>
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
