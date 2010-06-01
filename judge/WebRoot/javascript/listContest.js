$(document).ready(function() {
	$('#listContest').dataTable({
		"aaSorting": [[ 2, "asc" ]],
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
//		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
		"aoColumns": [{"sType": "numeric"},
		              {"sType": "html"},
		              {"sType": "date"},
		              {"sType": "date"},
		              null,
		              null,
		              {"sType": "html"},
		              {"sType": "html"}
		              ]
	});
} );
