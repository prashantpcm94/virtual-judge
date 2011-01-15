$(document).ready(function(){

	DWREngine.setAsync(false);

	$("#addBtn").click(function(){
		addRow();
	});
	
	$(".deleteRow").live("click", function(){
		$(this).parent().parent().remove();
		updateNum();
		if ($("#addTable tr.tr_problem:visible").length < 26){
			$("#addBtn").show();
		}
	});
	
	$("[name=OJs]").live($.browser.msie ? 'click' : 'change', function(){
		last = "vj";
		updateTitle($(this).parent().parent());
	});
	
	$("[name=probNums]").live("focus", function(){
		$row = $(this).parent().parent();
		last = $("[name=probNums]", $row).val();
		thread = window.setInterval(function(){
			updateTitle($row);
		}, 100 );
	});

	$("[name=probNums]").live("blur", function(){
		window.clearInterval(thread);
		updateTitle($(this).parent().parent());
	});

	$("#form").submit(function(){
		$("#errorMsg").html("");
		if ($("#addTable tr.tr_problem:visible").length < 1){
			$("#errorMsg").html("Please add one problem at least!");
			return false;
		}
		if ($("#addTable tr.tr_problem:visible").length > 26){
			$("#errorMsg").html("At most 26 problems!");
			return false;
		}
		var dup = 0, err = 0, $trs = $("#addTable tr.tr_problem:visible");
		for (i = 0; i < $trs.length; i=i+1){
			for (j = 0; j < i; j=j+1){
				if ($("[name=OJs]", $trs.eq(i)).val() == $("[name=OJs]", $trs.eq(j)).val() && $("[name=probNums]", $trs.eq(i)).val() == $("[name=probNums]", $trs.eq(j)).val()){
					dup = 1;
					break;
				}
			}
			tmp = $trs.eq(i).children().eq(-1).html().charAt(1);
			if (tmp != 'a' && tmp != 'A'){
				err = 1;
				break;
			}
		}
		if (dup == 1){
			$("#errorMsg").html("Duplcate problems are not allowed!");
			return false;
		}
		if (err == 1){
			$("#errorMsg").html("There are invalid problems!");
			return false;
		}
		
		var hour = parseInt($("#hour").val());
		var minute = parseInt($("#minute").val());
		var d_day = parseInt($("#d_day").val());
		var d_hour = parseInt($("#d_hour").val());
		var d_minute = parseInt($("#d_minute").val());
		if (isNaN(hour) || hour < 0 || hour > 23 || isNaN(minute) || minute < 0 || minute > 59 || isNaN(d_day) || d_day < 0 || isNaN(d_hour) || d_hour < 0 || d_hour > 23 || isNaN(d_minute) || d_minute < 0 || d_minute > 59){
			$("#errorMsg").html("Date format error!");
			return false;
		}
		$("[name=contest.beginTime]").val($("[name=contest.beginTime]").val() + " " + hour + ":" + minute + ":00");
	
		$("tr:not(:visible)").remove();
	});

	if ($("#addTable tr.tr_problem:visible").length < 1){
		addRow();
	}
	$("#addTable tr.tr_problem:visible").each(function(){
		updateTitle($(this));
	});
	
	
	
	$("[name=contest.beginTime]").datepicker({
		dateFormat: 'yy-mm-dd',
		minDate: -0,
		maxDate: +30,
		showAnim: 'blind'
	});
	
	$(".clk_select").click(function(){
		this.select();
	});
	$(".clk_select").blur(function(){
		if (!this.value || this.value.match(/^\s+$/)){
			this.value = 0;
		}
	});

});

var problemInfo;
function callBack(_problemInfo){
	problemInfo = _problemInfo;
}

var last;
function updateTitle($row){
	if ($("[name=probNums]", $row).val() == last)return;
	last = $("[name=probNums]", $row).val();
	judgeService.findProblemSimple($("[name=OJs]", $row).val(), $("[name=probNums]", $row).val(), callBack);
	if (problemInfo == null){
		$row.children().eq(-1).html("<font color='red'>No such problem!</font>");
	} else {
		$row.children().eq(-1).html("<a target='_blank' href='problem/viewProblem.action?id=" + problemInfo[0] + "'>" + problemInfo[1] +"</a>");
		$("[name=pids]", $row).val(problemInfo[0]);
	}
	updateNum();
}

function updateNum(){
	$("#addTable tr.tr_problem:visible").each(function(index){
		$last = $("td:last-child", $(this)); 
		if ($last.html().charAt(1) == 'a' || $last.html().charAt(1) == 'A'){
			$last.prev().html(String.fromCharCode(65 + index) + " - ");
		} else {
			$last.prev().html("");
		}
	});
}

function addRow(){
	var $originRow;
	if ($("#addTable tr.tr_problem:visible").length){
		$originRow = $("tr#addRow").prev();
	} else {
		$originRow = $("tr#addRow");
	}
	$newRow = $originRow.clone();
	$("[name=OJs]", $newRow).val($("[name=OJs]", $originRow).val());
	$("[name=titles]", $newRow).val("");

	$newRow.removeAttr("id");
	$(":input", $newRow).removeAttr("id");
	
	var $probNum = $("[name=probNums]", $newRow);
	if ($probNum.val().match(/^\s*\d+\s*$/)){
		$probNum.val(parseInt($probNum.val()) + 1);
	} else {
		$probNum.val("");
	}
	$newRow.insertBefore("tr#addRow").show();
	updateTitle($newRow);
	
	if ($("#addTable tr.tr_problem:visible").length >= 26){
		$("#addBtn").hide();
	}
}
