package wechatms.handlerprocess;

import java.util.Map;

import bvpms.handlerProcess.LoginUser;

public abstract interface BusinessInterface {
	
	public abstract boolean process(Map<String, String> paramMap, LoginUser paramLoginUser);

	public abstract String getResultString();
}
