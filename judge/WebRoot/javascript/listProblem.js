var oTable;

$(document).ready(function() {
	oTable = $('#listProblem').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "problem/listProblem.action",
		"iDisplayLength": 25,
		
//		"bPaginate": false,
//		"bLengthChange": false,
//		"bFilter": false,
//		"bSort": false,
//		"bInfo": false,
		"bAutoWidth": false,
		"bStateSave": true,
		"aaSorting": [[ 3, "desc" ]],
		"aoColumns": [
			              {"sClass": "center"},
			              {"sClass": "title"},
			              {"sClass": "center"},
			              {"sClass": "time"},
			              {"sClass": "opr"},
			              {"sClass": "opr"},
			              {"sClass": "opr"}
		              ],
		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});
} );

var $gLoc;

function toggleAccess(id, $loc){
	$gLoc = $loc;
	baseService.toggleAccess(id, callback);
}

function callback(hidden){
	var title = $gLoc.parent().prev().prev().prev().prev().prev()[0];
	var here = $gLoc[0];
	if (!hidden){
		title.innerHTML = title.innerHTML.replace(/<font.*?font>/i, '');
		here.innerHTML = here.innerHTML.replace(/Reveal/, 'Hide');
	} else {
		title.innerHTML = title.innerHTML + "<font color=\"red\">(Hidden)<\/font>";
		here.innerHTML = here.innerHTML.replace(/Hide/, 'Reveal');
	}
}