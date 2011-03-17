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
	
		<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui-1.8.9.custom.css" />
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
		<script type="text/javascript" src="javascript/jquery-ui-1.8.9.custom.min.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='javascript/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/editContest.js"></script>
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
						<s:textfield name="_beginTime" size="10" readonly="true" />
						<s:textfield name="hour" size="2" maxlength="2" cssClass="clk_select" />:<s:textfield name="minute" size="2" maxlength="2" cssClass="clk_select" />:00
					</td>
				</tr>
				<tr>
					<td class="form_title">Duration:</td>
					<td>
						Day:<s:textfield name="d_day" size="5" cssClass="clk_select" /> <s:textfield name="d_hour" size="2" maxlength="2" cssClass="clk_select" />:<s:textfield name="d_minute" size="2" maxlength="2" cssClass="clk_select" />:00
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
							<tr style="height: 40px">
								<td width="30"><a id="addBtn" href="javascript:void(0)"><img height="18" src="images/ico_add.png" border="0"/></a></td>
								<td>OJ</td>
								<td>ProbNum</td>
								<td>Alias</td>
								<td></td>
								<td>Title</td>
							</tr>
						<s:iterator value="OJs" status="stat">	
							<tr class="tr_problem">
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" value="%{OJs[#stat.index]}" list="OJList" /><s:hidden name="pids" value="%{pids[#stat.index]}" /></td>
								<td><s:textfield name="probNums" value="%{probNums[#stat.index]}" /></td>
								<td><s:textfield name="titles" value="%{titles[#stat.index]}" /></td>
								<td></td>
								<td></td>
							</tr>
						</s:iterator>
							<tr id="addRow" class="tr_problem" style="display:none">
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" list="OJList" /><s:hidden name="pids" /></td>
								<td><s:textfield name="probNums" /></td>
								<td><s:textfield name="titles" /></td>
								<td></td>
								<td></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" name="cid" value="${cid}" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Reset" onclick="document.forms[0].reset();location.reload();" />
						<input style="float:right" id="submit" class="bnt1" type="submit" value="Submit" />
						<div id="errorMsg" style="color:red;font-weight:bold;float:right"><s:actionerror /></div>
					</td>
				</tr>
			</table>
			<s:hidden name="beginTime" />
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
