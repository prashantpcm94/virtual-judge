$(document).ready(function() {
	var oTable = $('#listProblem').dataTable({
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
					{
		  				"sClass": "center status",
						"bSortable": false
					},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return "<a href='" + oObj.aData[6] + "'>" + oObj.aData[1] + "</a>";
		  				},
		  				"sClass": "center status"
		  			},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return "<a href='problem/viewProblem.action?id=" + oObj.aData[5] + "'>" + oObj.aData[2] + "</a>";
		  				},
		  				"sClass": "title"
		  			},
		  			{
		  				"sClass": "time"
		  			},
		  			{},
		  			{"bVisible": false},
		  			{"bVisible": false}
              ],
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			var OJId = $("#OJId").val();
			aoData.push( { "name": "OJId", "value": OJId } );
			$.ajax( {
				"dataType": 'json', 
				"type": "POST", 
				"url": sSource, 
				"data": aoData, 
				"success": fnCallback
			} );
		},

		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});
	
	$("#addBtn").css("visibility", $("#OJId").val() == 'All' ? "hidden" : "visible");

	$("#OJId").change(function(){
		$("#addBtn").css("visibility", $("#OJId").val() == 'All' ? "hidden" : "visible");
		oTable.fnDraw();
	});
	
});
