package jinsoftms.thread;

import jinsoftms.controller.AccessTokenController;
import jinsoftms.database.wechat.AccessToken;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.WeChatMainUtil;

public class AccessTokenThread implements Runnable {
	public static AccessToken accessToken = null;
	public static String APPID;
	public static String APPSECRET;

	 public AccessToken accessToken(){
		AccessTokenController ac = new AccessTokenController();
		if(ac.checkToken()){
			System.out.println("检查数据结果: "+ ac.getResultString());
			AccessToken access_token = new AccessToken();
			String str[]=ac.getResultString().split(",");
			access_token.setAccess_token(str[0]);
			access_token.setExpires_in(new Integer(str[1]));
			accessToken = access_token;
		}else{
			System.out.println("新建token数据");
			accessToken = WeChatMainUtil.getAccessToken(APPID, APPSECRET);
			ac.saveorupdate(accessToken);
		}
		return accessToken;
	 }

	public void run() {
		while (true) {
			try {
				AccessTokenController ac = new AccessTokenController();
				if(ac.checkToken()){
					System.out.println("检查数据结果: "+ ac.getResultString());
					AccessToken access_token = new AccessToken();
					String str[]=ac.getResultString().split(",");
					access_token.setAccess_token(str[0]);
					access_token.setExpires_in(new Integer(str[1]));
					accessToken = access_token;
				}else{
					System.out.println("新建token数据");
					accessToken = WeChatMainUtil.getAccessToken(APPID, APPSECRET);
					ac.saveorupdate(accessToken);
				}
				if (accessToken == null) {
					// 如果access_token为null，3秒后再获取
					Thread.sleep(30 * 1000);
				} else {
					// 休眠7000秒
					System.out.println("获取的时间数："+accessToken.getExpires_in()+",休眠:"+(accessToken.getExpires_in() - 20));
					Thread.sleep((accessToken.getExpires_in() - 20)*1000);
				}
				
				
				//使用properties读取token
//				StringBuffer access_token = checkPros();
//				System.out.println(access_token);
//				if (access_token == null) {
//					System.out.println("chuang" + access_token);
//					accessToken = WeChatMainUtil.getAccessToken(APPID, APPSECRET);
//					if (accessToken != null) {
//						boolean isW = writeProperties(accessToken);
//						if (isW)
//							// 休眠7000秒
//							Thread.sleep((accessToken.getExpires_in() - 200) * 1000);
//					} else {
//						// 如果access_token为null，3秒后再获取
//						Thread.sleep(30 * 1000);
//					}
//				} else {
//					System.out.println("pros" + access_token.toString());
//					accessToken.setAccess_token(access_token.toString());
//				}
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

//	/**
//	 * 更新（或插入）一对properties信息(主键及其键值) 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
//	 * 
//	 * @param keyname 键名
//	 * @param keyvalue 键值
//	 */
//	public static boolean writeProperties(AccessToken accessToken) {
//		String configPath = null;
//		if (configPath == null) {
//			configPath = AccessTokenThread.class.getResource("").getPath()
//					+ "AccessToken.properties";
//		}
//		BufferedWriter bw = null;
//		try {
//			configPath = URLDecoder.decode(configPath, "utf-8");
//			System.out.println("updateProperties propertiesPath:" + configPath);
//
//			// pros = ReadDetail.loadProperties(null);
//			// System.out.println("updateProperties old:" + pros);
//
//			// 写入属性文件
//			bw = new BufferedWriter(new OutputStreamWriter(
//					new FileOutputStream(configPath)));
//
//			pros.clear();// 清空旧的文件
//
//			pros.setProperty("access_token", accessToken.getAccess_token());
//			pros.setProperty("expires_in", accessToken.getExpires_in() + "");
//			pros.setProperty("timestamp", System.currentTimeMillis() + "");
//
//			System.out.println("updateProperties new:" + pros);
//
//			pros.store(bw, "");
//			System.out.println("配置文件录入完成");
//		} catch (IOException e) {
//			System.err.println("写入配置文件失败" + e.getMessage());
//			return false;
//		} finally {
//			try {
//				bw.close();
//			} catch (IOException e) {
//				System.err.println("关闭配置文件失败" + e.getMessage());
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private static StringBuffer checkPros() {
//		pros = getConfig(null);
//		if (pros == null) {
//			return null;
//		} else {
//			StringBuffer access_token = new StringBuffer(pros.getProperty("access_token"));
//			Long expires_in = new Long(pros.getProperty("expires_in"));
//			Long timestamp = new Long(pros.getProperty("timestamp"));
//			if (access_token == null || expires_in == null || timestamp == null) {
//				System.out.println("必须提供连接数据的参数,参数名是access_token、expires_in、timestamp");
//				return null;
//			}
//
//			long time = (System.currentTimeMillis() - timestamp) / (1000 * 60);
//			System.out.println(time);
//			if (time < 6000) {
//				return access_token;
//			} else {
//				return null;
//			}
//		}
//
//	}

	// /**
	// * 本地操作时打开
	// * @param configPath
	// * @return
	// */
	// private static String getCurrentSrcPath(){
	// String currentPath=AccessTokenThread.class.getResource("").getPath();
	// String classPath=AccessTokenThread.class.getName().replace(".", "/");
	// classPath="/"+classPath.substring(0,classPath.lastIndexOf("/"));
	// currentPath=currentPath.substring(0,currentPath.lastIndexOf(classPath));
	// int count=0;
	// File file;
	// do{
	// currentPath=currentPath.substring(0, currentPath.lastIndexOf("/"));
	// file=new File(currentPath+"/src");
	// count++;
	// }while(!file.exists()&&count<20);
	// if (count==20)
	// return null;
	// else
	// return currentPath+"/src";
	// }

//	private static Properties getConfig(String configPath) {
//		Properties pros = null;
//		if (configPath == null) {
//			configPath = AccessTokenThread.class.getResource("").getPath()
//					+ "AccessToken.properties";
//		}
//
//		File configFile = new File(configPath);
//		if (configFile.exists()) {
//			try {
//				InputStream inStream = new FileInputStream(configFile);
//				pros = new Properties();
//				pros.load(inStream);
//			} catch (Exception ex) {
//				System.out.println("无法读取配置文件");
//				ex.printStackTrace();
//			}
//		} else {
//			System.out.println(configPath + "不存在");
//		}
//		return pros;
//	}

}