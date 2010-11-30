$(document).ready(function(){

	DWREngine.setAsync(false);

	$("#addBtn").click(function(){
		addRow();
	});
	
	$(".deleteRow").live("click", function(){
		$(this).parent().parent().remove();
		updateNum();
		if ($("#addTable tr:visible").length < 26){
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
		if ($("#addTable tr:visible").length < 1){
			$("#errorMsg").html("Please add one problem at least!");
			return false;
		}
		if ($("#addTable tr:visible").length > 26){
			$("#errorMsg").html("At most 26 problems!");
			return false;
		}
		var dup = 0, err = 0, $trs = $("#addTable tr:visible");
		for (i = 0; i < $trs.length; i=i+1){
			for (j = 0; j < i; j=j+1){
				if ($("[name=OJs]", $trs.eq(i)).val() == $("[name=OJs]", $trs.eq(j)).val() && $("[name=probNums]", $trs.eq(i)).val() == $("[name=probNums]", $trs.eq(j)).val()){
					dup = 1;
					break;
				}
			}
			if ($trs.eq(i).children().eq(-1).html()[1] != 'a'){
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
		$("tr:not(:visible)").remove();
	});

	if ($("#addTable tr:visible").length < 1){
		addRow();
	}
	$("#addTable tr:visible").each(function(){
		updateTitle($(this));
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
	$("#addTable tr:visible").each(function(index){
		$last = $("td:last-child", $(this)); 
		if ($last.html()[1] == 'a' || $last.html()[1] == 'A'){
			$last.prev().html(String.fromCharCode(65 + index) + " - ");
		} else {
			$last.prev().html("");
		}
	});
}

function addRow(){
	var $originRow;
	if ($("#addTable tr:visible").length){
		$originRow = $("tr#addRow").prev();
	} else {
		$originRow = $("tr#addRow");
	}
	$newRow = $originRow.clone();
	$("[name=OJs]", $newRow).val($("[name=OJs]", $originRow).val());
	
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
	
	if ($("#addTable tr:visible").length >= 26){
		$("#addBtn").hide();
	}
}
