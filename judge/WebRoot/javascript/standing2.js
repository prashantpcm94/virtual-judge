var cid, onlyCid, data, ti, pnum, maxTime, changed, standingTable, oFH, autoRefresh;

jQuery.fn.dataTableExt.oSort['rank-asc']  = function(x, y) {
	var v1 = parseInt(x), v2 = parseInt(y);
	if (isNaN(v1)) {
		return 1;
	} else if (isNaN(v2)) {
		return -1;
	} else {
		return v1 < v2 ? -1 : 1;
	}
};

jQuery.fn.dataTableExt.oSort['rank-desc'] = function(x, y) {
	var v1 = parseInt(x), v2 = parseInt(y);
	if (isNaN(v1)) {
		return 1;
	} else if (isNaN(v2)) {
		return -1;
	} else {
		return v1 > v2 ? -1 : 1;
	}
};

var standingTableSetting = {
	"bPaginate": false,
	"bLengthChange": false,
	"bFilter": true,
	"bInfo": false,
	"bAutoWidth": false,
	"aoColumns": [{"sType":"rank", "bSortable":false}, {"sType":"string", "bSortable":false}, {"sType":"numeric", "bSortable":false}, {"sType":"date", "bSortable":false}]
};

$(document).ready(function() {
	
	cid = $("[name=cid]").val();
	pnum = $("#standing th").length - 5;
	
	if ($.cookie("contest_" + cid) == undefined){
		$.cookie("contest_" + cid, cid, { expires: 3 });
	}
	if ($.cookie("penalty_format") == undefined){
		$.cookie("penalty_format", 0, { expires: 30 });
	}
	if ($.cookie("show_nick") == undefined){
		$.cookie("show_nick", 0, { expires: 30 });
	}
	if ($.cookie("auto_refresh_period") == undefined){
		$.cookie("auto_refresh_period", 0, { expires: 30 });
	}
	$("input[name=autoRefreshPeriod]").click(function(){
		this.select();
	});

	standingTable = $('#standing').dataTable(standingTableSetting);

	$( "#tabs" ).tabs({
		show: function(event, ui) {
			if (ui.index == 0){
				onlyCid = 0;
				$("div#contestTitle").text("　");
				getRemoteData();
				setTimeout(function(){
					if (!oFH) {
						oFH = new FixedHeader( standingTable );
					} else {
						oFH.fnUpdate();
					}
				}, 100);
			}
		},
		select: function(event, ui) {
			if (ui.index != 0) {
				clearTimeout(autoRefresh);
				$("div.FixedHeader_Cloned").hide();
			} else {
				$("div.FixedHeader_Cloned").show();
			}
		},
		cookie: { expires: 30 }
	});

	$( "#tabs" ).removeClass("ui-widget-content");
	$( "#tabs" ).addClass("ui-widget-content-custom");

	$('#setting table.display').dataTable({
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false
	});

	$("[name=ids]").click(function(){
		var ids = "";
		$("[name=ids]:checked").each(function(){
			ids += "/" + $(this).val();
		});
		$.cookie("contest_" + cid, ids.substr(1), { expires: 3 });
		updateCheckAll();
	});
	
	$("#checkAll").click(function(){
		$("[name=ids]").attr("checked", $(this).attr("checked"));
		var ids = "";
		$("[name=ids]:checked").each(function(){
			ids += "/" + $(this).val();
		});
		$.cookie("contest_" + cid, ids.substr(1), { expires: 3 });
	});

	$("[name=penaltyFormat]").change(function(){
		$.cookie("penalty_format", $(this).val(), { expires: 30 });
	});
	$("[name=showNick]").change(function(){
		$.cookie("show_nick", $(this).val(), { expires: 30 });
	});

	$("[name=autoRefreshPeriod]").blur(function(){
		var period = /^\d{1,5}$/.test($(this).val()) ? parseInt($(this).val(), 10) : 0;
		if (period < 10) {
			period = 0;
		}
		$(this).val(period);
		$.cookie("auto_refresh_period", period, { expires: 30 });
	});

	$("td.meta_td").live("mouseover", function(){
		var curCid = $(this).parent().attr("cid");
		$("tr[cid=" + curCid + "] td.meta_td").addClass("same_td");
	}).live("mouseout", function(){
		var curCid = $(this).parent().attr("cid");
		$("tr[cid=" + curCid + "] td.meta_td").removeClass("same_td");
	}).live("click", function(){
		var curCid = $(this).parent().attr("cid");
		if (onlyCid){
			onlyCid = 0;
			if (changed){
				calcScoreBoard();
			} else {
				$("tr.disp").show();
			}
			$("div#contestTitle").text("　");
		} else {
			onlyCid = curCid;
			changed = false;
			$("tr.disp[cid!=" + curCid + "]").hide();
			$("div#contestTitle").html($("input[type=checkbox][value=" + curCid + "]").parent().next().html());
		}
	});
	
	$("tr.disp").live("mouseover", function(){
		$(this).css("background-color", "#CCEEFF")
	}).live("mouseout", function(){
		$(this).css("background-color", "transparent");
	});

	var ids = $.cookie("contest_" + cid);
	if (!!ids) {
		ids = ids.split("/");
		for (var i = 0; i < ids.length; i++){
			$("input[type=checkbox][value=" + ids[i] + "]").attr("checked",'true');
		}
	}

	$("[name=penaltyFormat]").get($.cookie("penalty_format")).checked = 1;
	$("[name=showNick]").get($.cookie("show_nick")).checked = 1;
	$("[name=autoRefreshPeriod]").val($.cookie("auto_refresh_period"));

	updateCheckAll();
});

function getRemoteData(){
	$("#status_processing").show();
	var ids = $.cookie("contest_" + cid) || "";

	DWREngine.setAsync(false);
	judgeService.getContestTimeInfo(cid, function(res){ti = res;});
	DWREngine.setAsync(true);
	
	ids = ids.split("/");
	var cnum = ids.length, ccnt = 0;
	data = {};
	for (i in ids) if (ids[i].length) {
		judgeService.getStandingData(ids[i], function(res){
			$.getJSON(res[1], function(contestData) {
				data[res[0]] = contestData;
				if (++ccnt == cnum){
					init();
					$("#status_processing").hide();
				}
			});
		});
	}
	if (ids[0].length == 0) {
		init();
		$("#status_processing").hide();
	}
}

function init() {
	maxTime = ti[1];
	calcScoreBoard();

	var exceedMax = false;
	var sl = $( "#time_controller" ).slider({
		range: "min",
		min: 0,
		max: ti[0],
		value: ti[1],
		slide: function( event, ui ) {
			$("#status_processing").show();
			if (ui.value > ti[1]) {
				exceedMax = true;
				return false;
			}
		},
		stop: function( event, ui ) {
			if (exceedMax) {
				sl.slider("value", ti[1]);
				maxTime = ti[1];
				exceedMax = false;
				getRemoteData();
			} else {
				maxTime = ui.value;
				calcScoreBoard();
			}
			changed = true;
			$("#status_processing").hide();
		}
	});
}

function calcScoreBoard() {
	var sb = {}, firstSolveTime = [], totalSubmission = [], correctSubmission = [], username = {}, nickname = {};
	for (var j = 0; j < pnum; ++j) {
		totalSubmission[j] = correctSubmission[j] = 0;
	}
	$.each(onlyCid ? {onlyCid: data[onlyCid]} : data, function(curCid, sInfo){
		$.each(sInfo, function(key, s) {
			if (key == 0) {
				for (uid in s) {
					var name = s[uid];
					username[uid] = name[0];
					nickname[uid] = name[1];
				}
				return;
			}
			if (s[3] > maxTime)return;
			var name = s[0] + "_" + curCid;
			if (!sb[name]){
				sb[name] = [];
			}
			if (sb[name][s[1]] == undefined){
				sb[name][s[1]] = [-1, 0];
			}
			if (sb[name][s[1]][0] < 0){
				totalSubmission[s[1]]++;
				if (s[2]) {
					sb[name][s[1]][0] = s[3];
					if (firstSolveTime[s[1]] == undefined || s[3] < firstSolveTime[s[1]]) {
						firstSolveTime[s[1]] = s[3];
					}
					correctSubmission[s[1]]++;
				} else {
					sb[name][s[1]][1]++;
				}
			}
		});
	});

	var result = [];
	for (name in sb){
		var solve = 0, penalty = 0;
		for (i in sb[name]){
			if (sb[name][i] && sb[name][i][0] >= 0){
				solve++;
				penalty += sb[name][i][0] + 1200 * sb[name][i][1];
			}
		}
		result.push([name, solve, penalty]);
	}

	result.sort(function(a, b){
		return b[1] - a[1] || a[2] - b[2];
	});
	
	var showNick = $.cookie("show_nick");
	var sbHtml = [];
	var my_username = $("#my_username").html();
	for (var i = 0; i < result.length; ++i) {
		var curInfo = result[i];
		var splitIdx = curInfo[0].lastIndexOf("_");
		var uid = curInfo[0].substr(0, splitIdx);
		var curCid = curInfo[0].substr(splitIdx + 1);
		sbHtml.push("<tr class='disp");
		if (cid == curCid) {
			sbHtml.push(" cur_tr");
			if (my_username == username[uid]) {
				sbHtml.push(" my_tr");
			}
		}
		sbHtml.push("' style='background:transparent' cid='" + curCid + "'><td>" + (i + 1) + "</td><td class='meta_td");
		if (username[uid]) {
			sbHtml.push("'><a href='user/profile.action?uid=" + uid + "'>" + (showNick > 0 ? nickname[uid] || username[uid] : username[uid]) + "</a></td><td class='meta_td");
		} else {
			sbHtml.push(" replay'>" + uid + "</td><td class='meta_td");
		}
		sbHtml.push("'>" + curInfo[1] + "</td><td class='meta_td'>" + dateFormat(curInfo[2]) + "</td>");

		var thisSb = sb[curInfo[0]];
		for (var j = 0; j <= pnum; ++j) {
			var probInfo = thisSb[j];
			if (!probInfo) {
				sbHtml.push("<td />");
			} else {
				sbHtml.push("<td ");
				if (probInfo[0] < 0) {
					sbHtml.push("class='red'");
				} else if (firstSolveTime[j] == probInfo[0]) {
					sbHtml.push("class='solvedfirst'");
				} else {
					sbHtml.push("class='green'");
				}
				sbHtml.push(">" + dateFormat(probInfo[0]) + "<br />" + (probInfo[1] ? "<span>(-" + probInfo[1] + ")</span>" : "　") + "</td>");
			}
		}
		sbHtml.push("</tr>");
	}
	if (sbHtml.length > 0) {
		sbHtml.push("<tr><td colspan='4' style='background-color:#EAEBFF'>Submission statistics</td>");
		var maxCorrectNumber = 0, totalNumber = 0, totalCorrectNumber = 0;
		for (var j = 0; j < pnum; ++j) {
			totalNumber += totalSubmission[j];
			totalCorrectNumber += correctSubmission[j];
			if (maxCorrectNumber < correctSubmission[j]) {
				maxCorrectNumber = correctSubmission[j];
			}
		}
		for (var j = 0; j < pnum; ++j) {
			if (!totalSubmission[j]) {
				sbHtml.push("<td style='background-color:white'/>");
			} else {
				var ratio = maxCorrectNumber ? correctSubmission[j] / maxCorrectNumber : 0.0;
				sbHtml.push("<td style='background-color:" + grayDepth(ratio) + ";color:" + (ratio < .5 ? "black" : "white") + "'>" + correctSubmission[j] + "/" + totalSubmission[j] + "<br />" + Math.floor(100 * correctSubmission[j] / totalSubmission[j]) + "%</td>")
			}
		}
		sbHtml.push("<td style='background-color:#D3D6FF'>" + totalCorrectNumber + "/" + totalNumber + "<br />" + Math.floor(100 * totalCorrectNumber / totalNumber) + "%</td></tr>");
	}
	standingTable.fnDestroy();
	var standingTableDOM = document.getElementById("standing");
	standingTableDOM.removeChild(standingTableDOM.lastChild);

	var $newTbody = $("<tbody id='standing_tbody' />")
	$("table#standing").append($newTbody);
	$("#standing_tbody").html(sbHtml.join(""));

	standingTable.dataTable(standingTableSetting);
	
	if ($.browser.msie) {
		$("#for_ie").show().appendTo($("#scoreboard"));
	}

	setTimeout(function(){
		if (!oFH) {
			oFH = new FixedHeader( standingTable );
		} else {
			oFH.fnUpdate();
		}
	}, 100);
	
	$("#time_index").css("width", (2 - $.cookie("penalty_format") + 100 * maxTime / ti[0]) + "%");
	$("#time_index span").text(dateFormat(maxTime));
	
	clearTimeout(autoRefresh);
	var period = $.cookie("auto_refresh_period");
	if (ti[1] < ti[0] && period >= 10 && period < 100000) {
		autoRefresh = setTimeout('getRemoteData()', period * 1000);
	}
}

function dateFormat(time){
	var formatIdx = $.cookie("penalty_format");
	if (time < 0)return "　";
	if (formatIdx == 0){
		var h = Math.floor(time / 3600);
		var m = Math.floor(time % 3600 / 60);
		var s = time % 60;
		return h + ":" + (m<10?"0":"") + m + ":" + (s<10?"0":"") + s;
	} else {
		return Math.floor(time / 60);
	}
}

function updateCheckAll() {
	if ($("[name=ids]:checked").length == 0){
		$("#checkAll").attr("checked", false);
	} else if ($("[name=ids]:not(:checked)").length == 0){
		$("#checkAll").attr("checked", true);
	}
}

function grayDepth(ratio) {
	var res = (Math.floor((1 - ratio) * 0xff) * 0x010101).toString(16);
	while (res.length < 6) res = '0' + res;
	return "#" + res;
}
