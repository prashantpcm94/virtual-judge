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
		<script type="text/javascript" src="javascript/jquery-1.5.min.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />

		<div class="ptt">
			<s:property value="contest.title" />
		</div>
		
		<table style="margin:auto" class="plm">
			<tr>
				<td class="alignRight"><b>Current Time: </b></td>
				<td class="alignLeft"><span class="currentTime" /></td>
				<td class="alignRight"><b>Contest Type: </b></td>
				<td class="alignLeft"><s:if test="contest.password == null"><font color="blue"> Public</font></s:if><s:else><font color="red">Private</font></s:else></td>
			</tr>
			<tr>
				<td class="alignRight"><b>Start Time: </b></td>
				<td class="alignLeft"><s:date name="contest.beginTime" format="yyyy-MM-dd HH:mm:ss" /></td>
				<td class="alignRight"><b>Contest Status: </b></td>
				<td class="alignLeft">
					<s:if test="curDate.compareTo(contest.beginTime) < 0"><font color="blue">Scheduled</font></s:if>
					<s:elseif test="curDate.compareTo(contest.endTime) < 0"><font color="red">Running</font></s:elseif>
					<s:else><font color="green">Ended</font></s:else>
				</td>
			</tr>
			<tr>
				<td class="alignRight"><b>End Time: </b></td>
				<td class="alignLeft"><s:date name="contest.endTime" format="yyyy-MM-dd HH:mm:ss" /></td>
				<td class="alignRight"><b>Manager: </b></td>
				<td class="alignLeft"><a href="user/profile.action?uid=<s:property value='contest.manager.id' />" ><s:property value="contest.manager.username" /></a></td>
			</tr>
		</table>
		
		<div class="plm">
			<br />
			<form action="contest/loginContest.action" method="get">
				Password:<s:password name="password" cssClass="input_login" />
				<input type="hidden" name="cid" value="${cid}" />
				<input class="bnt1" type="submit" value="Login" />
			</form>
			<s:actionerror />
		</div>

		<s:include value="/bottom.jsp" />
	</body>
</html>
