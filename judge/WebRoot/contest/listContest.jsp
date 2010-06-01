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
	    <title>Virtual Judge -- Contest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/listContest.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />
		
		<div class="ptt">Contest List</div>
		<div class="plm">
			<font color="orange">Current Server Time : <s:date name="curDate" format="yyyy-MM-dd HH:mm:ss" /></font>
			<br /><br />
			<s:if test="#session.visitor != null">
				<a href="contest/toAddContest.action">Add a contest</a>
			</s:if>
			<s:else>
				<a href="user/toLogin.action"><font color="red">Login to add contests!</font></a>
			</s:else>
			<br /><br />
		</div>
		
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="listContest">
			<thead>
				<tr>
					<th>ID</th>
					<th>Title</th>
					<th>Begin Time</th>
					<th>End Time</th>
					<th>Status</th>
					<th>Type</th>
					<th></th>
					<th></th>
				</tr>
			</thead>

			<s:iterator value="dataList" status="stat">
				<tr>
					<td class="id">
						<s:property value="id" />
					</td>
					<td class="title">
						<a href="contest/viewContest.action?cid=<s:property value='id' />">
							<s:property value="title" />
						</a>
					</td>
					<td class="time">
						<s:date name="beginTime" format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td class="time">
						<s:date name="endTime" format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td class="center status">
						<%
							Date date = new Date();
							long now = date.getTime();
							long begin = ((Date)(request.getAttribute("beginTime"))).getTime();
							long end = ((Date)(request.getAttribute("endTime"))).getTime();
							if (now < begin){
								out.println("<font color=\"blue\">Scheduled</font>");
							} else if (now < end) {
								out.println("<font color=\"red\">Running</font>");
							} else {
								out.println("<font color=\"green\">Ended</font>");
							}
						%>
					</td>
					<td class="center type">
						<s:if test="password == null"><font color="blue">Public</font></s:if>
						<s:else><font color="red">Private</font></s:else>
					</td>

					<td class="opr">
						<s:if test="managerId == #session.visitor.id || #session.visitor.sup == 1">
							<a href="contest/toEditContest.action?cid=<s:property value='id' />">Edit</a>
						</s:if>
					</td>
					<td class="opr">
						<s:if test="managerId == #session.visitor.id || #session.visitor.sup == 1">
							<a href="javascript:void(0)" onclick="comfirmDeleteContest('${id}')">Delete</a>
						</s:if>
					</td>
				</tr>
			</s:iterator>
		</table>
		<s:include value="/bottom.jsp" />
	</body>
</html>
