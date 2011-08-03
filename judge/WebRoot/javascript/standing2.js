var cid, onlyCid, data, ti, maxTime, changed, standingTable, oFH;

var standingTableSetting = {
	"bPaginate": false,
	"bLengthChange": false,
	"bFilter": true,
	"bInfo": false,
	"bAutoWidth": false,
	"aoColumns": [{"sType": "numeric"}, {"sType": "string"}, {"sType": "numeric"}, {"sType": "date"}]
};

$(document).ready(function() {

	cid = $("[name=cid]").val();
	pnum = $("tr#template td").length - 5;

	if ($.cookie("contest_" + cid) == undefined){
		$.cookie("contest_" + cid, cid, { expires: 3 });
	}
	if ($.cookie("penalty_format") == undefined){
		$.cookie("penalty_format", 0, { expires: 30 });
	}

	$( "#tabs" ).tabs({
		show: function(event, ui) {
			if (ui.index == 0){
				onlyCid = 0;
				$("div#contestTitle").text("　");
				getRemoteData();
				setTimeout(function(){
					oFH = new FixedHeader( standingTable );
				}, 100);
			}
		},
		select: function(event, ui) {
			if (ui.index != 0) {
				$("div.FixedHeader_Cloned").hide();
			} else {
				$("div.FixedHeader_Cloned").show();
			}
		},
		cookie: { expires: 30 }
	});

	$( "#tabs" ).removeClass("ui-widget-content");
	$( "#tabs" ).addClass("ui-widget-content-custom");

	standingTable = $('#standing').dataTable(standingTableSetting);

	$('#setting table').dataTable({
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

	$("td.meta_td").live("mouseover", function(){
		var curCid = $(this).parent().attr("cid");
		$("tr[cid=" + curCid + "] td.meta_td").addClass("sameTd")
	}).live("mouseout", function(){
		var curCid = $(this).parent().attr("cid");
		$("tr[cid=" + curCid + "] td.meta_td").removeClass("sameTd")
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

	$("#refresh").click(function(){
		getRemoteData();
		return false;
	});

	var ids = $.cookie("contest_" + cid);
	if (!!ids) {
		ids = ids.split("/");
		for (var i = 0; i < ids.length; i++){
			$("input[type=checkbox][value=" + ids[i] + "]").attr("checked",'true');
		}
	}

	var formatIdx = $.cookie("penalty_format");
	$("[name=penaltyFormat]").get(formatIdx).checked = 1;
	
	updateCheckAll();
});

function getRemoteData(){
	$("#status_processing").show();
	var ids = $.cookie("contest_" + cid);
	if (!ids){
		$("tr.disp").remove();
		$("#status_processing").hide();
		return;
	}

	DWREngine.setAsync(false);
	judgeService.getContestTimeInfo(cid, function(res){ti = res;});
	DWREngine.setAsync(true);

	ids = ids.split("/");
	var cnum = ids.length, ccnt = 0;
	data = {};
	for (i in ids){
		judgeService.getStandingData(ids[i], function(res){
			$.getJSON(res[1], function(contestData) {
				data[res[0]] = contestData;
				if (++ccnt == cnum){
					init();
				}
			});
		});
	}
}

function init() {
	maxTime = ti[1];
	calcScoreBoard();

	$( "#time_controller" ).slider({
		range: "min",
		min: 0,
		max: ti[0],
		value: ti[1],
		slide: function( event, ui ) {
			if (ui.value > ti[1])return false;
		},
		stop: function( event, ui ) {
			maxTime = ui.value;
			calcScoreBoard();
			changed = true;
		}
	});

	$("#status_processing").hide();
}

function calcScoreBoard(){
	$("#status_processing").show();

	var sb = {}, firstSolveTime = [];
	$.each(onlyCid ? {onlyCid: data[onlyCid]} : data, function(curCid, sInfo){
		$.each(sInfo, function(key, s){
			if (s[3] > maxTime)return;
			var name = s[0] + "_" + curCid;
			if (!sb[name]){
				sb[name] = [];
			}
			if (sb[name][s[1]] == undefined){
				sb[name][s[1]] = [-1, 0];
			}
			if (sb[name][s[1]][0] < 0){
				if (s[2]) {
					sb[name][s[1]][0] = s[3];
					if (firstSolveTime[s[1]] == undefined || s[3] < firstSolveTime[s[1]]) {
						firstSolveTime[s[1]] = s[3];
					}
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

	standingTable.fnDestroy();
	$("tr.disp").remove();

	var $originRow = $("tr#template");
	$.each(result, function(i, v1){
		var $newRow = $originRow.clone().removeAttr("id").attr("class", "disp");
		var splitIdx = v1[0].lastIndexOf("_");
		var curCid = v1[0].substr(splitIdx + 1);
		$.each(sb[v1[0]], function(j, v2){
			v1.push(v2 ? (firstSolveTime[j] == v2[0] ? " " : "") + dateFormat(v2[0]) + "<br />" + (v2[1] ? "<span>(-" + v2[1] + ")</span>" : "　") : "");
		});
		v1[0] = v1[0].substr(0, splitIdx);
		v1[2] = dateFormat(v1[2]);
		$newRow.attr("cid", curCid);

		var $curTd = $("td:eq(0)", $newRow);
		$curTd.html(i + 1);
		$curTd = $curTd.next();
		
		for (k in v1){
			$curTd.html(v1[k]);
			if (k <= 2){
				$curTd.addClass("meta_td");
				if (cid == curCid){
					$curTd.addClass("curTd");
				}
			} else if (v1[k].length){
				$curTd.addClass(v1[k].charAt(0) == '　' ? "red" : v1[k].charAt(0) == ' ' ? "solvedfirst" : "green");
			}
			$curTd = $curTd.next();
		}
		$newRow.insertBefore($originRow).show();
	});
	standingTable.dataTable(standingTableSetting);
	setTimeout(function(){
		oFH = new FixedHeader( standingTable );
	}, 100);
	
	var formatIdx = $.cookie("penalty_format");
	$("#time_index").css("width", (2 - formatIdx + 100 * maxTime / ti[0]) + "%");
	$("#time_index span").text(dateFormat(maxTime));

	$("#status_processing").hide();
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
