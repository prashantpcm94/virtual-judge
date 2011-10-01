$(function(){
	
	///////////////////// miscellaneous ///////////////////////
	$( "#contest_tabs" ).tabs({
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


function comfirmDeleteContest(cid){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + cid;
	}
}
