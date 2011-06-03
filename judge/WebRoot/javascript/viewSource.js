$(document).ready(function() {
	$("input[name=open]").change(function(){
		judgeService.toggleOpen($("[name=sid]").val(), dispInfo);
	});
});

function dispInfo(){
	if ($("input[name=open]:checked").val() == 1){
		$("p#info").css("visibility", "visible");
	} else {
		$("p#info").css("visibility", "hidden");
	}
}
