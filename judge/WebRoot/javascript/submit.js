$(document).ready(function() {
	
	var oj = $("input[name=oj]").val();
	
	if ($.cookie("lang_" + oj)) {
		$("select#language").val($.cookie("lang_" + oj));
	}

	$("select#language").change(function(){
		$.cookie("lang_" + oj, $(this).val(), {expires:30, path:'/'});
	});
	
	$("#form").submit(function(){
		$("#submit").attr("disabled", true);
		return true;
	});
});
