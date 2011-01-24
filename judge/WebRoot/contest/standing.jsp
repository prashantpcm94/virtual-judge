<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<script type="text/javascript" src="javascript/standing.js"></script>

<table cellpadding="0" cellspacing="1" border="0" class="display standing" id="standing1">
	<thead>
		<tr>
			<th class="rank">Rank</th>
			<th class="id">ID</th>
			<th class="solve">Solve</th>
			<th class="standing_time">Penalty</th>
			<s:iterator value="tList" status="stat">
				<th><a href="contest/viewProblem.action?pid=${id}"><s:property value="num" /></a></th>
			</s:iterator>
			<th />
		</tr>
	</thead>

	<s:iterator value="dataList" status="stat">
		<tr>
			<td><s:property value="#stat.index + 1" /></td>
			<td><a href="user/profile.action?uid=${userId}"><s:property value="handle" /></a></td>
			<td><s:property value="solCnt" /></td>
			<td><s:property value="sPenalty" /></td>
			<s:iterator value="sACtime" status="stat1">
				<s:if test="ACtime[#stat1.index] gt 0">
					<td class="green standing_time"><s:property value="sACtime[#stat1.index]" /><s:if test="attempts[#stat1.index] gt 0">(-<s:property value="attempts[#stat1.index]" />)</s:if></td>
				</s:if>
				<s:elseif test="attempts[#stat1.index] == 0">
					<td class="white standing_time" />
				</s:elseif>
				<s:else>
					<td class="red standing_time">-<s:property value="attempts[#stat1.index]" /></td>
				</s:else>
			</s:iterator>
			<td class="white" />
		</tr>
	</s:iterator>			
</table>
