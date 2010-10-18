<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Problem</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" src="javascript/jquery.dataTables.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror/>
		
		<form action="problem/status.action" method="get">
			Username:<input type="text" name="un" />&nbsp;&nbsp;
			Problem ID:<input type="text" name="id" />&nbsp;&nbsp;
			Result:<s:select name="res" list="#{'0':'All','1':'Accepted','2':'Wrong Answer','3':'Time Limit Exceed','4':'Runtime Error','5':'Presentation Error','6':'Compile Error','7':'Judge Error'}" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="filter" value="Filter"/>
		</form>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="status" style="text-align:center">
			<thead>
				<tr>
					<th>Run ID</th>
					<th>User</th>
					<th>Problem</th>
					<th>Result</th>
					<th>Memory</th>
					<th>Time</th>
					<th>Language</th>
					<th>Code Length</th>
					<th>Submit Time</th>
					<th></th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="9">Loading data from server</td>
				</tr>
			</tbody>
		</table>

<!-- 
			<s:iterator value="dataList" status="stat">
				<s:if test="dataList[#stat.index][13] == 0 || #session.visitor.sup == 1 || #session.visitor.id == dataList[#stat.index][11]">
					<tr class="<s:property value='dataList[#stat.index][10]' />">
						<td>
							<s:property value="dataList[#stat.index][0]" />
						</td>
						<td>
							<a href="user/profile.action?uid=<s:property value='dataList[#stat.index][11]' />" >
								<s:property value="dataList[#stat.index][1]" />
							</a>
						</td>
						<td>
							<a href="problem/viewProblem.action?id=<s:property value="dataList[#stat.index][2]" />" >
								<s:property value="dataList[#stat.index][2]" />
							</a>	
						</td>
						<td style="font-family:Arial,Helvetica,sans-serif;font-weight:bold;">
							<s:property value="dataList[#stat.index][3]" />
						</td>
						<td>
							<s:if test="dataList[#stat.index][3] == 'Accepted'">
								<s:property value="dataList[#stat.index][4]" /> KB
							</s:if>
						</td>
						<td>
							<s:if test="dataList[#stat.index][3] == 'Accepted'">
								<s:property value="dataList[#stat.index][5]" /> ms
							</s:if>
						</td>
						<td>
							<s:if test="dataList[#stat.index][12] == 0 && (#session.visitor == null || #session.visitor.sup == 0 && #session.visitor.id != dataList[#stat.index][11])">
								<s:property value="dataList[#stat.index][6]" />
							</s:if>
							<s:else>
								<s:if test="dataList[#stat.index][12] != 0">
									<a href="problem/viewSource.action?id=<s:property value="dataList[#stat.index][0]" />" style="font-family:Arial,Helvetica,sans-serif;color:green;">
										<s:property value="dataList[#stat.index][6]" />
									</a>	
								</s:if>
								<s:else>
									<a href="problem/viewSource.action?id=<s:property value="dataList[#stat.index][0]" />">
										<s:property value="dataList[#stat.index][6]" />
									</a>
								</s:else>
							</s:else>
						</td>
						<td>
							<s:property value="dataList[#stat.index][7]" /> B
						</td>
						<td>
							<s:date name="dataList[#stat.index][8]"	format="yyyy-MM-dd HH:mm:ss" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr class="pending">
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
						<td><font color="red">hidden</font></td>
					</tr>
				</s:else>	
			</s:iterator>	


		
		<p align="center">
			<font size="3" color="#333399">
				[<a href="problem/status.action?id=${id}&un=${un}">Top</a>]&nbsp;&nbsp;
				[<a href="problem/statusPrev.action?id=${id}&un=${un}">Prev Page</a>]&nbsp;&nbsp;
				[<a href="problem/statusNext.action?id=${id}&un=${un}">Next Page</a>]&nbsp;&nbsp; 
			</font>
		</p>
 -->
		<s:include value="/bottom.jsp" />
		<script type="text/javascript" src="javascript/status.js"></script>
	</body>

</html>
