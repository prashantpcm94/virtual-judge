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

function doIfLoggedIn(func) {
	
	
}

var nextUrl;



$(function(){

	var updateTips = function ( t ) {
		var tips = $( "p.validateTips" );
		tips.text( t ).addClass( "ui-state-highlight" );
		setTimeout(function() {
			tips.removeClass( "ui-state-highlight", 1500 );
		}, 500 );
	}
	
	$( "#dialog-form-login" ).dialog({
		autoOpen: false,
		height: 270,
		width: 350,
		position: ['top', 50],
		modal: true,
		buttons: {
			"Login": function() {
				var info = {username: $("#username").val(), password: $("#password").val()};
				$.post('user/login.action', info, function(data) {
					if (data == "success") {
						$( this ).dialog( "close" );
						if (nextUrl != "javascript:void(0)") {
							window.location.href = nextUrl;
						} else {
							window.location.reload();
						}
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
			$("p.validateTips").html("");
			$( this ).find("input").val("");
		}
	}).keyup(function(e){
		if (e.keyCode == 13) {
			$(this).dialog('option', 'buttons')['Login']();
		}
	});
	
	$( "#dialog-form-register" ).dialog({
		autoOpen: false,
		height: 570,
		width: 500,
		position: ['top', 50],
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
			$("p.validateTips").html("");
			$( this ).find(":input").val("");
			$( this ).find("textarea").val("");
		}
	});

	$("a.login").click(function(){
		nextUrl = $(this)[0].href;
		$( "#dialog-form-login" ).dialog( "open" );
		return false;
	});

	$("a.register").click(function(){
		$( "#dialog-form-register" ).dialog( "open" );
		return false;
	});

	$("#logout").click(function(){
		$.get("user/logout.action", function(){
			window.location.reload();
		});
	});

});
