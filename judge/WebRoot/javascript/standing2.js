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
});

function fill_table(){
	
	var sameContests = $("[name=sameContests]").val();
	var cids = sameContests.split(' ');
	for (cid in cids){
		alert(cid);
	}
	
	
}

