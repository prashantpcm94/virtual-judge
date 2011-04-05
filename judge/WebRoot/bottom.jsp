<%@ page language="java" import="java.util.Date" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" type="text/css" href="css/global.css" />
<link rel="shortcut icon" href="images/logo.ico" />
<div style="text-align:center;margin-top:30px;clear:both;font-size:80%">
	<hr />FAQ | About Virtual Judge | <a href="http://acm.hust.edu.cn/forum/forumdisplay.php?fid=10">Forum</a> | <a href="mailto:is.un@qq.com">Contact</a> | <a href="http://code.google.com/p/virtual-judge/">Open Source Project</a><br />
	All Copyright Reserved ©2010-2011 <a href="http://acm.hust.edu.cn">HUST ACM/ICPC</a> TEAM
	<s:if test="#session.visitor.sup == 1"><a href="stat/listOL.action"><img style="text-decoration: none;" height="15px" border="0" src="images/statistics.gif" /></a></s:if>
	<br>Anything about the OJ, please ask in the <a href="http://acm.hust.edu.cn/forum/forumdisplay.php?fid=10">forum</a>, or contact author:<a href="mailto:is.un@qq.com">Isun</a><br>
	Server Time: <span class="currentTime"></span>
</div>
<script type="text/javascript" src="javascript/common.js"></script>
<script type="text/javascript">
	var timeDiff = <%= new Date().getTime()%> - new Date().valueOf();
	
	function updateTime() {
		$(".currentTime").html(new Date(new Date().valueOf() + timeDiff).format("yyyy-MM-dd hh:mm:ss"));
	}
	updateTime();
	setInterval(updateTime, 1000);

	var _gaq = _gaq || [];
	_gaq.push(['_setAccount', 'UA-13231844-2']);
	_gaq.push(['_trackPageview']);
	
	(function() {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	})();
</script>
