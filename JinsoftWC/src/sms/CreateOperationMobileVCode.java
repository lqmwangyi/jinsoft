package sms;

import jinsoftms.handlerprocess.OperationMoblieVCode;

public class CreateOperationMobileVCode implements OperationMoblieVCode{
	private static int mobile_code = 0;
	
	public int getMoblie_VCode() {
		if(Check_Moblie_VCode(mobile_code)){
			mobile_code = (int)((Math.random()*9+1)*100000);
		}
		System.out.println("取到的验证码："+mobile_code);
		return mobile_code;
	}

	public boolean Check_Moblie_VCode(int vcode){
		System.out.println(mobile_code+"old and new"+vcode);
		if(vcode==0){
			return false;
		}
		if(mobile_code!=vcode){
			return false;
		}
		return true;
	}
}
