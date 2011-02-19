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
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='javascript/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/problem_status.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror/>
		
		<form id="form_status">
			Contest:<input type="checkbox" name="inContest" checked="true" />
			Username:<input type="text" name="un" value="${un}" />&nbsp;&nbsp;
			OJ:<s:select id="OJId" name="OJId" value="%{OJId}" list="OJListAll" />&nbsp;&nbsp;
			Problem ID:<s:textfield name="probNum" />&nbsp;&nbsp;
			Result:<s:select name="res" list="#{'0':'All','1':'Accepted','2':'Wrong Answer','3':'Time Limit Exceed','4':'Runtime Error','5':'Presentation Error','6':'Compile Error','7':'Processing'}" />&nbsp;&nbsp;
			<input type="submit" value="Filter"/>&nbsp;&nbsp;
			<input type="button" value="Reset" id="reset" />
		</form>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="status" style="text-align:center">
			<thead>
				<tr>
					<th>RunID</th>
					<th>User</th>
					<th>Problem</th>
					<th>Result</th>
					<th style="text-align:right;padding:3px">Memory</th>
					<th style="text-align:right;padding:3px">Time</th>
					<th>Language</th>
					<th style="text-align:right;padding:3px">Length</th>
					<th>Submit Time</th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="14">Loading data from server</td>
				</tr>
			</tbody>
		</table>

		<s:include value="/bottom.jsp" />
	</body>

</html>
