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
		<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
		<style id="styles" type="text/css">
			#editorsForm {
				height: 600px;
				overflow: auto;
				border: 0;
				margin: 0;
				padding: 0;
			}
		</style>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<div id="topSpace"></div>
		<form id="editorsForm" action="problem/editProblem.action">
			<table>
				<tr>
					<td>Title:</td>
					<td><s:textfield name="problem.title" value="%{problem.title}" size="130" theme="simple" /></td>
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
					<td><s:textarea id="description" name="problem.description" value="%{problem.description}" cols="120" rows="15" theme="simple" /></td>
				</tr>
				<tr>
					<td>Input:</td>
					<td><s:textarea id="input" name="problem.input" value="%{problem.input}" cols="120" rows="15" theme="simple" /></td>
				</tr>
				<tr>
					<td>Output:</td>
					<td><s:textarea id="output" name="problem.output" value="%{problem.output}" cols="120" rows="15" theme="simple" /></td>
				</tr>
				<tr>
					<td>Sample Input:</td>
					<td><s:textarea id="sampleInput" name="problem.sampleInput" value="%{problem.sampleInput}" cols="120" rows="15" theme="simple" /></td>
				</tr>
				<tr>
					<td>Sample Output:</td>
					<td><s:textarea id="sampleOutput" name="problem.sampleOutput" value="%{problem.sampleOutput}" cols="120" rows="15" theme="simple" /></td>
				</tr>
				<tr>
					<td>Hint:</td>
					<td><s:textarea id="hint" name="problem.hint" value="%{problem.hint}" cols="120" rows="15" theme="simple" /></td>
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
		</form>
		<div id="bottomSpace"></div>

		<script type="text/javascript">
		//<![CDATA[
			CKEDITOR.replace( 'description',
				{
					sharedSpaces :
					{
						top : 'topSpace',
						bottom : 'bottomSpace'
					},
				} );
	
			CKEDITOR.replace( 'input',
					{
						sharedSpaces :
						{
							top : 'topSpace',
							bottom : 'bottomSpace'
						},
						removePlugins : 'maximize,resize'
					} );
	
			CKEDITOR.replace( 'output',
					{
						sharedSpaces :
						{
							top : 'topSpace',
							bottom : 'bottomSpace'
						},
						removePlugins : 'maximize,resize'
					} );
			CKEDITOR.replace( 'sampleInput',
					{
						sharedSpaces :
						{
							top : 'topSpace',
							bottom : 'bottomSpace'
						},
						removePlugins : 'maximize,resize'
					} );
	
			CKEDITOR.replace( 'sampleOutput',
					{
						sharedSpaces :
						{
							top : 'topSpace',
							bottom : 'bottomSpace'
						},
						removePlugins : 'maximize,resize'
					} );
					
			CKEDITOR.replace( 'hint',
					{
						sharedSpaces :
						{
							top : 'topSpace',
							bottom : 'bottomSpace'
						},
						removePlugins : 'maximize,resize'
					} );
		//]]>
		</script>
	</body>

</html>
