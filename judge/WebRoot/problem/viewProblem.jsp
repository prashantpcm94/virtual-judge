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

		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>

		<script type="text/javascript" src="javascript/viewProblem.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		
		<table width="100%"><tr>
		<td id="left_view">
			<div class="ptt">
				<a href="${problem.url}">${problem.title}</a>
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
							${_64Format}
						</td>
					</tr>
				</table>
			</div>
	
			<p align="center">
				<font size="3" color="#333399">
					[<a href="problem/toSubmit.action">Submit</a>]&nbsp;&nbsp;
					[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
					[<a href="problem/status.action?id=${problem.id}">Status</a>]&nbsp;&nbsp; 
				</font>
			</p>
			
			<div class="hiddable" id="vj_description"><p class="pst">Description</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_input"><p class="pst">Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_output"><p class="pst">Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleInput"><p class="pst">Sample Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleOutput"><p class="pst">Sample Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_hint"><p class="pst">Hint</p><div class="textBG"></div></div>
			<s:if test="problem.source != null"><p class="pst">Source</p><div class="textBG">${problem.source}</div></s:if>

			<p align="center">
				<font size="3" color="#333399">
					[<a href="problem/toSubmit.action">Submit</a>]&nbsp;&nbsp;
					[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
					[<a href="problem/status.action?id=${problem.id}">Status</a>]&nbsp;&nbsp; 
				</font>
			</p>
		</td>
		
		<td id="right_view">
			<s:iterator value="problem.descriptions" status="stat">
				<div class="desc_info">
					<div id="info<s:property value="#stat.index" />">
						<b><s:if test="#stat.index eq 0"><font color="red">System Crawler</font></s:if><s:else><s:property value="author" /></s:else></b>
						<s:date name="updateTime" format="yyyy-MM-dd" />
						<span class="hiddable opt">
							<s:hidden name="vote" />
							<s:if test="#session.votePids.contains(problem.id)">
								<span style="float:right"><img height="18" src="images/thumb_up.png" border="0"/><span>${vote}</span></span>
							</s:if>
							<s:else>
								<span style="float:right" ><a class="vote" id="vote_${id}" href="javascript:void(0)" style="text-decoration:none"><img height="18" src="images/thumb_up.png" border="0"/>${vote}</a></span>
								<span style="float:right;display:none"><img height="18" src="images/thumb_up.png" border="0"/><span>${vote}</span></span>
							</s:else>
							<a style="padding-right:10px;float:right" href="problem/toEditDescription.action?id=${id}"><img height="18" src="images/ico_edit.gif" border="0"/></a>
							<s:if test="#session.visitor.sup == 1 || #session.visitor.username == author">
								<a style="padding-right:10px;float:right" id="del_${id}" class="delete_desc" href="javascript:void(0)"><img height="18" src="images/ico_delete.gif" border="0"/></a>
							</s:if>
						</span>
					</div>
					<div class="hiddable remark">
						<img height="20" src="images/icon_quote_s.gif" /><s:property value="remarks" /><img height="20" src="images/icon_quote_e.gif" />
					</div>
				</div>
			</s:iterator>
		</td>
		
		</tr></table>

		<s:hidden name="pid" value="%{problem.id}" />
		<s:include value="/bottom.jsp" />
	</body>
	
</html>
