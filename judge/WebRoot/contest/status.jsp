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
		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
		
		<script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
		<script type='text/javascript' src='dwr/util.js'></script>
		
		<script type="text/javascript" src="javascript/contest_status.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />
		<s:actionerror/>

		<form id="form_status">
			Username:<input type="text" name="un" value="${un}" />&nbsp;&nbsp;
			Problem:<s:select name="num" list="%{numList}" cssStyle="width:90px" />&nbsp;&nbsp;
			Result:<s:select name="res" list="#{'0':'All','1':'Accepted','2':'Wrong Answer','3':'Time Limit Exceed','4':'Runtime Error','5':'Presentation Error','6':'Compile Error','7':'Judge Error'}" />&nbsp;&nbsp;
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
					<th>Memory</th>
					<th>Time</th>
					<th>Language</th>
					<th>Length</th>
					<th>Submit Time</th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="11">Loading data from server</td>
				</tr>
			</tbody>
		</table>

		<s:include value="/bottom.jsp" />
	</body>
</html>
