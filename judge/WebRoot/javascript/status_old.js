$(document).ready(function() {
	var oTable = $('#status').dataTable({
		"aaSorting": [[ 1, "asc" ]],
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false
	});
});
