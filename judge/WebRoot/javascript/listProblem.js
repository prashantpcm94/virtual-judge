$(document).ready(function() {
	$('#listProblem').dataTable({
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
