package jinsoftms.database.wechat;

public class AccessToken {
	/**
	 	access_token	获取到的凭证
		expires_in	凭证有效时间，单位：秒
	 */
	private String access_token;
	private int expires_in;
	
	//自增字段
	private long timestamp;
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
