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
	    <title>Virtual Judge</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/global.css" />
	</head>

	<body>
		<br /><br /><br /><hr>
		<center>
			<p>
				All Copyright Reserved ©2010 <a href="http://acm.hust.edu.cn">HUST ACM/ICPC</a> TEAM<br>
				Anything about the OJ, Please Contact Author:<a href="mailto:is.un@qq.com">Isun</a><br>
			</p>
		</center>
	</body>
</html>
