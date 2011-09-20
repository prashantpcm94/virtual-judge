if(typeof String.prototype.trim !== 'function') {
	String.prototype.trim = function() {
		return this.replace(/^\s+|\s+$/g, '');
	}
}

function parseUrlParameter() {
	var params = new Object();
	var startpos = window.location.href.indexOf("?");
	var pieces = window.location.href.substring(startpos + 1).split("&");
	for(var i = 0; i < pieces.length; i++) {
		try {
			var keyvalue = pieces[i].split("=");
			params[keyvalue[0]] = keyvalue[1];
		} catch(e){}
	}
	return params;
}

Date.prototype.format = function(format){
	var o = {
		"M+": this.getMonth() + 1, //month
		"d+": this.getDate(), //day
		"h+": this.getHours(), //hour
		"m+": this.getMinutes(), //minute
		"s+": this.getSeconds(), //second
		"q+": Math.floor((this.getMonth() + 3) / 3), //quarter
		"S": this.getMilliseconds() //millisecond
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

$(function(){
	var allFields = $( [] ).add( $( "input.text" ) ).add( $( "#blog" ) ),
		tips = $( ".validateTips" );

	function updateTips( t ) {
		tips.text( t ).addClass( "ui-state-highlight" );
		setTimeout(function() {
			tips.removeClass( "ui-state-highlight", 1500 );
		}, 500 );
	}

	$( "#dialog-form-login" ).dialog({
		autoOpen: false,
		height: 270,
		width: 350,
		modal: true,
		buttons: {
			"Login": function() {
				var info = {username: $("#username").val(), password: $("#password").val()};
				$.post('user/login.action', info, function(data) {
					if (data == "success") {
						$( this ).dialog( "close" );
						window.location.reload();
					} else {
						updateTips(data);						
					}
				});
			},
			"Cancel": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" );
		}
	});
	
	$( "#dialog-form-register" ).dialog({
		autoOpen: false,
		height: 570,
		width: 480,
		modal: true,
		buttons: {
			"Register": function() {
				var info = {
					username: $("#username1").val(),
					password: $("#password1").val(),
					repassword: $("#repassword").val(),
					nickname: $("#nickname").val(),
					school: $("#school").val(),
					qq: $("#qq").val(),
					email: $("#email").val(),
					blog: $("#blog").val(),
					share: $("#share").val()
				};
				$.post('user/register.action', info, function(data) {
					if (data == "success") {
						$( this ).dialog( "close" );
						window.location.reload();
					} else {
						updateTips(data);						
					}
				});
			},
			"Cancel": function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
			allFields.val( "" );
		}
	});

	$("#login").click(function(){
		tips.html("Fill the blank");
		$( "#dialog-form-login" ).dialog( "open" );
	});

	$("#register").click(function(){
		tips.html("Fill the blank");
		$( "#dialog-form-register" ).dialog( "open" );
	});

	$("#logout").click(function(){
		$.get("user/logout.action", function(){
			window.location.reload();
		});
	});

});
