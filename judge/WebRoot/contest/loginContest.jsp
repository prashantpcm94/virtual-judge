<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Contest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	</head>

	<body>
		<s:include value="/top.jsp" />

		<div class="ptt">
			<s:property value="contest.title" />
		</div>
		
		<div class="plm">
			<b>Start Time:</b> <s:date name="contest.beginTime" format="yyyy-MM-dd HH:mm:ss" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<b>End Time:</b> <s:date name="contest.endTime" format="yyyy-MM-dd HH:mm:ss" /><br>
			<b>Contest Type</b>:<s:if test="contest.password == null"><font color="blue"> Public</font></s:if><s:else><font color="red">Priavte</font></s:else>
			&nbsp;&nbsp;&nbsp;
			<b>Contest Status:</b> 
				<%
					Date date = new Date();
					long now = date.getTime();
					long begin = ((Date)(request.getAttribute("contest.beginTime"))).getTime();
					long end = ((Date)(request.getAttribute("contest.endTime"))).getTime();
					if (now < begin){
						out.println("<font color=\"blue\">Scheduled</font>");
					} else if (now < end) {
						out.println("<font color=\"red\">Running</font>");
					} else {
						out.println("<font color=\"green\">Ended</font>");
					}
				%>
			<br />
			<font color="orange">Current Server Time : <s:date name="curDate" format="yyyy-MM-dd HH:mm:ss" /></font>
			<br /><br /><br />

			<s:form action="loginContest" namespace="/contest" theme="simple" method="get">
				Password:<s:password name="password" cssClass="input_login" />
				<input type="hidden" name="cid" value="${cid}" />
				<input class="bnt1" type="submit" value="Login" />
			</s:form>
			<s:actionerror />

		</div>

		<s:include value="/bottom.jsp" />
	</body>
</html>
