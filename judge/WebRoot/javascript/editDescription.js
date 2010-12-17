$(document).ready(function() {
	var form = document.getElementById("editorsForm");
	$("#remarks").focus();

	$("#editorsForm").submit(function(){
		if ($("[name=description.remarks]").val().match(/^\s*$/)){
			form.scrollTop = 0;
			$("#remarks").focus();
			alert("Fill some remarks, please ~~");
			return false;
		}
		if ($("[name=description.remarks]").val().length > 450){
			form.scrollTop = 0;
			$("#remarks").focus();
			alert("Remarks is too long !!");
			return false;
		}
	});

});