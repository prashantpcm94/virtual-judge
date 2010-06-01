$(document).ready(function() {
	$('#standing').dataTable({
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": true,
//		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
		"aoColumns": [{"sType": "numeric"},
		              {"sType": "html"},
		              {"sType": "numeric"},
		              {"sType": "date"}
		              ]
		
	});
} );
