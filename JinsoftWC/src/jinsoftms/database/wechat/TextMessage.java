package jinsoftms.database.wechat;

public class TextMessage extends BaseMessage {
	/**
		Content	 文本消息内容
		MsgId	 消息id，64位整型
	 */
	private String Content;
	private String MsgId;
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	
}
