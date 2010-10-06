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
	</head>

	<body>
		<s:include value="/top.jsp" />
		<form action="contest/addContest.action">
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
						<s:password name="contest.password" theme="simple" />
						<font color="green">Leave it blank if you want to make it public<font color="green">
					</td>
				</tr>
				<tr>
					<td class="form_title">Problems:</td>
					<td>
						<font color="green">Please enter the problem IDs you want to add to this contest.<br />Use non-digit separators to separator them.<br /></font>
						<s:textarea name="problemList" cols="80" rows="10" theme="simple" />
					</td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<input class="bnt1" type="submit" value="Submit" />
					</td>
				</tr>
			</table>
			<s:actionerror />
		</form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
