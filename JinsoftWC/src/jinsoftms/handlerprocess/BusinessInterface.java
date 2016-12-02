package jinsoftms.handlerprocess;

import java.util.Map;

import jinsoftms.database.khdata.LoginUser;

public interface BusinessInterface {
	public abstract boolean process(Map<String, String> paramMap, LoginUser paramLoginUser);

	public abstract String getResultString();
}
