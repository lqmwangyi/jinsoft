<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ page import = "java.io.*" %><%@ page import = "java.lang.*" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>认证用户授权</title> 
<link rel="icon" href="favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" /> 
<link rel="stylesheet" href="style/weui.min.css">
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
</head>
<body>
<%
String wycode = request.getParameter("code");
PrintWriter pw = response.getWriter();
if(!wycode.isEmpty()){
	request.getSession().setAttribute("WYCODE_SESSION_KEY", wycode);
	request.getSession().setMaxInactiveInterval(300);
	System.out.println("用来获取网页token,网页授权的code:"+ wycode);
}else{
	System.out.println("没有获取到网页授权的code");
}
pw.close();
%>
	
		
<!-- <form action="wechatmain" method="post">
	FromUserName<input type="text" name="FromUserName" value="oKOi9s3bSH6hXONqmrumpVAj9wII"/><br/>
	ToUserName<input type="text" name="ToUserName" value="gh_4113098cf4b9"/><br/>
	MsgType<input type="text" name="MsgType" value="text"/><br/>
	Content<input type="text" name="Content"/><br/>
	

	<input type="submit"/>
</form> -->
</body>
</html>