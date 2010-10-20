$(document).ready(function() {
	oTable = $('#status').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "problem/fetchStatus.action",
		"iDisplayLength": 20,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
//		"bStateSave": true,

		"aoColumns": [
		  			{},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return "<a href='user/profile.action?uid=" + oObj.aData[9] + "'>" + oObj.aData[1] + "</a>";
		  				},
		  			},
		  			{
		  				"fnRender": function ( oObj ) {
			  				return "<a href='problem/viewProblem.action?id=" + oObj.aData[2] + "'>" + oObj.aData[2] + "</a>";
		  				}
		  			},
		  			{
		  				"sClass": "result"
					},
		  			{
		  				"fnRender": function ( oObj ) {
		  					return oObj.aData[4] == null ? '' : oObj.aData[4] + " KB";
		  				}
		  			},
		  			{ 
		  				"fnRender": function ( oObj ) {
		  					return oObj.aData[5] == null ? '' : oObj.aData[5] + " ms";
		  				}
		  			},
		  			{ 
		  				"fnRender": function ( oObj ) {
	  						return oObj.aData[10] ? "<a " + (oObj.aData[10] == 2 ? "class='shared'" : "") + " href='problem/viewSource.action?id=" + oObj.aData[0] + "'>" + oObj.aData[6] + "</a>" : oObj.aData[6];
		  				}
		  			},
		  			{
		  				"fnRender": function ( oObj ) {
		  					return oObj.aData[7] + " B";
		  				}
		  			},
		  			{},
		  			{"bVisible": false},
		  			{"bVisible": false},
		  			{"bVisible": false},
		  			{"bVisible": false}
		  		],
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			var un = $("[name='un']").val();
			var id = $("[name='id']").val();
			var res = $("[name='res']").val();
		
			aoData.push( { "name": "un", "value": un } );
			aoData.push( { "name": "id", "value": id } );
			aoData.push( { "name": "res", "value": res } );
			$.ajax( {
				"dataType": 'json', 
				"type": "POST", 
				"url": sSource, 
				"data": aoData, 
				"success": fnCallback
			} );
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
		    $(nRow).addClass(aData[3]=="Accepted" ? "yes" : aData[3].indexOf("ing") < 0 || aData[3].indexOf("rror") >= 0 ? "no" : "pending");
			return nRow;
		},
		"sPaginationType": "full_numbers"
	});

	$("#form_status").submit(function(){
		var id = $("[name='id']").val();
		if (!id || parseInt(id)) {
			oTable.fnDraw();
		}
		return false;
	});
});
