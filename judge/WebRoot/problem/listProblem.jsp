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
	    <title>Virtual Judge -- Problem</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/listProblem.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />
		
		<div class="ptt">Problem List</div>
		<br />

		<div class="plm">
			<s:if test="#session.visitor != null">
				<s:form id="addProblem" action="addProblem" namespace="/problem">
					Add a problem:<s:select id="OJId" name="OJId" value="%{OJId}" list="OJList" theme="simple" cssClass="select" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Problem number:<s:textfield id="ProbNum" name="ProbNum" theme="simple" />
					<input type="submit" value="Add"/>
				</s:form>
			</s:if>
			<s:else>
				<a href="user/toLogin.action"><font color="red">Login to add problems!</font></a>
			</s:else>
		</div>
		
		<table cellpadding="0" cellspacing="0" border="0" class="display" id="listProblem">
			<thead>
				<tr>
					<th >ID</th>
					<th>Title</th>
					<th>Source</th>
					<th>Add Time</th>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td colspan="7">Loading data from server</td>
				</tr>
			</tbody>


<!-- 
			<s:iterator value="dataList" status="stat">
				<tr>
					<td class="center">
						<s:property value="dataList[#stat.index][0]" />
					</td>
					<td class="title">
						<a href="problem/viewProblem.action?id=<s:property value='dataList[#stat.index][0]' />">
							<s:property value="dataList[#stat.index][1]" />
						</a>
						<s:if test="dataList[#stat.index][3] == 1"><font color="red">(Hidden)</font></s:if>
					</td>
					<td class="center">
						<s:if test="dataList[#stat.index][3] == 0 || dataList[#stat.index][4] == #session.visitor.id || #session.visitor.sup == 1">
							<a href="<s:property value='dataList[#stat.index][7]' />">
								<s:property value='dataList[#stat.index][5]' />
								<s:property value='dataList[#stat.index][6]' />
							</a>
						</s:if>
					</td>
					<td class="time">
						<s:date name="dataList[#stat.index][2]"	format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td class="opr">
						<s:if test="#session.visitor.sup == 1 || dataList[#stat.index][4] == #session.visitor.id">
							<a href="problem/toEditProblem.action?id=<s:property value='dataList[#stat.index][0]' />">Edit</a>
						</s:if>
					</td>
					<td class="opr">
						<s:if test="#session.visitor.sup == 1 || dataList[#stat.index][4] == #session.visitor.id">
							<a href="javascript:void(0)" onclick="comfirmDeleteProblem(<s:property value='dataList[#stat.index][0]' />)">Delete</a>
						</s:if>
					</td>
					<td class="opr">
						<s:if test="#session.visitor.sup == 1 || dataList[#stat.index][4] == #session.visitor.id">
							<a href="problem/toggleAccess.action?id=<s:property value='dataList[#stat.index][0]' />">
								<s:if test="dataList[#stat.index][3] == 1">Reveal</s:if>
								<s:else>Hide</s:else>
							</a>
						</s:if>
					</td>
				</tr>
			</s:iterator>	
 -->

		</table>
		<s:include value="/bottom.jsp" />
	</body>
</html>
