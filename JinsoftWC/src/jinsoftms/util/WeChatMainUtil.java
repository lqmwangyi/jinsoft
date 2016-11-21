package jinsoftms.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import jinsoftms.database.wechat.AccessToken;
import jinsoftms.database.wechat.menu.Button;
import jinsoftms.database.wechat.menu.ClickButton;
import jinsoftms.database.wechat.menu.Menu;
import jinsoftms.database.wechat.menu.ViewButton;
import jinsoftms.service.WeChatMain;
import jinsoftms.thread.AccessTokenThread;

public class WeChatMainUtil {
//	微信公众号access_token	
	private static final String ACCESS_TOKEN_URL = WeChatMain.WCHTTPSURL+"/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
//	图片消息上传路径
	private static final String UPLOAD_URL = WeChatMain.WCHTTPSURL+"/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
//	创建菜单请求地址
	private static final String CREATE_MENU_URL = WeChatMain.WCHTTPSURL+"/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
//	查询菜单请求地址
	private static final String QUERY_MENU_URL = WeChatMain.WCHTTPSURL+"/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
//	删除菜单请求地址
	private static final String DELETE_MENU_URL = WeChatMain.WCHTTPSURL+"/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
//	获取第三方应用返回用户code地址(用户同意授权，获取code)
	private static final String BIND_USER_URL = WeChatMain.WCOPENHTTPSURL+"/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	/**
	 * get请求
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httGet);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		}
		return jsonObject;
	}
	
	/**
	 * post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostStr(String url,String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(),"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		}
		return jsonObject;
	}
	
	/**
	 * 获取access_token
	 * @return
	 */
	public static AccessToken getAccessToken(String APPID,String APPSECRET){
		AccessToken token = new AccessToken();
		//改变access_token有效时间,存储在AccessToken.properties文件提供使用
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null){
			token.setAccess_token(jsonObject.getString("access_token"));
			token.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	/**
	 * 新建临时消息 有效时间3天
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static String upload(String filePath,String accessToken,String type) throws IOException{
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new IOException("文件不存在");
		}
		String url = UPLOAD_URL.replace("ACCESS_TOKEN",accessToken).replace("TYPE", type);
		
		URL urlObj = new URL(url);
		
		//链接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		//忽略缓存
		con.setUseCaches(false);
		
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		
		//设置边界
		String BOUNDARY = "------------" +System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
		
		StringBuffer sb = new StringBuffer();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition;form-data;name=\"file\";filename=\""+file.getName()+"\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		
		byte[] head = sb.toString().getBytes("utf-8");
		
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		
		//文件正文部分
		//把文件以流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while((bytes = in.read(bufferOut)) != -1){
			out.write(bufferOut,0,bytes);
		}
		
		in.close();
		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线
		
		out.write(foot);
		
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		
		try{
			//定义BufferReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null){
				buffer.append(line);
			}
			
			if(result == null){
				result = buffer.toString();
			}
		}catch(IOException e){
			throw new RuntimeExceptionUtil(e.getMessage());
		}finally{
			if(reader != null){
				reader.close();
			}
		}
		
		JSONObject jsonObj = JSONObject.fromObject(result);
		
		String typeName ="media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	/**
	 * 菜单组装
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static Menu initMenu() throws ParseException, IOException{
		Menu menu = new Menu();
//		ClickButton buildingDynamicClickButton = new ClickButton();
//		buildingDynamicClickButton.setName("楼盘动态");
//		buildingDynamicClickButton.setType("click");
//		buildingDynamicClickButton.setKey("BuildingDynamic");
		
		ClickButton companyClickButton = new ClickButton();
		companyClickButton.setName("公司介绍");
		companyClickButton.setType("click");
		companyClickButton.setKey("Company");
		
		ClickButton newBusinessClickButton = new ClickButton();
		newBusinessClickButton.setName("最新业务");
		newBusinessClickButton.setType("click");
		newBusinessClickButton.setKey("NewBusiness");
		
		ClickButton buildingDetailClickButton = new ClickButton();
		buildingDetailClickButton.setName("楼盘详情");
		buildingDetailClickButton.setType("click");
		buildingDetailClickButton.setKey("BuildingDetail");
		
		Button buildingDynamicButton = new Button();
		buildingDynamicButton.setName("楼盘动态");
		buildingDynamicButton.setSub_button(new Button[]{companyClickButton,newBusinessClickButton,buildingDetailClickButton});
		
//		ClickButton clientMessageClickButton = new ClickButton();
//		clientMessageClickButton.setName("客户信息");
//		clientMessageClickButton.setType("click");
//		clientMessageClickButton.setKey("ClientMessage");
		
		ViewButton roomViewButton = new ViewButton();
		roomViewButton.setName("房产信息");
		roomViewButton.setType("view");
		roomViewButton.setUrl(WeChatMain.HTTPURI + WeChatMain.ProjectName + "/mobile/repair/sevice.html");
		
		ClickButton  scancodeClickButton = new ClickButton();
		scancodeClickButton.setName("扫码事件");
		scancodeClickButton.setType("scancode_push");
		scancodeClickButton.setKey(" Scancode");
		
		Button clientMessageButton = new Button();
		clientMessageButton.setName("客户信息");
		clientMessageButton.setSub_button(new Button[]{roomViewButton,scancodeClickButton});
		
//		ClickButton clientServiceClickButton = new ClickButton();
//		clientServiceClickButton.setName("客户服务");
//		clientServiceClickButton.setType("click");
//		clientServiceClickButton.setKey("ClientService");
		
		ViewButton bindUserServiceViewButton = new ViewButton();
		bindUserServiceViewButton.setName("认证页面");
		bindUserServiceViewButton.setType("view");
		bindUserServiceViewButton.setUrl(bindUserCode());
		
		
		ViewButton repairServiceViewButton = new ViewButton();
		repairServiceViewButton.setName("维修服务");
		repairServiceViewButton.setType("view");
		repairServiceViewButton.setUrl(WeChatMain.HTTPURI + WeChatMain.ProjectName + "/mobile/repair/sevice.html");
		
		ClickButton accountPayClickButton = new ClickButton();
		accountPayClickButton.setName("欠费缴纳");
		accountPayClickButton.setType("click");
		accountPayClickButton.setKey("AccountPay");
		
//		ClickButton locationClickButton = new ClickButton();
//		locationClickButton.setName("地理位置");
//		locationClickButton.setType("location_select");
//		locationClickButton.setKey("Location");
	
		ViewButton loginServiceViewButton = new ViewButton();
		loginServiceViewButton.setName("个人主界面");
		loginServiceViewButton.setType("view");
		loginServiceViewButton.setUrl(WeChatMain.HTTPURI + WeChatMain.ProjectName + "/person.html");
		
		Button clientServiceButton = new Button();
		clientServiceButton.setName("客户服务");
		clientServiceButton.setSub_button(new Button[]{bindUserServiceViewButton,repairServiceViewButton,accountPayClickButton,loginServiceViewButton});
	
		menu.setButton(new Button[]{buildingDynamicButton,clientMessageButton,clientServiceButton});
		return menu;
	}
	
	
	public static String bindUserCode() throws ParseException,IOException{
		String url = BIND_USER_URL.replace("APPID", AccessTokenThread.APPID).
				replace("REDIRECT_URI", WeChatMain.HTTPSURL + WeChatMain.ProjectName + "/reg.jsp")
				.replace("SCOPE", "snsapi_userinfo").replace("STATE", "1");
		return url;
	}
	
	public static int createMenu(String access_token,String menu) throws ParseException,IOException{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject queryMenu(String access_token) throws ParseException,IOException{
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	public static int deleteMenu(String access_token) throws ParseException,IOException{
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = doGetStr(url);
		int result = 0;
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
}
