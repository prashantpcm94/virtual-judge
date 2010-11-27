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
		<script type="text/javascript" src="javascript/jquery.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<div id="topSpace"></div>
		<form id="editorsForm" action="problem/editDescription.action" method="post">
			<table>
				<tr>
					<td>Title:</td>
					<td><s:property value="%{problem.title}" /></td>
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
					<td>Remarks:</td>
					<td><s:textarea id="remarks" name="description.remarks" value="" cols="115" rows="8" /></td>
				</tr>
				
				<tr>
					<td>Description:</td>
					<td><s:textarea id="description" name="description.description" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Input:</td>
					<td><s:textarea id="input" name="description.input" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Output:</td>
					<td><s:textarea id="output" name="description.output" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Sample Input:</td>
					<td><s:textarea id="sampleInput" name="description.sampleInput" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Sample Output:</td>
					<td><s:textarea id="sampleOutput" name="description.sampleOutput" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Hint:</td>
					<td><s:textarea id="hint" name="description.hint" cols="120" rows="15" /></td>
				</tr>
				<tr>
					<td>Source:</td>
					<td>${problem.source}</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<s:hidden name="id" value="%{problem.id}" />
						<input class="bnt1" type="submit" value="Submit" />
						<input class="bnt1" type="button" value="Cancel" onclick="history.go(-1)" />
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
		<script type="text/javascript" src="javascript/editDescription.js"></script>
		<s:include value="/bottom.jsp" />
	</body>

</html>
