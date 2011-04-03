<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<% String basePath = (String)application.getAttribute("basePath"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Contest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
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
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.cookie.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>

		<script type="text/javascript" src="javascript/listContest.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />
		
		<div class="head_status" style="float:right;display:none">
			<span class="Scheduled">Scheduled:</span><input type="checkbox" name="scheduled" checked="checked" value="1" />  
			<span class="Running">&nbsp;&nbsp;Running:</span><input type="checkbox" name="running" checked="checked" value="2" />  
			<span class="Ended">&nbsp;&nbsp;Ended:</span><input type="checkbox" name="ended" checked="checked" value="3" />  
		</div>
		
		<div class="ptt">Contest List</div>
		<div class="plm">
			<br />
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
					<th>Length</th>
					<th>Status</th>
					<th>Type</th>
					<th>Manager</th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="9">Loading data from server</td>
				</tr>
			</tbody>
		</table>

		<s:include value="/bottom.jsp" />
	</body>
</html>
