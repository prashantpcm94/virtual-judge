$(document).ready(function() {
	$('#standing1').dataTable({
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
});
