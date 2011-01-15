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
	    <title>Virtual Judge -- Contest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/viewContest.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />
		
		<div class="ptt">
			<s:property value="contest.title" />
		</div>
		
		<div class="plm">
			<b>Start Time:</b> <s:date name="contest.beginTime" format="yyyy-MM-dd HH:mm:ss" />&nbsp;&nbsp;&nbsp;&nbsp;
			<b>End Time:</b> <s:date name="contest.endTime" format="yyyy-MM-dd HH:mm:ss" /><br>
			<b>Contest Type</b>:<s:if test="contest.password == null"><font color="blue"> Public</font></s:if><s:else><font color="red">Priavte</font></s:else>&nbsp;&nbsp;&nbsp;
			<b>Contest Status:</b> 
				<%
					Date date = new Date();
					long now = date.getTime();
					long begin = ((Date)(request.getAttribute("contest.beginTime"))).getTime();
					long end = ((Date)(request.getAttribute("contest.endTime"))).getTime();
					if (now < begin){
						out.println("<font color=\"blue\">Scheduled</font>");
					} else if (now < end) {
						out.println("<font color=\"red\">Running</font>");
					} else {
						out.println("<font color=\"green\">Ended</font>");
					}
				%>&nbsp;&nbsp;&nbsp;
			<b>Manager:</b> <a href="user/profile.action?uid=<s:property value='contest.manager.id' />" ><s:property value="contest.manager.username" /></a>
			<br />
			<font color="orange">Current Server Time : <s:date name="curDate" format="yyyy-MM-dd HH:mm:ss" /></font>
			<br />
		</div>

		<s:if test="dataList != null">
			<br />
			<table style="width:960px" cellpadding="0" cellspacing="0" border="0" class="display" id="viewContest">
				<thead>
					<tr>
						<th>Solved</th>
						<th>ID</th>
						<th>Title</th>
						<th>Ratio(AC/att)</th>
						<th></th>
					</tr>
				</thead>
				
				<s:iterator value="dataList" status="stat">
					<tr>
						<td class="id">
							<s:if test="dataList[#stat.index][0] != null">
								Yes
							</s:if>
						</td>
						<td class="title">
							Problem <s:property value="dataList[#stat.index][1]" />
						</td>
						<td class="title">
							<s:url id="viewProblem" action="viewProblem" namespace="/contest">
								<s:param name="pid" value="dataList[#stat.index][4]" />
							</s:url>
							<a href="${viewProblem}">
								<s:property value="dataList[#stat.index][2]" escape="false" />
							</a>
						</td>
						<td class="center">
							<s:property value="dataList[#stat.index][3]" />
						</td>
						<td>
							<s:url id="toSubmit" action="toSubmit" namespace="/contest">
								<s:param name="pid" value="dataList[#stat.index][4]" />
							</s:url>
							<a href="${toSubmit}">Submit</a>						
						</td>
					</tr>
				</s:iterator>
			</table>
		</s:if>
		
		<div class="description">
			${contest.description}
		</div>
		<s:include value="/bottom.jsp" />
	</body>
</html>
