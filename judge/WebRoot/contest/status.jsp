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
		<script type="text/javascript" src="javascript/status.js"></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />
		<s:actionerror/>

		<s:form id="filter" action="status" namespace="/contest" method="get">
			Username:<s:textfield name="un" value="%{un}" theme="simple"/>
			&nbsp;&nbsp;Problem Num:<s:textfield name="num" value="%{num}" theme="simple"/>
			<input type="hidden" name="cid" value="${cid}"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Filter"/>
			<br /><br />
		</s:form>

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
				</tr>
			</thead>
			<s:iterator value="dataList" status="stat">
				<tr class="<s:property value='dataList[#stat.index][11]' />">
					<td>
						<s:property value="dataList[#stat.index][0]" />
					</td>
					<td>
						<a href="user/profile.action?uid=<s:property value='dataList[#stat.index][13]' />" >
							<s:property value="dataList[#stat.index][1]" />
						</a>
					</td>
					<td>
						<a href="contest/viewProblem.action?pid=<s:property value="dataList[#stat.index][10]" />" >
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
						<s:if test="dataList[#stat.index][12] == 0 && (#session.visitor == null || #session.visitor.sup == 0 && #session.visitor.id != dataList[#stat.index][13])">
							<s:property value="dataList[#stat.index][6]" />
						</s:if>
						<s:else>
							<s:if test="dataList[#stat.index][12] != 0">
								<a href="contest/viewSource.action?id=<s:property value="dataList[#stat.index][0]" />" style="font-family:Arial,Helvetica,sans-serif;color:green;">
									<s:property value="dataList[#stat.index][6]" />
								</a>
							</s:if>
							<s:else>
								<a href="contest/viewSource.action?id=<s:property value="dataList[#stat.index][0]" />">
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
			</s:iterator>	
		</table>

		<p align="center">
			<font size="3" color="#333399">
				[<a href="contest/status.action?cid=${cid}&un=${un}&num=${num}">Top</a>]&nbsp;&nbsp;
				[<a href="contest/statusPrev.action?cid=${cid}&un=${un}&num=${num}">Prev Page</a>]&nbsp;&nbsp;
				[<a href="contest/statusNext.action?cid=${cid}&un=${un}&num=${num}">Next Page</a>]&nbsp;&nbsp; 
			</font>
		</p>
		<s:include value="/bottom.jsp" />

	</body>
</html>
