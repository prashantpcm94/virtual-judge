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
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/standing.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />

		<div class="ptt">Contest Standing -- <s:property value="contest.title" /></div>

		<table cellpadding="0" cellspacing="1" border="0" class="display" id="standing">
			<thead>
				<tr>
					<th>Rank</th>
					<th>ID</th>
					<th>Solve</th>
					<th class="penalty">Penalty</th>
					<s:iterator value="tList" status="stat">
						<th><a href="contest/viewProblem.action?pid=${id}"><s:property value="num" /></a></th>
					</s:iterator>
				</tr>
			</thead>

			<s:iterator value="dataList" status="stat">
				<tr>
					<td><s:property value="#stat.index + 1" /></td>
					<td><a href="user/profile.action?uid=${userId}"><s:property value="handle" /></a></td>
					<td><s:property value="solCnt" /></td>
					<td class="penalty"><s:property value="sPenalty" /></td>
					<s:iterator value="sACtime" status="stat1">
						<s:if test="ACtime[#stat1.index] gt 0">
							<td class="green"><s:property value="sACtime[#stat1.index]" /><s:if test="attempts[#stat1.index] gt 0">(-<s:property value="attempts[#stat1.index]" />)</s:if></td>
						</s:if>
						<s:elseif test="attempts[#stat1.index] == 0">
							<td class="white" />
						</s:elseif>
						<s:else>
							<td class="red">-<s:property value="attempts[#stat1.index]" /></td>
						</s:else>
					</s:iterator>
				</tr>
			</s:iterator>			
		</table>
		<s:include value="/bottom.jsp" />
	</body>
</html>
