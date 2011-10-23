<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="rank" style="padding:0;min-height:300px">
	<div id="status_processing" class="processing" style="display:none">Processing...</div>
	<table cellpadding="0" cellspacing="1" class="display standing" id="rank_table">
		<thead>
			<tr style="background-color:#F2FCF7">
				<th class="rank">Rank</th>
				<th class="id">ID</th>
				<th class="solve">Solve</th>
				<th class="standing_time">Penalty</th>
				<s:iterator value="numList" status="stat">
					<th class="standing_time"><a href="contest/view.action?cid=${cid}#problem/${key}">${key}</a></th>
				</s:iterator>
				<th style="text-align:right"><input type="button" id="rank_setting" value="Setting" style="font-size:12px"></th>
			</tr>
		</thead>
		<tbody id="rank_tbody"></tbody>
		<tfoot id="rank_tfoot"></tfoot>
	</table>

	<div id="div_rank_tool" style="text-align:right;position:fixed;bottom:80px;right:15px;z-index:999999">
		<img src="images/find_me.png" id="img_find_me" title="Find me" style="visibility:hidden;cursor: pointer" />
		<img src="images/go_top.png" id="img_go_top" title="Go to top" height="60" style="visibility:hidden;cursor: pointer" />
	</div>
	
	<div id="dialog-form-rank-setting" style="display:none" title="Rank Setting"></div>
	
</div>
