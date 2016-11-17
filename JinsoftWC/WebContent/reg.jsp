<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script language="javascript">
function get_mobile_code(){
	$.post('sms.jsp', {mobile:jQuery.trim($('#mobile').val())}, function(msg){
		alert(jQuery.trim(unescape(msg)));
		if(msg=='提交成功'){
			RemainTime();
		}
	});
}

var Account, iTime=59;
function RemainTime(){
	document.getElementById('zphone').disable = true;
	var iSecond,sSecond="",sTime="";
	if(iTime >= 0){
		iSecond = parseInt(iTime%60);
		iMinute = parseInt(iTime/60);
		if(iSecond >= 0){
			if(iMinute > 0){
				sSecond = iMinute + "分" + iSecond + "秒";
			}else{
				sSecond = iSecond + "秒";
			}
		}
		sTime=sSecond;
		if(iTime==0){
			clearTimeout(Account);
			sTime='获取验证码';
			iTime=59;
			document.getElemenById('zphone').disabled = false;
		}else{
			Account = setTimeout("RemainTime()",1000);
			iTime=iTime-1;
		}
	}else{
		sTime='没有倒计时';
	}
	document.getElementById('zphone').value = sTime;
}
</script>
</head>
<body>

<form action="reg.jsp" method="post" name="formUser" onSubmit="return register();">
	<table width="100%"  border="0" align="left" cellpadding="5" cellspacing="3">
		<tr>
		<td align="center">
			<input id="mobile" name="mobile" type="text" placeholder="输入你的手机号码" size="25" class="inputBg"/>
			<span style="color:#FF0000">*</span>
		</td>
		</tr>
		<tr>
		<td align="center">
			<input id="mobile_code" name="mobile_code" type="text" placeholder="输入验证码" size="13" class="inputBg"/>
			<input id="zphone" type="button" value="获取验证码" onClick="get_mobile_code();" />
		</td>
		</tr>
	</table>
</form>
</body>
</html>