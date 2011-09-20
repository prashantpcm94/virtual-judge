<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<s:include value="/header.jsp" />
		<title>Virtual Judge -- Contest</title>
		<style type="text/css">
			.ui-tabs .ui-tabs-panel {padding:15px 0px}
			table#standing th {background-color: #EDFFED;}
			td.blue_border {border:1px solid #A6C9E2;}
		</style>
		<script type="text/javascript" src="javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="javascript/FixedHeader.js"></script>

		<script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='javascript/engine.js'></script>
		<script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/standing2.js"></script>
	</head>

	<body>
		<s:hidden name="cid" />
	
		<s:include value="/contest/top.jsp" />

		<div class="ptt"><s:if test="contest.replayStatus != null"><img height="25" title="Replay" src="images/replay.png"></s:if><s:property value="contest.title" escape="false" /></div>
		
		<div id="tabs">
			<ul>
				<li><a href="#scoreboard">Score Board</a></li>
				<li><a href="#setting">Setting</a></li>
				<li><a href="contest/standing.action?cid=${cid}">Old Version</a></li>
			</ul>
			<div id="scoreboard">
				<div id="time_container" style="height:40px;">
					<div id="time_index" style="text-align:right">
						<span></span>
					</div>
					<div id="time_controller"></div>
				</div>
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

				<div class="find_me" style="display:none" id="toolFindMe">
					<a id="aFindMe" title="where am I" class="find_me1">C</a>
				</div>

				<div id="find_me"></div>
				<div id="for_ie" style="display:none;padding-top:30px;">
					<table><tr><td><image src="images/warning.png" height="30" /></td><td>Due to IE's poor performance with Javascript, it's recommended to view this page in Firefox or Chrome.</td></tr></table>
				</div>
			</div>
			<div id="setting">
				<table style="width:95%;border:1px solid #A6C9E2;border-collapse:collapse;margin:20px auto 100px auto;" border="1">
					<tr>
						<td class="blue_border" style="width:200px;vertical-align:top;padding-top:25px;font:15px 'Lucida Grande',Verdana;">Time Machine:<br /><br /><br />(Check them to include their standings to your score board)</td>
						<td class="blue_border">
							<table id="time_machine" class="display" cellpadding="0" cellspacing="0" border="0">
								<thead>
									<tr>
										<th style="text-align:left;padding-left:3px"><s:checkbox id="checkAll" name="checkAll" /></th>
										<th></th>
										<th>Title</th>
										<th>Begin Time</th>
										<th>Length</th>
										<th>Manager</th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="sameContests" status="stat">
									<tr class="<s:property value='sameContests[#stat.index][7]' />">
										<td><s:checkbox fieldValue="%{sameContests[#stat.index][0]}" name="ids" /></td>
										<td><s:if test="sameContests[#stat.index][1] != null"><img height="20" title="Replay" src="images/replay.png"></s:if></td>
										<td><div style="white-space:normal;word-break:break-all;word-wrap:break-word;width:400px;">
											<s:if test="sameContests[#stat.index][0] == cid || contest.endTime.compareTo(curDate) > 0">
												<s:property value="sameContests[#stat.index][2]" escape="false" />
											</s:if>
											<s:else>
												<a href="contest/viewContest.action?cid=<s:property value='sameContests[#stat.index][0]' escape="false" />" target="_blank"><s:property value="sameContests[#stat.index][2]" escape="false" /></a>
											</s:else>
										</div></td>
										<td class="date"><s:date name="sameContests[#stat.index][3]" format="yyyy-MM-dd HH:mm:ss" /></td>
										<td class="date"><s:property value='sameContests[#stat.index][4]' /></td>
										<td class="center"><a href="user/profile.action?uid=<s:property value='sameContests[#stat.index][6]' />"><s:property value="sameContests[#stat.index][5]" /></a></td>
									</tr>
									</s:iterator>	
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<td class="blue_border" style="padding:8px;font:15px 'Lucida Grande',Verdana;">Time Format:</td>
						<td class="blue_border" style="">
							<s:radio name="penaltyFormat" list="%{#{'0':'hh:mm:ss','1':'mm'}}" onclick="this.blur()" />
						</td>
					</tr>
					<tr>
						<td class="blue_border" style="padding:8px;font:15px 'Lucida Grande',Verdana;">Team Name:</td>
						<td class="blue_border" style="">
							<s:radio name="showNick" list="%{#{'0':'Username','1':'Nickname'}}" onclick="this.blur()" />
						</td>
					</tr>
					<tr>
						<td class="blue_border" style="padding:8px;font:15px 'Lucida Grande',Verdana;">Auto Refresh Period (s):</td>
						<td class="blue_border" style="padding:8px;">
							<s:textfield name="autoRefreshPeriod" /> (At least 10s. 0 means no auto refresh)
						</td>
					</tr>
				</table>
			</div>
		</div>

		<s:include value="/bottom.jsp" />
	</body>
</html>
