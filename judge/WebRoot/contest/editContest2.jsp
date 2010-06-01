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
	    <title>Virtual Judge -- COntest</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:form action="editContest" namespace="/contest">
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
						Day :<s:textfield value="%{d_day}" name="d_day" size="7" theme="simple" />
						Hour :<s:textfield value="%{d_hour}" name="d_hour" size="7" theme="simple" />
						minute:<s:textfield value="%{d_minute}" name="d_minute" size="7" theme="simple" />
						(Attention: you can only delay or keep the end time.)
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="hidden" name="cid" value="${cid}" />
						<input class="bnt1" type="submit" value="Submit" />
						<input class="bnt1" type="button" value="Cancel" onclick="history.go(-1)"/>
					</td>
				</tr>
			</table>
			<s:actionerror />
		</s:form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
