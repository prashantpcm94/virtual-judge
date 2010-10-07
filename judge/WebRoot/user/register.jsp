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
	</head>

	<body>
		<s:include value="/top.jsp" />
		<form action="user/register.action" method="post">
			<table border="0" style="MARGIN-RIGHT:auto;MARGIN-LEFT: auto;">
				<tr>
					<td>Username:</td>
					<td><s:textfield name="username" size="50"/> *</td>
				</tr>	
				<tr>
					<td>Password:</td>
					<td><s:password name="password" size="50"/> *</td>
				</tr>
				<tr>
					<td>Repeat:</td>
					<td><s:password name="repassword" size="50"/> *</td>
				</tr>
				<tr>
					<td>Nickname:</td>
					<td><s:textfield name="nickname" size="50" /></td>
				</tr>
				<tr>
					<td>School:</td>
					<td><s:textfield name="school" size="50" /></td>
				</tr>
				<tr>
					<td>QQ:</td>
					<td><s:textfield name="qq" size="50" /></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><s:textfield name="email" size="50" /></td>
				</tr>
				<tr>
					<td class="form_title">Blog:</td>
					<td><s:textarea name="blog" rows="5" cols="41" /></td>
				</tr>
				<tr>
					<td class="form_title">Share code<br />by default:</td>
					<td><s:radio name="share" list="#{'0':'No', '1':'Yes'}" value="1" theme="simple" /></td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" value="${redir}" name="redir" />
						<input class="bnt1" type="submit" value="Submit" />
					</td>
				</tr>
			</table>
			<center><s:actionerror /></center>
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
