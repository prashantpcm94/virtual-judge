var descList;
var originWidth = {}, originHeight = {};

function show(thisId){
	$(".hiddable").hide();
	$(".selected").removeClass("selected");
	$(".desc_info:eq(" + thisId + ")").addClass("selected");
	$(".opt:eq(" + thisId + ")").show();
	$(".remark:eq(" + thisId + ")").animate({height:'show',opacity:'show'}, 'fast');
	
	for (elem in descList[thisId]){
		if (descList[thisId][elem]){
			$("#vj_" + elem).show();
			$("#vj_" + elem + " div").html(descList[thisId][elem]);
		}
	}
	
	$(".textBG img").load(function(){
		originWidth[$(this).attr("src")] = $(this).attr("width");
		originHeight[$(this).attr("src")] = $(this).attr("height");
		$(this).resizeImg();
	});
}

$(document).ready(function() {
	
	DWREngine.setAsync(false);
	judgeService.fetchDescriptions($("input[name=pid]").val(), function(dl){descList = dl;});
	DWREngine.setAsync(true);
	
	$(".desc_info").click(function(){
		if (!$(this).hasClass("selected")){
			show($(this).children()[0].id.substring(4));
		}
	});
	
	$("a.vote").click(function(){
		$.get("problem/vote4Description.action", {id: $(this)[0].id.substring(5)});
		$(this).parent().next().children().eq(1).html(parseInt($(this).parent().next().children().eq(1).html()) + 1);
		$("a.vote").each(function(){
			$(this).parent().next().show();
			$(this).parent().remove();
		});
	});
	
	$("a.delete_desc").click(function(){
		if (confirm("Sure to delete this description?")){
			$.get("problem/deleteDescription.action", {id: $(this)[0].id.substring(4)}, function() {
				location.reload();
			});
		}
	});
	
	if (location.href.indexOf("edit=") >= 0){
		show($("input[name=vote]").length - 1);
	} else {
		var maxIdx, maxVote = -1;
		$("input[name=vote]").each(function(idx){
			var vote = $(this).val();
			if (vote.match(/\d+/)){
				curVote = parseInt(vote);
				if (curVote >= maxVote){
					maxVote = curVote;
					maxIdx = idx;
				}
			}
		});
		show(maxIdx);
	}

});

$(window).resize(function(){
	$(".textBG img").each(function(){
		$(this).resizeImg();	
	});
});

jQuery.prototype.resizeImg = function(){
	var src = $(this).attr("src");
	if (originWidth[src] != undefined){
		var frameWidth = (document.body.clientWidth - 320) * 0.99;
		var scale = Math.min(frameWidth / originWidth[src], 1.0);
		$(this).attr("width", scale * originWidth[src]);
		$(this).attr("height", scale * originHeight[src]);
	}
}
