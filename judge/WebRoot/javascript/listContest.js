$(document).ready(function() {
	$("input[type='checkbox']").each(function(){
		if ($.cookie("checked_" + $(this).attr("name")) == 'false') {
			$(this).removeAttr("checked");
		}
	});

	if ($.cookie("contestType") == undefined) {
		$.cookie("contestType", 0, {expires:7});
	}
	$("input[name='contestType']").get($.cookie("contestType")).checked = 1;
	
	var oTable = $('#listContest').dataTable({
		"bProcessing": true,
		"bServerSide": true,
		"sAjaxSource": "contest/listContest.action",
		"iDisplayLength": 25,
		"bAutoWidth": false,
		"bStateSave": true,
		"sDom": '<"H"pfr>t<"F"il>',
		"oLanguage": {
			"sInfo": "_START_ to _END_ of _TOTAL_ contests",
			"sInfoEmpty": "No contests",
			"sInfoFiltered": " (filtering from _MAX_ total contests)"
		},
		"aaSorting": [[ 2, "desc" ]],
		"aoColumnDefs": [
			{ "asSorting": [ "desc", "asc" ], "aTargets": [ "_all" ] }
		],

		"aoColumns": [
					{ 
						"sClass": "id"
					},
					{
						"fnRender": function ( oObj ) {
							return "<div class='title'><a href='contest/viewContest.action?cid=" + oObj.aData[0] + "'>" + oObj.aData[1] + "</a></div>";
						},
						"sClass": "title"
					},
					{
						"fnRender": function ( oObj ) {
							return new Date(parseInt(oObj.aData[2])).format("yyyy-MM-dd hh:mm:ss");
						},
						"sClass": "date"
					},
					{ 
						"bSortable": false,
						"sClass": "time"
					},
					{ 
						"bSortable": false,
						"sClass": "center type"
					},
					{ 
						"fnRender": function ( oObj ) {
							return "<a href='user/profile.action?uid=" + oObj.aData[6] + "'>" + oObj.aData[5] + "</a>";
						},
						"sClass": "manager center"
					},
					{ 
						"fnRender": function ( oObj ) {
							if (oObj.aData[7] == 1) {
								return "<a href='contest/toEditContest.action?cid=" + oObj.aData[0] + "'><img height='15px' border='0' src='images/wrench.gif' /></a>&nbsp;<a href='javascript:void(0)' onclick='comfirmDeleteContest(\"" + oObj.aData[0] + "\")'><img height='15px' border='0' src='images/recycle.gif' /></a>";
							} else return "";
						},
						"bUseRendered": false, 
						"bSearchable": false,
						"bSortable": false,
						"sClass": "id icon"
					},
					{
						"bVisible": false
					},
					{
						"bVisible": false
					}
				],
		"fnServerData": function ( sSource, aoData, fnCallback ) {
			var s = $("[name='scheduled']").prop("checked");
			var r = $("[name='running']").prop("checked");
			var e = $("[name='ended']").prop("checked");
			var contestType = $("[name='contestType']:checked").val();
		
			aoData.push( { "name": "s", "value": s } );
			aoData.push( { "name": "r", "value": r } );
			aoData.push( { "name": "e", "value": e } );
			aoData.push( { "name": "contestType", "value": contestType } );
			$.ajax({
				"dataType": 'json', 
				"type": "POST", 
				"url": sSource, 
				"data": aoData, 
				"success": fnCallback
			});
		},
		"fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
			$('td:eq(4)', nRow).addClass(aData[4]);
			nRow.className += " " + aData[8];
			return nRow;
		},
		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});
	
	var query = parseUrlParameter()['q'];
	if (query) {
		setTimeout(function(){
			oTable.fnFilter(query);
		}, 300);
	}

	$("div.head_status").insertBefore("div#listContest_processing").show();
	$("div.dataTables_filter").css("width", "250px");

	$("input[type='checkbox']").change(function() {
//		alert("checked_" + $(this).attr("name") + "     " +  ($(this).prop("checked")));
		$.cookie("checked_" + $(this).attr("name"), $(this).prop("checked"), {expires:7});
//		$("[name='"+$(this).attr("name")+"']").attr("checked", $(this).attr("checked"));
		oTable.fnDraw();
	});
	
	$("input[name='contestType']").change(function(){
		$.cookie("contestType", $(this).val(), {expires:7});
		oTable.fnDraw();
	});

});

function comfirmDeleteContest(cid){
	if (confirm("Sure to delete this contest?")){
		location = 'contest/deleteContest.action?cid=' + cid;
	}
}
