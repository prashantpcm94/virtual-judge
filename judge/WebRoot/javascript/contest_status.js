$(document).ready(function() {
	
	var href = location.href;
	var cid = href.substring(href.indexOf("cid") + 4);
	
	oTable = $('#status').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "contest/fetchStatus.action?cid=" + cid,
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
			  				return "<a href='contest/viewProblem.action?pid=" + oObj.aData[11] + "'>" + oObj.aData[2] + "</a>";
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
	  						return oObj.aData[10] ? "<a " + (oObj.aData[10] == 2 ? "class='shared'" : "") + " href='contest/viewSource.action?id=" + oObj.aData[0] + "'>" + oObj.aData[6] + "</a>" : oObj.aData[6];
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
		  			{"bVisible": false}
		  		],
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			var un = $("[name='un']").val();
			var num = $("[name='num']").val();
			var res = $("[name='res']").val();
		
			aoData.push( { "name": "un", "value": un } );
			aoData.push( { "name": "num", "value": num } );
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
		$(".errorMessage").remove();
		var id = $("[name='id']").val();
		if (!id || parseInt(id)) {
			oTable.fnPageChange( 'first' );
		}
		return false;
	});
	
	$("#reset").click(function(){
		$(".errorMessage").remove();
		$("[name='un']").val("");
		$("[name='num']").val("All");
		$("[name='res']").val(0);
		oTable.fnPageChange( 'first' );
	});
	
	if (location.href.indexOf("reset") >= 0){
		oTable.fnPageChange( 'first' );
	}
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
