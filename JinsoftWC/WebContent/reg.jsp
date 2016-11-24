<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
<title>认证界面</title> 
<link rel="icon" href="favicon.ico" type="image/x-icon" /> 
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" /> 
<link rel="stylesheet" href="style/weui.min.css">
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
</head>
<body>

<br>
<div class="page__hd">
    <h3 class="page__title" align="center">精软物业</h3>
</div>

<div class="weui-cells weui-cells_form">
	<div class="weui-cell">
         <div class="weui-cell__hd">
         	<label class="weui-label">手机号</label>
         </div>
         <div class="weui-cell__bd">
             <input class="weui-input" id="mobile" type="tel" placeholder="请输入手机号"/>
         </div>
         <div class="weui-cell__ft">
             <i class="weui-icon-warn"></i>
         </div>
    </div>
 	<div class="weui-cell weui-cell_vcode">
	    <div class="weui-cell__hd">
	        <label class="weui-label">验证码</label>
	    </div>
	    <div class="weui-cell__bd">
	        <input class="weui-input" type="number" pattern="[0-9][4]" value="weui input error" placeholder="请输入验证码">
	    </div>
	    <div class="weui-cell__ft">
	        <button class="weui-vcode-btn" id="vcode-btn" onClick="get_mobile_code();">获取验证码</button>
	    </div>
	</div>
	<div class="weui-cell weui-cell_vcode">
	    <div class="weui-cell__hd">
			<label for="weuiAgree" class="weui-agree">
			    <input id="weuiAgree" type="checkbox" class="weui-agree__checkbox">
			    <span class="weui-agree__text">
			        	阅读并同意<a href="javascript:void(0);">《相关条款》</a>
			    </span>
			</label>
	    </div>
    </div>
</div>

<div class="weui-btn-area">
    <a class="weui-btn weui-btn_primary" href="javascript:" id="showTooltips">确定</a>
</div>

<br>
<br>
<div class="weui-footer">
    <p class="weui-footer__text">广州市精软网络数据有限公司 All Right Reserved</p>
    <p class="weui-footer__text">Copyright ©2016 粤ICP备06034825号</p>
</div>

<!-- <form action="reg.jsp" method="post" name="formUser" onSubmit="return register();">
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
		<tr>
			<td>
				<a href="javascript:;" class="weui_btn weui_btn_primary">按钮</a>
				<a href="#" class="weui_btn weui_btn_disabled weui_btn_primary">按钮</a>
				<a href="#" class="weui_btn weui_btn_warn">确认</a>
				<a href="#" class="weui_btn weui_btn_disabled weui_btn_warn">确认</a>
				<a href="#" class="weui_btn weui_btn_default">按钮</a>
				<a href="#" class="weui_btn weui_btn_disabled weui_btn_default">按钮</a>
				<div class="button_sp_area">
				    <a href="#" class="weui_btn weui_btn_plain_default">按钮</a>
				    <a href="#" class="weui_btn weui_btn_plain_primary">按钮</a>
				
				    <a href="#" class="weui_btn weui_btn_mini weui_btn_primary">按钮</a>
				    <a href="#" class="weui_btn weui_btn_mini weui_btn_default">按钮</a>
				</div>	
			</td>
		</tr>
	</table>
</form> -->

<script language="javascript">
function get_mobile_code(){
	$.post('sms.jsp', {mobile:jQuery.trim($('#mobile').val())}, function(msg){
		RemainTime(msg);
	});
}

var Account, msgText, iTime=59;
function RemainTime(msg){
	$(msg).find("msg").each(function(index, item) {
	    msgText = $(item).text();
	});
	
	document.getElementById('vcode-btn').disable = true;
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
		sTime=msgText+"("+sSecond+")";
		if(iTime==0){
			clearTimeout(Account);
			sTime='获取验证码';
			iTime=59;
			document.getElemenById('vcode-btn').disabled = false;
		}else{
			Account = setTimeout("RemainTime()",1000);
			iTime=iTime-1;
		}
	}else{
		sTime='没有倒计时';
	}
	console.log(sTime);
	document.getElementById('vcode-btn').value = sTime;
}
</script>
</body>
</html>