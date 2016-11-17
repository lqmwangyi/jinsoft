package jinsoftms.database.wechat;

public class BaseMessage {
	/**
	 	ToUserName	开发者微信号、接收方帐号（收到的OpenID）
		FromUserName	 发送方帐号（一个OpenID）、开发者微信号
		CreateTime	 消息创建时间 （整型）
		MsgType	 text、news
	 */
	private String ToUserName;
	private String FromUserName;
	private long CreateTime;
	private String MsgType;
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public long getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	
}
