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
		<script type="text/javascript" src="javascript/viewContest.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />
		
		<div class="ptt">
			<s:if test="contest.replayStatus != null"><img height="25" title="Replay" src="images/replay.png"></s:if>
			<s:property value="contest.title" escape="false" />
		</div>
		
		<table style="margin:auto" class="plm">
			<tr>
				<td class="alignRight"><b>Current Time: </b></td>
				<td class="alignLeft"><span class="currentTime" /></td>
				<td class="alignRight"><b>Contest Type: </b></td>
				<td class="alignLeft"><s:if test="contest.password == null"><font color="blue"> Public</font></s:if><s:else><font color="red">Private</font></s:else></td>
			</tr>
			<tr>
				<td class="alignRight"><b>Start Time: </b></td>
				<td class="alignLeft"><span class="plainDate">${beginTime}</span></td>
				<td class="alignRight"><b>Contest Status: </b></td>
				<td class="alignLeft">
					<s:if test="curDate.compareTo(contest.beginTime) < 0"><font color="blue">Scheduled</font></s:if>
					<s:elseif test="curDate.compareTo(contest.endTime) < 0"><font color="red">Running</font></s:elseif>
					<s:else><font color="green">Ended</font></s:else>
				</td>
			</tr>
			<tr>
				<td class="alignRight"><b>End Time: </b></td>
				<td class="alignLeft"><span class="plainDate">${endTime}</span></td>
				<td class="alignRight"><b>Manager: </b></td>
				<td class="alignLeft"><a href="user/profile.action?uid=<s:property value='contest.manager.id' />" ><s:property value="contest.manager.username" /></a></td>
			</tr>
		</table>

		<s:if test="dataList != null">
			<br />
				<div style="text-align:center;padding:10px">
					<s:if test="curDate.compareTo(contest.endTime) > 0">
						[<a href="contest/toAddContest.action?cid=${cid}" title="Create a contest using the same problems, in which you can see the original score board.">Clone this contest</a>]
					</s:if>
					<s:if test="#session.visitor.sup == 1 || #session.visitor.id == contest.manager.id">
						[<a href="contest/toEditContest.action?cid=${cid}">Edit</a>]
						[<a href='javascript:void(0)' onclick='comfirmDeleteContest(${cid})'>Delete</a>]
					</s:if>
				</div>
			<table style="width:960px" cellpadding="0" cellspacing="0" border="0" class="display" id="viewContest">
				<thead>
					<tr>
						<th>Solved</th>
						<th>ID</th>
						<th></th>
						<th style="text-align: left">Title</th>
						<th><s:if test="contest.replayStatus == null">Ratio(AC/att)</s:if></th>
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
						<td class="title center">
							Problem <s:property value="dataList[#stat.index][1]" />
						</td>
						<td>
							<s:if test="curDate.compareTo(contest.endTime) > 0 || #session.visitor.sup == 1 || #session.visitor.id == contest.manager.id">
								<a href="<s:property value="dataList[#stat.index][7]" />"><s:property value="dataList[#stat.index][5]" /> <s:property value="dataList[#stat.index][6]" /></a>
							</s:if>
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
							<s:if test="contest.replayStatus == null"><s:property value="dataList[#stat.index][3]" /></s:if>
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
