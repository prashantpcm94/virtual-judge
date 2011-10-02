<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<s:include value="/header.jsp" />
		<title>Virtual Judge -- Contest</title>
		<script type="text/javascript" src="javascript/viewContest.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:if test="contest.announcement != null && !contest.announcement.isEmpty()"><marquee id="contest_announcement" height="25" style="text-align:center;color:red;font-weight:bold" onmouseout="this.start()" onmouseover="this.stop()" scrollamount="2" scrolldelay="1" behavior="alternate">${contest.announcement}</marquee><script type="text/javascript">$("table.banner").css("margin-bottom", "5px");if ($.browser.safari)$("#contest_announcement").removeAttr("behavior");</script></s:if>
		
		<div class="ptt">
			<s:if test="contest.replayStatus != null"><img height="25" title="Replay" src="images/replay.png" /></s:if>
			<s:property value="contest.title" escape="false" />
		</div>
		<div id="time_container" style="height:40px;">
			<div id="time_index" style="text-align:right">
				<span></span>
			</div>
			<div id="time_controller"></div>
		</div>
		
		<div id="contest_tabs">
			<ul>
				<li><a href="#overview">Overview</a></li>
				<li><a href="#problem">Problem</a></li>
				<li><a href="#status">Status</a></li>
				<li><a href="#rank">Rank</a></li>
			</ul>
			<s:include value="/contest/div_overview.jsp" />
			<s:include value="/contest/div_problem.jsp" />
			<s:include value="/contest/div_status.jsp" />
			<s:include value="/contest/div_rank.jsp" />
			
			<div id="problem"><s:include value="/contest/problem_div.jsp" /></div>
			<div id="status"><s:include value="/contest/status_div.jsp" /></div>
			<div id="rank"><s:include value="/contest/rank_div.jsp" /></div>
		</div>		
		
		

		<s:include value="/bottom.jsp" />
	</body>
</html>
