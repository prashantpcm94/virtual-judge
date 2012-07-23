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
		$("textarea[name='tmp_source']").attr("disabled", true);
		$("input[name='source']").val($("textarea[name='tmp_source']").val().replace("/*", "__comment_start__").replace("*/", "__comment_end__"));
		return true;
	});
});
