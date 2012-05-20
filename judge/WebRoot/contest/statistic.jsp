<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<s:include value="/header.jsp" />
		<title><s:property value="contest.title" escape="false" /> - Virtual Judge</title>
		<style type="text/css" media="screen">
			table#statistic_table td {border:1px solid #A6C9E2;width:50px;height:10px;text-align:center;}
			table#statistic_table {border-collapse:collapse;}
		</style>

	</head>

	<body>
		<s:include value="/top.jsp" />
		<form action="contest/statistic.action" method="post">
			Contest Ids (you can use any separator):<br />
			<s:textarea cols="60" rows="10" name="cids" />
			<input type="submit" value="Show" />
		</form>
		<table id="statistic_table" cellpadding="0" cellspacing="0">
			<tr>
				<td></td>
				<s:iterator value="contestIds" status="stat">
				<td>
					<a target="_blank" href="contest/view.action?cid=<s:property value='contestIds[#stat.index]' />#rank">
						<s:property value="contestIds[#stat.index]" />
					</a>
				</td>
				</s:iterator>
				<td>Total</td>
			</tr>
		<s:iterator value="statisticRank" status="rowstatus_out">
			<tr>
				<s:iterator value="statisticRank[#rowstatus_out.index]" status="rowstatus_in">
				<td>
					<s:if test="#rowstatus_in.index == 0">
						<s:property value="statisticRank[#rowstatus_out.index][#rowstatus_in.index]" />
					</s:if>
					<s:else>
						<a href="javascript:void(0)" title='<s:property value="statisticRank[#rowstatus_out.index][#rowstatus_in.index]" />'>
							<s:property value="statisticRank[#rowstatus_out.index][#rowstatus_in.index].size()" />
						</a>
					</s:else>
				</td>
				</s:iterator>
			</tr>
		</s:iterator>
		</table>

		<s:include value="/bottom.jsp" />
	</body>
</html>
