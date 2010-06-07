<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Statistics</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/listOL.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />

		<div class="ptt">OnLine Users</div>
		<br />
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="listOL">
			<thead>
				<tr>
					<th>IP</th>
					<th>Location</th>
					<th>Browser</th>
					<th>OS</th>
					<th>Username</th>
					<th>ArriveTime</th>
				</tr>
			</thead>

			<s:iterator value="dataList" status="stat">
				<tr>
					<td class="id"><s:property value="dataList[#stat.index][0]" /></td>
					<td class="title" style="text-align:center;"><s:property value="dataList[#stat.index][1]" /></td>
					<td class="time"><s:property value="dataList[#stat.index][2]" /></td>
					<td class="time"><s:property value="dataList[#stat.index][3]" /></td>
					<td class="time">
						<a href="user/profile.action?uid=<s:property value='dataList[#stat.index][5]' />">
							<s:property value='dataList[#stat.index][4]' />
						</a>
					</td>
					<td class="time"><s:date name="dataList[#stat.index][6]" format="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</s:iterator>
		</table>

		<a href="stat/listHU.action">HistoryUsers</a>

		<s:include value="/bottom.jsp" />
	</body>
</html>
