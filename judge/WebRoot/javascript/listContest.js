$(document).ready(function() {
	$("input[type='checkbox']").each(function(){
		if ($.cookie("checked_" + $(this).attr("name")) == 'false') {
			$(this).removeAttr("checked");
		}
	});
	
	var oTable = $('#listContest').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "contest/listContest.action",
		"iDisplayLength": 25,
		"bAutoWidth": false,
		"bStateSave": true,
		"aaSorting": [[ 3, "desc" ]],

		"aoColumns": [
		  			{ 
		  				"sClass": "id"
		  			},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return oObj.aData[1] == 1 ? "<img height='15px' border='0' src='images/replay.png' />" : "";
		  				},
		  				"bSortable": false
		  			},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return "<a href='contest/viewContest.action?cid=" + oObj.aData[0] + "'>" + oObj.aData[2] + "</a>";
		  				},
		  				"sClass": "title"
		  			},
{
		  				"fnRender": function ( oObj ) {
			  				return new Date(parseInt(oObj.aData[3])).format("yyyy-MM-dd hh:mm:ss");
		  				},
		  				"sClass": "date"
		  			},
		  			{ 
		  				"bSortable": false,
		  				"sClass": "time"
		  			},
		  			{
		  				"bSortable": false,
		  				"sClass": "center status"
		  			},
		  			{ 
		  				"bSortable": false,
		  				"sClass": "center type"
		  			},
		  			{ 
		  				"fnRender": function ( oObj ) {
		  					return "<a href='user/profile.action?uid=" + oObj.aData[8] + "'>" + oObj.aData[7] + "</a>";
		  				},
		  				"sClass": "center"
		  			},
		  			{
		  				"bVisible": false
		  			},
		  			{ 
		  				"fnRender": function ( oObj ) {
		  					if (oObj.aData[9] == 1) {
		  						return "<a href='contest/toEditContest.action?cid=" + oObj.aData[0] + "'><img height='15px' border='0' src='images/wrench.gif' /></a>&nbsp;<a href='javascript:void(0)' onclick='comfirmDeleteContest(\"" + oObj.aData[0] + "\")'><img height='15px' border='0' src='images/recycle.gif' /></a>";
		  					} else return "";
		  				},
		  				"bUseRendered": false, 
		  				"bSearchable": false,
		  				"bSortable": false,
		  				"sClass": "id icon"
		  			}
		  		],
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			var s = $("[name='scheduled']").attr("checked");
			var r = $("[name='running']").attr("checked");
			var e = $("[name='ended']").attr("checked");
		
			aoData.push( { "name": "s", "value": s } );
			aoData.push( { "name": "r", "value": r } );
			aoData.push( { "name": "e", "value": e } );
			$.ajax( {
				"dataType": 'json', 
				"type": "POST", 
				"url": sSource, 
				"data": aoData, 
				"success": fnCallback
			} );
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		    $('td:eq(5)', nRow).addClass(aData[5]);
		    $('td:eq(6)', nRow).addClass(aData[6]);
		    nRow.className += " " + aData[5];
			return nRow;
		},
		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});

	$("div.head_status").insertBefore("div#listContest_processing").show();
	$("div.dataTables_filter").css("width", "250px");

	$("input[type='checkbox']").change(function(){
		$.cookie("checked_" + $(this).attr("name"), $(this).attr("checked"), {expires:7});
		$("[name='"+$(this).attr("name")+"']").attr("checked", $(this).attr("checked"));
		oTable.fnDraw();
	});
});

function comfirmDeleteContest(id){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + id;
	}
}
