<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.util.Date" %>
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
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/listHU.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />

		<div class="ptt">History Users</div>
		<br />
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="listHU">
			<thead>
				<tr>
					<th>IP</th>
					<th>User name</th>
					<th>Arrive Time</th>
					<th>Stay Length</th>
				</tr>
			</thead>

			<s:iterator value="dataList" status="stat">
				<tr>
					<td class="id">
						<a href="stat/viewHU.action?id=<s:property value="dataList[#stat.index][0].id" />">
							<s:property value="dataList[#stat.index][0].ip" />
						</a>	
					</td>
					<td class="time"><s:property value="dataList[#stat.index][1]" /></td>
					<td class="time"><s:date name="dataList[#stat.index][0].createTime" format="yyyy-MM-dd HH:mm:ss" /></td>
					<td class="time"><s:property value="dataList[#stat.index][0].duration" /></td>
				</tr>
			</s:iterator>
		</table>

		<p align="center">
			<font size="3" color="#333399">
				[<a href="stat/listHU.action">Top</a>]&nbsp;&nbsp;
				[<a href="stat/prevHU.action">Prev Page</a>]&nbsp;&nbsp;
				[<a href="stat/nextHU.action">Next Page</a>]&nbsp;&nbsp; 
			</font>
		</p>

		<br /><a href="stat/listOL.action">OnlineUsers</a>
		<br /><a href="stat/listHU.action">HistoryUsers</a>
		<br /><a href="stat/toListBrowser.action">Browser</a>
		<br /><a href="stat/toListOS.action">OS</a>
		<br /><a href="stat/toShowStayTime.action">Stay Time</a>
		<br /><a href="stat/toShowDayVisits.action">30Days visits</a>


		<s:include value="/bottom.jsp" />
	</body>
</html>
