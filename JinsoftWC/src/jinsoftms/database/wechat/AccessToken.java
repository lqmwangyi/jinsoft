package jinsoftms.database.wechat;

import jinsoftms.thread.AccessTokenThread;

public class AccessToken {
	/**
	 	access_token	获取到的凭证
		expires_in	凭证有效时间，单位：秒
	 */
	private String access_token;
	private int expires_in;
	
	public String getAccess_token() {
		if(access_token==null){
			// 启动定时获取access_token的线程   
			new Thread(new AccessTokenThread()).start(); 
		}
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
}
