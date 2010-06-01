$(document).ready(function() {
	$('#listProblem').dataTable({
//		"bPaginate": false,
//		"bLengthChange": false,
//		"bFilter": false,
//		"bSort": false,
//		"bInfo": false,
		"bAutoWidth": false,
		"bStateSave": true,
		"aaSorting": [[ 3, "desc" ]],
		"aoColumns": [{"sType": "numeric"},
		              {"sType": "html"},
		              {"sType": "html"},
		              {"sType": "date"},
		              null,
		              null,
		              null
		              ]
	});
} );
