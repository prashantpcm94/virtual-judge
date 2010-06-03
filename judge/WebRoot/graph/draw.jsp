<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'draw.jsp' starting page</title>
    
	<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
	<script type="text/javascript" language="javascript" src="javascript/swfobject.js"></script>
	
	<script type="text/javascript" language="javascript">
		$(document).ready(function(){
			var url = 'graph/draw.action';
			swfobject.embedSWF("open-flash-chart.swf", "chart", "900", "900", "9.0.0", "expressInstall.swf", {"data-file":url});
		});
	</script> 

  </head>
  
	<body>
		<div id="chart"></div>
	</body>
</html>
