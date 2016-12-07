package jinsoftms;

import wechatms.handlerprocess.BusinessInterface;

public abstract class AbstractBusiness implements BusinessInterface {
	
	protected String resultstr;
	
	public String getResultString() {
		return resultstr;
	}
}
