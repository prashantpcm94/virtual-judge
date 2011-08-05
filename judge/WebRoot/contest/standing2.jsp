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
		<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui-1.8.9.custom.css" />
		<style type="text/css">
			.ui-tabs .ui-tabs-panel {padding:15px 0px}
			table#standing th {background-color: #EDFFED;}
		</style>
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="javascript/jquery-ui-1.8.9.custom.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/FixedHeader.js"></script>

		<script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='javascript/engine.js'></script>
		<script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/standing2.js"></script>
	</head>

	<body>
		<s:hidden name="cid" />
	
		<s:include value="/contest/top.jsp" />

		<div class="ptt">Contest Standing -- <s:property value="contest.title" /></div>
		
		<div id="tabs">
			<ul>
				<li><a href="#scoreboard">Score Board</a></li>
				<li><a href="#setting">Time Machine</a></li>
				<li><a href="#penaltyformat">Penalty Format</a></li>
				<li><a href="contest/standing.action?cid=${cid}">Old Version</a></li>
			</ul>
			<div id="scoreboard">
				<table width="100%">
					<tr>
						<td width="18">
							<a href="#" id="refresh"><img src="images/refresh.png" height="18" border="0" /></a>
						</td>
						<td>
							<div id="time_container">
								<div id="time_index" style="text-align:right">
									<span></span>
								</div>
								<div id="time_controller"></div>
							</div>
						</td>
					</tr>
				</table>

				<div id="contestTitle" style="width:960px;font-size:14px;padding-top:15px;margin-left:auto;margin-right:auto;text-align:center;font: 20px 'Lucida Grande',Verdana,Arial,Helvetica,sans-serif;" >　</div>
				<div id="status_processing" class="processing" style="display:none">Processing...</div>
				<table cellpadding="0" cellspacing="1" class="display standing" id="standing">
					<thead>
						<tr>
							<th class="rank">Rank</th>
							<th class="id">ID</th>
							<th class="solve">Solve</th>
							<th class="standing_time">Penalty</th>
							<s:iterator value="tList" status="stat">
								<th class="standing_time"><a href="contest/viewProblem.action?pid=${id}"><s:property value="num" /></a></th>
							</s:iterator>
							<th />
						</tr>
					</thead>
					<tbody id="standing_tbody" />
				</table>
			</div>
			<div id="setting">
				<div style="width:960px;font-size:14px;margin-left:auto;margin-right:auto;">
					<p style="text-align:center;margin-top:5px;font: 20px 'Lucida Grande',Verdana,Arial,Helvetica,sans-serif;">Contests using the same problems:</p>
					<table class="display" cellpadding="0" cellspacing="0" border="0">
						<thead>
							<tr>
								<th style="text-align:left;padding-left:3px"><s:checkbox id="checkAll" name="checkAll" /></th>
								<th>Title</th>
								<th>Begin Time</th>
								<th>Length</th>
								<th>Manager</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="sameContests" status="stat">
							<tr class="<s:property value='sameContests[#stat.index][6]' />">
								<td><s:checkbox fieldValue="%{sameContests[#stat.index][0]}" name="ids" /></td>
								<td>
									<s:if test="sameContests[#stat.index][0] == cid || contest.endTime.compareTo(curDate) > 0">
										<s:property value="sameContests[#stat.index][1]" />
									</s:if>
									<s:else>
										<a href="contest/viewContest.action?cid=<s:property value='sameContests[#stat.index][0]' />" target="_blank"><s:property value="sameContests[#stat.index][1]" /></a>
									</s:else>
								</td>
								<td class="date"><s:date name="sameContests[#stat.index][2]" format="yyyy-MM-dd HH:mm:ss" /></td>
								<td class="date"><s:property value='sameContests[#stat.index][3]' /></td>
								<td class="center"><a href="user/profile.action?uid=<s:property value='sameContests[#stat.index][5]' />"><s:property value="sameContests[#stat.index][4]" /></a></td>
							</tr>
							</s:iterator>	
						</tbody>
					</table>
				</div>
			</div>
			<div id="penaltyformat">
				<div style="width:960px;font-size:14px;margin-left:auto;margin-right:auto;text-align:center;">
					<p style="margin-top:5px;font: 20px 'Lucida Grande',Verdana,Arial,Helvetica,sans-serif;">Penalty time format:</p>
					<s:radio name="penaltyFormat" list="%{#{'0':'hh:mm:ss','1':'mm'}}" onclick="this.blur()" />
				</div>
			</div>
		</div>

		<s:include value="/bottom.jsp" />
	</body>
</html>
