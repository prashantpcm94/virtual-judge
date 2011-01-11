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
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/standing2.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />

		<div class="ptt">Contest Standing -- <s:property value="contest.title" /></div>

		<table cellpadding="0" cellspacing="1" border="0" class="display" id="standing">
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
		</table>
		
		<s:hidden name="sameContests" />
		
		<s:include value="/bottom.jsp" />
	</body>
</html>
