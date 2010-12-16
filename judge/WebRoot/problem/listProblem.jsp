<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Problem</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<style type="text/css" media="screen">
			@import "css/demo_table_jui.css";
			@import "css/jquery-ui-1.8.4.custom.css";
			
			.dataTables_info { padding-top: 0; }
			.dataTables_paginate { padding-top: 0; }
			.css_right { float: right; }
			#example_wrapper .fg-toolbar { font-size: 0.8em }
			#theme_links span { float: left; padding: 2px 10px; }
		</style>

		<script type="text/javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/listProblem.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />
		
		<div class="ptt">Problem List</div>
		<br />

		<div class="plm">
			<s:if test="#session.visitor != null">
				<form id="addProblem" action="problem/addProblem.action" method="post">
					Add a problem:<s:select id="OJId" name="OJId" list="OJListAll" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Problem number:<s:textfield id="ProbNum1" name="ProbNum1" size="10" />---<s:textfield id="ProbNum2" name="ProbNum2" size="10" />
					<input type="submit" value="Add" id="addBtn"/>
				</form>
			</s:if>
			<s:else>
				OJ:&nbsp;&nbsp;<s:select id="OJId" name="OJId" list="OJListAll" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="user/toLogin.action"><font color="red">Login to add problems!</font></a>
			</s:else>
		</div>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="listProblem">
			<thead>
				<tr>
					<th>OJ</th>
					<th>ProbID</th>
					<th>Title</th>
					<th>Trigger Time</th>
					<th>Source</th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="7">Loading data from server</td>
				</tr>
			</tbody>
		</table>
		
		<s:include value="/bottom.jsp" />
	</body>

</html>
