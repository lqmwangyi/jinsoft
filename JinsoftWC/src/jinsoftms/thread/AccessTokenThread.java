package jinsoftms.thread;

import org.apache.log4j.Logger;

import jinsoftms.database.wechat.AccessToken;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.WeChatMainUtil;


public class AccessTokenThread implements Runnable {

    /**
     * 第三方用户唯一凭证    
     * 第三方用户唯一凭证密钥
     * 获取到的凭证
     */
	public static String APPID;
	public static String APPSECRET;
    public static AccessToken accessToken = null;
    private static Logger log = Logger.getLogger(AccessTokenThread.class);
    
    public void run() {    
        while (true) {    
            try {    
                accessToken = WeChatMainUtil.getAccessToken(APPID,APPSECRET);    
                if (null != accessToken) {    
                    log.info("accessToken: "+ accessToken.getAccess_token());  
                    // 休眠7000秒    
                    Thread.sleep((accessToken.getExpires_in() - 200) * 1000);    
                } else {    
                    // 如果access_token为null，3秒后再获取    
                    Thread.sleep(30 * 1000);    
                }    
            } catch (InterruptedException e) {    
                try {    
                    Thread.sleep(30 * 1000);    
                } catch (InterruptedException ex) {    
                	throw new RuntimeExceptionUtil(ex.getMessage()); 
                }    
                throw new RuntimeExceptionUtil(e.getMessage());   
            }    
        }    
    }    
}
