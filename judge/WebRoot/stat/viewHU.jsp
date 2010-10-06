<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Statistics</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	</head>

	<body>
		<s:include value="/top.jsp" />
		<table border="1" style="MARGIN-RIGHT:auto;MARGIN-LEFT:auto;">
			<tr>
				<td width="100">sessionId:</td>
				<td width="600"><s:property value="vlog.sessionId"/></td>
			</tr>	
			<tr>
				<td>IP:</td>
				<td>
					<a href="http://www.ip138.com/ips.asp?ip=<s:property value="vlog.ip"/>">
						<s:property value="vlog.ip"/>
					</a>
				</td>
			</tr>
			<tr>
				<td>User name:</td>
				<td>
					<a href="user/profile.action?uid=<s:property value="vlog.loginer"/>">
						<s:property value="username"/>
					</a>
				</td>
			</tr>
			<tr>
				<td>create Time:</td>
				<td><s:date name="vlog.createTime" format="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<tr>
				<td>Stay Time:</td>
				<td><s:property value="vlog.duration"/></td>
			</tr>
			<tr>
				<td>Referer:</td>
				<td>
					<a href="${vlog.referer}" >
						<s:property value="vlog.referer"/>
					</a>
				</td>
			</tr>
			<tr>
				<td>User Agent:</td>
				<td><s:property value="vlog.userAgent"/></td>
			</tr>
		</table>
		
		<br /><a href="stat/listOL.action">OnlineUsers</a>
		<br /><a href="stat/listHU.action">HistoryUsers</a>
		<br /><a href="stat/toListBrowser.action">Browser</a>
		<br /><a href="stat/toListOS.action">OS</a>
		<br /><a href="stat/toShowStayTime.action">Stay Time</a>
		<br /><a href="stat/toShowDayVisits.action">30Days visits</a>
		
		
		<s:include value="/bottom.jsp" />
	</body>
</html>
