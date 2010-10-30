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
		"bStateSave": true,
		"sPaginationType": "full_numbers",

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
		  					return oObj.aData[3] == 'Accepted' ? oObj.aData[4] + " KB" : "";
		  				},
						"sClass": "memory"
		  			},
		  			{ 
		  				"fnRender": function ( oObj ) {
		  					return oObj.aData[3] == 'Accepted' ? oObj.aData[5] + " ms" : "";
		  				},
		  				"sClass": "time"
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
		    $(nRow).attr("id", aData[0]);
		    
		    if ($(nRow).hasClass("pending")){
		    	getResult(aData[0]);
		    }
		    
			return nRow;
		}
	});
	
	$("#status_last").remove();

	$("#form_status").submit(function(){
		var id = $("[name='id']").val();
		if (!id || parseInt(id)) {
//			oTable.oSettings._iDisplayStart = 1;
			oTable.fnDraw();
		}
		return false;
	});
});

function getResult(id){
	baseService.getResult(id, cb);
}

function cb(back){
	var id = back[0];
	var result = back[1];
	var memory = back[2];
	var time = back[3];
	var $row = $("#" + id);
	if ($row.length){
		$(".result", $row).html(result);
		if (result.indexOf("ing") >= 0 && result.indexOf("rror") < 0){
			setTimeout("getResult(" + id + ")", 1000);
		} else if (result == "Accepted"){
			$row.removeClass("pending");
			$row.addClass("yes");
			$(".memory", $row).html(memory + " KB");
			$(".time", $row).html(time + " ms");
		} else {
			$row.removeClass("pending");
			$row.addClass("no");
		}
	}
}
