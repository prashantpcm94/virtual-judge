<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="problem">
	<table width="100%">
		<thead><tr><th>
			<span style="text-align:left;font-family:Verdana;color:green">Problem: <s:select id="problem_number" name="problem_number" list="%{numList4Problem}" /></span>
			<span style="text-align:right;float:right">
				<input type="button" value="Practice" id="practice" />
				<input type="button" value="Origin OJ" id="originOJ" />
				<input type="button" value="Submit" id="submit" />
			</span>
		</th></tr></thead>

		<tbody><tr><td>
			<div class="ptt" id="contest_title"></div>
			<div class="plm">
				<span id="crawling" style="display:none" class="crawlInfo">
					<b><font color="green">Crawling in process...</font></b>
				</span>
				<span id="crawlFailed" style="display:none" class="crawlInfo">
					<b><font color="red">Crawling failed</font></b>
				</span>
				<span id="crawlSuccess" style="display:none" class="crawlInfo">
					<b>Time Limit:</b><span id="timeLimit"></span>&nbsp;&nbsp;
					<b>Memory Limit:</b><span id="memoryLimit"></span>&nbsp;&nbsp;
					<b>64bit IO Format:</b><span id="longlongFormat"></span>
				</span>
			</div>
			<div class="hiddable" id="vj_description"><p class="pst">Description</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_input"><p class="pst">Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_output"><p class="pst">Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleInput"><p class="pst">Sample Input</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_sampleOutput"><p class="pst">Sample Output</p><div class="textBG"></div></div>
			<div class="hiddable" id="vj_hint"><p class="pst">Hint</p><div class="textBG"></div></div>
		</td></tr></tbody>
	</table>
	
</div>