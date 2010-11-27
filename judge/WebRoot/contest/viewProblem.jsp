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

		<script type="text/javascript" src="javascript/jquery.js"></script>

	    <script type="text/javascript" src="dwr/interface/judgeService.js"></script>
		<script type='text/javascript' src='dwr/engine.js'></script>
	    <script type='text/javascript' src='dwr/util.js'></script>
	</head>

	<body>
		<s:include value="/contest/top.jsp" />

		<table width="100%"><tr>
		<td id="left_view">
			<div class="ptt">
				<s:if test="contestOver == 1">
					<a href="${problem.url}">${cproblem.num} - ${problem.title}</a>
				</s:if>
				<s:else>
					${cproblem.num} - ${problem.title}
				</s:else>
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
					[<a href="contest/toSubmit.action?pid=${pid}">Submit</a>]&nbsp;&nbsp;
					[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
					[<a href="contest/status.action?cid=${cid}&num=${num}">Status</a>]
				</font>
			</p>
			
			<div class="hiddable" id="vj_description"><p class="pst"><span>expand</span><span>collapse</span>Description</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_input"><p class="pst"><span>expand</span><span>collapse</span>Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_output"><p class="pst"><span>expand</span><span>collapse</span>Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleInput"><p class="pst"><span>expand</span><span>collapse</span>Sample Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleOutput"><p class="pst"><span>expand</span><span>collapse</span>Sample Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_hint"><p class="pst"><span>expand</span><span>collapse</span>Hint</p><div class="textBG"></div></div>

			<p align="center">
				<font size="3" color="#333399">
					[<a href="contest/toSubmit.action?pid=${pid}">Submit</a>]&nbsp;&nbsp;
					[<a href="javascript:history.go(-1)">Go Back</a>]&nbsp;&nbsp;
					[<a href="contest/status.action?cid=${cid}&num=${num}">Status</a>]
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
		<div style="clear:both" />
		<s:include value="/bottom.jsp" />

		<script type="text/javascript" src="javascript/viewProblem.js"></script>
	</body>
	
</html>