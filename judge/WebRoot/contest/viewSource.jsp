<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String langFile = "shjs/lang/" + request.getAttribute("language") + ".min.js";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<s:include value="/header.jsp" />
		<title>Source Code - Virtual Judge</title>
		<script type="text/javascript" src="shjs/sh_main.min.js" ></script>
		<script type="text/javascript" src="<%=langFile%>" ></script>
		<link type="text/css" rel="stylesheet" href="shjs/css/sh_style.min.css" />

		<script type="text/javascript" src="javascript/viewSource.js"></script>
	</head>

	<body onload="sh_highlightDocument();">
		<s:include value="/top.jsp" />
		<div class="ptt" style="color:black;font-weight:normal;margin-bottom:12px"><a href="user/profile.action?uid=${uid}">${submission.username}</a> 's source code for <a href="contest/view.action?cid=${contest.id}#problem/${cproblem.num}">${cproblem.num}</a></div>
		
		<div class="plm" style="text-align:left">
			<table align="center" style="font-size:10pt">
				<tr>
					<td>
						<b>Memory: </b>${submission.memory} KB
					</td>
					<td width=10px></td>
					<td>
						<b>Time: </b>${submission.time} MS
					</td>
				</tr>
				<tr>
					<td>
						<b>Language: </b>${submission.dispLanguage}
					</td>
					<td width=10px></td>
					<td>
						<b>Result: </b>
						<font color=blue>${submission.status}</font>
					</td>
				</tr>
				<s:if test="#session.visitor.id == uid || #session.visitor.sup != 0">
					<tr>
						<td>
							<b>Public: </b>
						</td>
						<td colspan="2">
							<s:radio name="open" list="#{'0':'No', '1':'Yes'}" value="%{submission.isOpen}" onclick="this.blur()" ></s:radio>
						</td>
					</tr>
				</s:if>
			</table>
			<s:hidden name="sid" value="%{submission.id}" />
		</div>
		<p id="info" style="text-align:center;font-size:15pt;color:green;visibility:hidden">This source is shared by <b>${submission.username}</b></p>
		<pre class="${language}" style="font-family:Courier New,Courier,monospace">${submission.source}</pre>
		<s:include value="/bottom.jsp" />
	</body>
</html>
