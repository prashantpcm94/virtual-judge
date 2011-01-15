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

		<link rel="stylesheet" type="text/css" href="css/redmond/jquery-ui-1.8.7.custom.css" />
		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="javascript/jquery-ui-1.8.7.custom.min.js"></script>
		
		<script type="text/javascript" src="javascript/common.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/editContest.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<form id="form" action="contest/addContest.action" method="post">
			<table>
				<tr>
					<td class="form_title">Title:</td>
					<td><s:textfield name="contest.title" size="94" theme="simple" /></td>
				</tr>
				<tr>
					<td class="form_title">Description:</td>
					<td><s:textarea name="contest.description" cols="80" rows="5" theme="simple" /></td>
				</tr>
				<tr>
					<td class="form_title">Begin Time:</td>
					<td>
						<s:textfield name="contest.beginTime" size="10" readonly="true" >
							<s:param name="value">
								<s:date name="%{contest.beginTime}" format="yyyy-MM-dd" />
							</s:param>
						</s:textfield>
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
						<s:password name="contest.password" theme="simple" />
						<font color="green">Leave it blank if you want to make it public<font color="green">
					</td>
				</tr>
				<tr>
					<td class="form_title">Problems:</td>
					<td>
						<table id="addTable">
							<tr>
								<td></td>
								<td>OJ</td>
								<td>ProbNum</td>
								<td>Alias</td>
								<td width="24"></td>
								<td>Title</td>
							</tr>
						<s:iterator value="OJs" status="stat">	
							<tr class="tr_problem">
								<s:hidden name="pids" value="%{pids[#stat.index]}" />
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" value="%{OJs[#stat.index]}" list="OJList" /></td>
								<td><s:textfield name="probNums" value="%{probNums[#stat.index]}" /></td>
								<td><s:textfield name="titles" value="%{titles[#stat.index]}" /></td>
								<td></td>
								<td></td>
							</tr>
						</s:iterator>
						
							<tr id="addRow" class="tr_problem" style="display:none">
								<s:hidden name="pids" />
								<td><a class="deleteRow" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a></td>
								<td><s:select name="OJs" list="OJList" /></td>
								<td><s:textfield name="probNums" /></td>
								<td><s:textfield name="titles" /></td>
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
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
						<input style="float:right" class="bnt1" type="submit" value="Submit" />
						<div id="errorMsg" style="color:red;font-weight:bold;float:right"><s:actionerror /></div>
					</td>
				</tr>
			</table>
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
