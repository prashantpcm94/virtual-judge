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
		<div class="ptt">
			${problem.title}
		</div>
		<div class="plm">
			<table align="center">
				<tr>
					<td>
						<b>Time Limit:</b>
						<s:if test="problem.timeLimit == 0">Unknown</s:if>
						<s:else>${problem.timeLimit}MS</s:else> 
					</td>
					<td width="10px"></td>
					<td>
						<b>Memory Limit:</b>
						<s:if test="problem.memoryLimit == 0">Unknown</s:if>
						<s:else>${problem.memoryLimit}K</s:else>
					</td>
					<td width="10px"></td>
					<td>
						<b>64bit IO Format:</b>
						${problem.originOJ}
					</td>
					<s:if test="#session.visitor.sup == 1">
						<td width="10px"></td>
						<td>
							<b>Creator:</b>
							<a href="user/profile.action?uid=<s:property value='problem.creatorId' />" >
								<s:property value='problem.creatorId' />
							</a>
						</td>
					</s:if>
				</tr>
			</table>
		</div>
		
		<p align="center">
			<font size="3" color="#333399">
				[<a href="contest/toSubmit.action?pid=${pid}">Submit</a>]&nbsp;&nbsp;
				[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
				[<a href="contest/status.action?cid=${cid}&num=${num}">Status</a>]&nbsp;&nbsp; 
			</font>
		</p>
		
		<s:if test="problem.description != null">
			<p class="pst">Description</p>
			<div class="textBG">
				${problem.description}
			</div>	
		</s:if>
			
		<s:if test="problem.input != null">
			<p class="pst">Input</p>
			<div class="textBG">
				${problem.input}
			</div>	
		</s:if>
	
		<s:if test="problem.output != null">
			<p class="pst">Output</p>
			<div class="textBG">
				${problem.output}
			</div>
		</s:if>
	
		<s:if test="problem.sampleInput != null">
			<p class="pst">Sample Input</p>
			<div class="sioBG">
				${problem.sampleInput}
			</div>	
		</s:if>
	
		<s:if test="problem.sampleOutput != null">
			<p class="pst">Sample Output</p>
			<div class="sioBG">
				${problem.sampleOutput}
			</div>	
		</s:if>

		<s:if test="problem.hint != null">
			<p class="pst">Hint</p>
			<div class="textBG">
				${problem.hint}
			</div>	
		</s:if>

		<p align="center">
			<font size="3" color="#333399">
				[<a href="contest/toSubmit.action?pid=${pid}">Submit</a>]&nbsp;&nbsp;
				[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
				[<a href="contest/status.action?cid=${cid}&num=${num}">Status</a>]&nbsp;&nbsp; 
			</font>
		</p>
		<s:include value="/bottom.jsp" />
	</body>
</html>
