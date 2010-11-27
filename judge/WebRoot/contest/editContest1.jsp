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
	
		<script type="text/javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<form id="form" action="contest/editContest.action" method="post">
			<table>
				<tr>
					<td class="form_title">Title:</td>
					<td><s:textfield name="contest.title" value="%{contest.title}" size="92" theme="simple" /></td>
				</tr>
				<tr>
					<td class="form_title">Description:</td>
					<td><s:textarea name="contest.description" value="%{contest.description}" cols="80" rows="5" theme="simple" /></td>
				</tr>
				<tr>
					<td class="form_title">Begin Time:</td>
					<td>
						Year:<s:textfield value="%{year}" name="year" size="7" theme="simple" />
						Month:<s:textfield value="%{month}" name="month" size="7" theme="simple" />
						&nbsp;&nbsp;&nbsp;Day:<s:textfield value="%{date}" name="date" size="7" theme="simple" />
						Hour:<s:textfield value="%{hour}" name="hour" size="7" theme="simple" />
						Minute:<s:textfield value="%{minute}" name="minute" size="7" theme="simple" />
					</td>
				</tr>
				<tr>
					<td class="form_title">Duration:</td>
					<td>
						Day :<s:textfield value="%{d_day}" name="d_day" size="7" theme="simple" />
						Hour :<s:textfield value="%{d_hour}" name="d_hour" size="7" theme="simple" />
						minute:<s:textfield value="%{d_minute}" name="d_minute" size="7" theme="simple" />
					</td>
				</tr>
				<tr>
					<td class="form_title">Password:</td>
					<td>
						<s:password name="contest.password" value="%{contest.password}" theme="simple" />
						<font color="green">Leave it blank if you want to make it public<font color="green">
					</td>
				</tr>
				<tr>
					<td class="form_title">Problems:</td>
					<td>
						<table id="addTable">
						<s:iterator value="OJs" status="stat">	
							<tr>
								<s:hidden name="pids" value="%{pids[#stat.index]}" />
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" value="%{OJs[#stat.index]}" list="OJList" /></td>
								<td><s:textfield name="probNums" value="%{probNums[#stat.index]}" /></td>
								<td></td>
								<td></td>
							</tr>
						</s:iterator>
						
							<tr id="addRow" style="display:none">
								<s:hidden name="pids" />
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" list="OJList" /></td>
								<td><s:textfield name="probNums" /></td>
								<td></td>
								<td></td>
							</tr>
						</table>
						<input type="button" id="addBtn" value="Add" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" name="cid" value="${cid}" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Reset" onclick="document.forms[0].reset();location.reload();" />
						<input style="float:right" class="bnt1" type="submit" value="Submit" />
						<div id="errorMsg" style="color:red;font-weight:bold;float:right"><s:actionerror /></div>
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript" src="javascript/editContest.js"></script>
		<s:include value="/bottom.jsp" />
	</body>
</html>
