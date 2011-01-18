<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = (String)application.getAttribute("basePath");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge -- Statistics</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<script type="text/javascript" src="javascript/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="javascript/swfobject.js"></script>

	</head>

	<body>
		<s:include value="/top.jsp" />
		<s:actionerror />

		<div class="ptt">Stay Time Distribution</div>
		<br />
		
		<s:select name="unique" list="#{'0':'By session','1':'By IP'}" value="%{unique}" onchange="refresh();"></s:select>
		<br />
		
		<div style="text-align:center;">
			<div id="chart" ></div>
		</div>	

		<br /><a href="stat/listOL.action">OnlineUsers</a>
		<br /><a href="stat/listHU.action">HistoryUsers</a>
		<br /><a href="stat/toListBrowser.action">Browser</a>
		<br /><a href="stat/toListOS.action">OS</a>
		<br /><a href="stat/toShowStayTime.action">Stay Time</a>
		<br /><a href="stat/toShowDayVisits.action">30Days visits</a>

		<s:include value="/bottom.jsp" />
	</body>
	
	<script type="text/javascript">
		$(document).ready(refresh());

		function refresh(){
			var unique = $("#unique").val();
			var url = 'stat/showStayTime.action?unique=' + unique;  
			swfobject.embedSWF("open-flash-chart.swf", "chart", "1000", "600", "9.0.0", "expressInstall.swf", {"data-file":url});
		}
	</script> 

</html>
