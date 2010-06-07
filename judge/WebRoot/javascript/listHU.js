$(document).ready(function() {
	$('#listHU').dataTable({
		"aaSorting": [[ 1, "asc" ]],
		"bPaginate": false,
		"bLengthChange": false,
//		"bFilter": false,
//		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
		"aoColumns": [null,
		              {"sType": "date"},
		              null
		              ]
	});
} );
