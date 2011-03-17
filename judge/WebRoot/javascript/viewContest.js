$(document).ready(function() {
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
});
