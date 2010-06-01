<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
		<s:form action="login" namespace="/user" theme="simple">
			<table border="0" style="MARGIN-RIGHT:auto;MARGIN-LEFT: auto;">
				<tr>
					<td>Username :</td>
					<td><s:textfield name="username" cssClass="input_login" /> *</td>
				</tr>
				<tr>
					<td>Password :</td>
					<td><s:password name="password" cssClass="input_login" /> *</td>
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
		</s:form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
