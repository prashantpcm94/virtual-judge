$(document).ready(function() {
	$("#form").submit(function(){
		$("#submit").attr("disabled", true);
		return true;
	});
});
