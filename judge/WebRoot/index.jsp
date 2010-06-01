<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
    	<base href="<%=basePath%>" />
	    <title>Virtual Judge</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="css/demo_page.css" />
		<link rel="stylesheet" type="text/css" href="css/demo_table.css" />
		<script type="text/javascript" language="javascript" src="javascript/jquery.js"></script>
		<script type="text/javascript" language="javascript" src="javascript/jquery.dataTables.js"></script>
		<script type="text/javascript" src="javascript/common.js"></script>
	</head>

	<body>
		<s:include value="/top.jsp" />
		<center>
			<h1>Virtual Judge</h1>
		</center>
		<div style="width:900px;MARGIN-RIGHT:auto;MARGIN-LEFT:auto;">
			<p>
				Virtual Judge is not a real online judge. It can grabs problems from other regular online judges and simulate submissions to other online judges.
				It aims to enable holding contests when you don't have the test data.
			</p>
			<p>
				Currently, this system supports the following online judges:<br />
				<a href="http://acm.pku.edu.cn/JudgeOnline/" target="_blank">POJ</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acm.zju.edu.cn/onlinejudge/" target="_blank">ZOJ</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acmicpc-live-archive.uva.es/nuevoportal/" target="_blank">UVALive</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acm.sgu.ru/" target="_blank">SGU</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acm.timus.ru/" target="_blank">URAL</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acm.hust.edu.cn/thx/" target="_blank">HUST</a>&nbsp;&nbsp;&nbsp;
				<a href="http://www.spoj.pl" target="_blank">SPOJ</a>&nbsp;&nbsp;&nbsp;
				<a href="http://acm.hdu.edu.cn" target="_blank">HDU</a>&nbsp;&nbsp;&nbsp;
			</p>
			<p>
				<b>Note:</b> There are some bugs in IE, Firefox is recommended.
			</p>
			<br />
			<b>Change Log:</b>
			<ul>
				<li>2010-05-04 : Put into use.</li>
				<li>2010-05-10 : Remember the last language selected.</li>
				<li>2010-05-10 : Remember the OJ of the last problem selected.</li>
				<li>2010-05-10 : Optimize the crawling of ZOJ.</li>
				<li>2010-05-10 : HDU OJ is added.</li>
			</ul>
		</div>
		<s:include value="/bottom.jsp" />
	</body>
</html>
