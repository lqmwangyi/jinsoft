package wechatms.handlerprocess;

public abstract interface OperationMoblieVCode {
	public abstract int getMoblie_VCode();
	public abstract boolean Check_Moblie_VCode(int vcode);
}
