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
		<s:include value="/contest/top.jsp" />
		<div style="width:800px;MARGIN-RIGHT:auto;MARGIN-LEFT:auto;">
			<form action="contest/submit.action" method="post">
				<table>
					<tr>
						<td>Problem:</td>
						<td><s:property value="%{cproblem.num}"/></td>
					</tr>
					<tr>
						<td>Language:</td>
						<td><s:select id="language" name="language" value="%{language}" listKey="key" listValue="value" list="languageList" theme="simple" cssClass="select" /></td>
					</tr>
					<tr>
						<td>Public:</td>
						<td>
							<s:radio name="isOpen" list="#{'0':'No', '1':'Yes'}" value="%{isOpen}" theme="simple"></s:radio>
						</td>
					</tr>
				</table>
				<s:textarea name="source" rows="25" cols="100" />
				<br />
				<input type="hidden" value="${pid}" name="pid" />
				<input style="margin-left:20px;float:right" class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
				<input style="float:right" class="bnt1" type="submit" value="Submit" />
				<div id="errorMsg" style="color:red;font-weight:bold;float:right"><s:actionerror /></div>
			</form>
		</div>
		<div style="clear:both" />
		<s:include value="/bottom.jsp" />
	</body>
</html>
