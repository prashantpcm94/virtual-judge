$(function(){
	
	///////////////////// miscellaneous ///////////////////////

	$(window).hashchange( function(){
		tabNavigate();

	});
	
	$( "#contest_tabs" ).tabs({
		show: function(event, ui) {
		},
		select: function(event, ui) {
			if (ui.index == 0){
				location.hash = "#overview";
			} else if (ui.index == 1){
				location.hash = "#problem";
			} else if (ui.index == 2){
				location.hash = "#status";
			} else if (ui.index == 3){
				location.hash = "#standing";
			}
		}
	});

	
	
	/////////////////////   Overview    //////////////////////
	$('#viewContest').dataTable({
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false
	});
	
	$("span.plainDate").each(function(){
		$(this).html(new Date(parseInt($(this).html())).format("yyyy-MM-dd hh:mm:ss"));
	});
	
	

	
	/////////////////////    Problem    //////////////////////

	/////////////////////    Status     //////////////////////

	/////////////////////     Rank      //////////////////////

	
});

function tabNavigate() {
	var hash = location.hash.split("/");
	if (hash[0] == "#problem") {
		showProblem(hash);
	} else if (hash[0] == "#status") {
		showStatus(hash);
	} else if (hash[0] == "#rank") {
		showRank(hash);
	} else {
		showOverview();
	}
}

function comfirmDeleteContest(cid){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + cid;
	}
}
