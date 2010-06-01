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
	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:form action="editProblem" namespace="/problem">
			<table>
				<tr>
					<td>Title:</td>
					<td><s:textfield name="problem.title" value="%{problem.title}" size="92" theme="simple" /></td>
				</tr>
				<tr>
					<td>Time Limit:</td>
					<td><s:property value="%{problem.timeLimit}" /> MS</td>
				</tr>
				<tr>
					<td>Memory Limit:</td>
					<td><s:property value="%{problem.memoryLimit}" /> KB</td>
				</tr>
				<tr>
					<td>Description:</td>
					<td><s:textarea name="problem.description" value="%{problem.description}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Input:</td>
					<td><s:textarea name="problem.input" value="%{problem.input}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Output:</td>
					<td><s:textarea name="problem.output" value="%{problem.output}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Sample Input:</td>
					<td><s:textarea name="problem.sampleInput" value="%{problem.sampleInput}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Sample Output:</td>
					<td><s:textarea name="problem.sampleOutput" value="%{problem.sampleOutput}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Hint:</td>
					<td><s:textarea name="problem.hint" value="%{problem.hint}" cols="80" rows="10" theme="simple" /></td>
				</tr>
				<tr>
					<td>Source:</td>
					<td>${problem.source}</td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<input type="hidden" value="${id}" name="id" />
						<input class="bnt1" type="submit" value="Submit" />
					</td>
				</tr>
			</table>
			<s:actionerror />
		</s:form>
		<s:include value="/bottom.jsp" />
	</body>
</html>
