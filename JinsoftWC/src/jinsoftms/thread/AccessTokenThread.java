package jinsoftms.thread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import jinsoftms.database.wechat.AccessToken;
import jinsoftms.service.WeChatMain;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.WeChatMainUtil;


public class AccessTokenThread implements Runnable {
	private static Properties pros = null;
    public static AccessToken accessToken = null;
    
    public void run() {    
        while (true) {    
            try {  
            	String access_token = checkPros();
        		if(access_token != null){
        			accessToken.setAccess_token(access_token);
        		}else{
	                accessToken = WeChatMainUtil.getAccessToken(WeChatMain.APPID,WeChatMain.APPSECRET);    
	                if (accessToken != null) {
	                	boolean isW = writeProperties(accessToken);
	                	if(isW)
		                    // 休眠7000秒    
		                    Thread.sleep((accessToken.getExpires_in() - 200) * 1000);    
	                } else {    
	                    // 如果access_token为null，3秒后再获取    
	                    Thread.sleep(30 * 1000);    
	                }   
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
    
    /**
     * 更新（或插入）一对properties信息(主键及其键值)
     * 如果该主键已经存在，更新该主键的值；
     * 如果该主键不存在，则插件一对键值。
     * @param keyname 键名
     * @param keyvalue 键值
     */
     public static boolean writeProperties(AccessToken accessToken) {       
		pros = getConfig(null);
		if (pros == null){
			return false;
		}else{
			String srcPath=pros.getProperty("srcPath");
			if (srcPath==null){
				System.out.println("必须提供src路径,参数名是srcPath");
				return false;
			}
			if (srcPath.equals(".")){
				srcPath=getCurrentSrcPath();
				if (srcPath==null){
					System.out.println("在当前的工作目录下无法找到src目录");
					return false;
				}
			}
			try{
				// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
				// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
				OutputStream fos = new FileOutputStream(srcPath);
				pros.setProperty("access_token", accessToken.getAccess_token());
				pros.setProperty("expires_in", accessToken.getExpires_in()+"");
				
				// 以适合使用 load 方法加载到 Properties 表中的格式，
				// 将此 Properties 表中的属性列表（键和元素对）写入输出流
				pros.store(fos, "Update '" + accessToken.getAccess_token() + "' value");
				pros.store(fos, "Update '" + accessToken.getExpires_in() + "' value");
				return true;
			} catch (IOException e) {
				System.err.println("属性文件更新错误");
				return false;
			}
		}
     }
    
    private static String checkPros(){
    	pros = getConfig(null);
		if (pros==null){
			return null;
		}else{
			String srcPath=pros.getProperty("srcPath");
			if (srcPath==null){
				System.out.println("必须提供src路径,参数名是srcPath");
				return null;
			}
			if (srcPath.equals(".")){
				srcPath=getCurrentSrcPath();
				if (srcPath==null){
					System.out.println("在当前的工作目录下无法找到src目录");
					return null;
				}
			}
			String access_token=pros.getProperty("access_token");
			String expires_in=pros.getProperty("expires_in");
			String timestamp=pros.getProperty("timestamp");
			if (access_token==null||expires_in==null||timestamp==null){
				System.out.println("必须提供连接数据的参数,参数名是access_token、expires_in、timestamp");
				return null;
			}
			
			long time=(System.currentTimeMillis()-Long.parseLong(timestamp)) / (1000 * 60);
			
			if(Long.parseLong(expires_in)>time){
				return access_token;	
			}else{
				return null;
			}
		}
		
	}
	
	private static String getCurrentSrcPath(){
	    String currentPath=AccessTokenThread.class.getResource("").getPath();
	    String classPath=AccessTokenThread.class.getName().replace(".", "/");
	    classPath="/"+classPath.substring(0,classPath.lastIndexOf("/"));
	    currentPath=currentPath.substring(0,currentPath.lastIndexOf(classPath));
	    int count=0;
	    File file;
	    do{
	    	currentPath=currentPath.substring(0, currentPath.lastIndexOf("/"));
	    	file=new File(currentPath+"/src");
	    	count++;
	    }while(!file.exists()&&count<20);
	    if (count==20)
	       return null;
	    else
	       return currentPath+"/src";
	}
	
	private static Properties getConfig(String configPath){
		Properties pros =null;
		if (configPath==null){
		    configPath=AccessTokenThread.class.getResource("").getPath()+"AccessToken.properties";
		}
		
        File configFile=new File(configPath);
        if (configFile.exists()){
           try{
	         	InputStream inStream=new FileInputStream(configFile);
	           	pros=new Properties();
	           	pros.load(inStream);
           }catch(Exception ex){
           		System.out.println("无法读取配置文件");
           		ex.printStackTrace();
           }
        }else{
        	System.out.println(configPath+"不存在");
        }
    	return pros;
	}	
    
}
