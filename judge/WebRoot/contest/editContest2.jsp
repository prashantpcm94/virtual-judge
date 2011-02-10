<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- COntest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<form action="contest/editContest.action" method="post">
			<table>
				<tr>
					<td>Title:</td>
					<td><s:property value="%{contest.title}" /></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><s:textarea name="contest.description" value="%{contest.description}" cols="80" rows="5" theme="simple" /></td>
				</tr>
				<tr>
					<td>Begin Time:</td>
					<td>
						<s:date name="%{contest.beginTime}" format="yyyy-MM-dd HH:mm:ss" />
					</td>
				</tr>
				<tr>
					<td>Duration:</td>
					<td>
						Day:<s:textfield name="d_day" size="5" cssClass="clk_select" /> <s:textfield name="d_hour" size="2" maxlength="2" cssClass="clk_select" />:<s:textfield name="d_minute" size="2" maxlength="2" cssClass="clk_select" />:00
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" name="cid" value="${cid}" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
						<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Reset" onclick="location.reload();" />
						<input style="float:right" id="submit" class="bnt1" type="submit" value="Submit" />
						<div id="errorMsg" style="color:red;font-weight:bold;float:right"><s:actionerror /></div>
					</td>
				</tr>
			</table>
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
