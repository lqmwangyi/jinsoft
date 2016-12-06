package wechatms.handlerprocess;

import java.util.Map;

public abstract interface OperationJDBCMapKey {
	public abstract String getKey(Map<String, String> paramMap);
}