<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Contest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/listOL.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		
		<div class="ptt">
			Online Users
		</div>
		
		<table id="listOL" cellpadding="0" cellspacing="0" border="0" class="display" style="text-align:center" >
			<thead>
				<tr>
					<th>Session ID</th>
					<th>Username</th>
					<th>IP</th>
					<th>Arrive Time</th>
					<th>Active Length</th>
					<th>Freeze Length</th>
					<th>Browser</th>
					<th>OS</th>
				</tr>
			</thead>

			<s:iterator value="dataList" status="stat">
			<tr>
				<td>
					<a href="stat/viewOL.action?id=<s:property value='dataList[#stat.index][0]' />">
						<s:property value="dataList[#stat.index][0]" />
					</a>
				</td>
				<td>
					<a href="user/profile.action?uid=<s:property value='dataList[#stat.index][2]' />">
						<s:property value='dataList[#stat.index][1]' />
					</a>
				</td>
				<td>
					<a href="http://www.ip138.com/ips.asp?ip=<s:property value="dataList[#stat.index][3]" /> ">
						<s:property value="dataList[#stat.index][3]" />
					</a>
				</td>
				<td><s:date name="dataList[#stat.index][4]" format="yyyy-MM-dd HH:mm:ss" /></td>
				<td><s:property value="dataList[#stat.index][5]" /></td>
				<td><s:property value="dataList[#stat.index][6]" /></td>
				<td><s:property value="dataList[#stat.index][7]" /></td>
				<td><s:property value="dataList[#stat.index][8]" /></td>
			</tr>
		</s:iterator>
		</table>

		<s:include value="/bottom.jsp" />
	</body>
</html>
