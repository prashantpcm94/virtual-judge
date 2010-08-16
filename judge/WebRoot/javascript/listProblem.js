var oTable;

$(document).ready(function() {
	oTable = $('#listProblem').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "problem/listProblem.action",
		
//		"bPaginate": false,
//		"bLengthChange": false,
//		"bFilter": false,
//		"bSort": false,
//		"bInfo": false,
		"bAutoWidth": false,
		"bStateSave": true,
		"aaSorting": [[ 3, "desc" ]],
		"aoColumns": [
			              {"sType": "numeric", "sClass": "center"},
			              {"sType": "html", "sClass": "title"},
			              {"sClass": "center"},
			              {"sType": "date", "sClass": "time"},
			              {"sClass": "opr"},
			              {"sClass": "opr"},
			              {"sClass": "opr"}
		              ]
	});
} );

var $gLoc;

function toggleAccess(id, $loc){
	$gLoc = $loc;
	baseService.toggleAccess(id, callback);
}

function callback(hidden){
	var title = $gLoc.parent()[0].innerHtml; 
	alert(title);
}