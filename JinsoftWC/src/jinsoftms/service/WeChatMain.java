package jinsoftms.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;
import jinsoftms.thread.AccessTokenThread;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.CheckDataUtil;
import jinsoftms.util.WeChatMainUtil;
import jinsoftms.util.MessageUtil;

public class WeChatMain extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	/**
	 * 微信接口服务地址
	 * 获取第三方应用返回用户code地址
	 */
	public static String WCHTTPSURL;
	public static String WCOPENHTTPSURL;
	/**
	 * HTTSPURL 安全模式的地址
	 * HTTPURI 明文模式的地址
	 * PROJECTNAME 项目名称
	 */
	public static String HTTPSURL;
	public static String HTTPURI;
	public static String ProjectName;
	/**
	 * 数据库名称
	 */
	public static String SecurityJDBC;
	
//	/**
//     * 第三方用户唯一凭证    
//     * 第三方用户唯一凭证密钥
//     * 获取到的凭证
//     */
//	public static String APPID;
//	public static String APPSECRET;
	/**
	 * 初始化access_token
	 */
	public void init(ServletConfig conf) throws ServletException { 
		super.init(conf);
		WCHTTPSURL = conf.getInitParameter("WCHTTPSURL");
		if (WCHTTPSURL == null) throw new ServletException("还没有在web.xml文件设置WCHTTPSURL参数");
		WCOPENHTTPSURL = conf.getInitParameter("WCOPENHTTPSURL");
		if (WCOPENHTTPSURL == null) throw new ServletException("还没有在web.xml文件设置WCOPENHTTPSURL参数");
		
		HTTPSURL = conf.getInitParameter("HTTPSURL");
		if (HTTPSURL == null) throw new ServletException("还没有在web.xml文件设置HTTPSURL参数");
		HTTPURI = conf.getInitParameter("HTTPURL");
		if (HTTPURI == null) throw new ServletException("还没有在web.xml文件设置HTTPURL参数");
		ProjectName = conf.getInitParameter("Project.name");
		if (ProjectName == null) throw new ServletException("还没有在web.xml文件设置Project.name参数");
		
		SecurityJDBC = conf.getInitParameter("SecurityJDBC.name");
		if (SecurityJDBC == null) throw new ServletException("还没有在web.xml文件设置SecurityJDBC.name参数");
		
		getServletContext().setAttribute("X_Security_JDBC", SecurityJDBC);
		
		// 获取web.xml中配置的参数    
		AccessTokenThread.APPID = conf.getInitParameter("APPID");
		if (AccessTokenThread.APPID == null) throw new ServletException("还没有在web.xml文件设置APPID参数");
		AccessTokenThread.APPSECRET = conf.getInitParameter("APPSECRET");
		if (AccessTokenThread.APPSECRET == null) throw new ServletException("还没有在web.xml文件设置APPSECRET参数");
		
		// 未配置appid、appsecret时给出提示    
		if ("".equals(AccessTokenThread.APPID) || "".equals(AccessTokenThread.APPSECRET)) {    
			throw new ServletException("还没有在web.xml文件设置APPID或APPSECRET参数");   
		} else {    
			// 启动定时获取access_token的线程    
			new Thread(new AccessTokenThread()).start();    
		}    
		
	}    
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		PrintWriter pw = resp.getWriter();
		if(CheckDataUtil.checkSignature(signature, timestamp, nonce)){
			pw.print(echostr);
		}
	}
	
	
	/**
	 *消息的接收与响应
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		setReponse(resp);
//		System.out.println(req.getSession().getAttribute("X_Security_JDBC"));
//		//IE地址栏地址
//		HTTPURL = req.getRequestURL();
//		System.out.println(HTTPURL);
//		//相对地址 	:
//		HTTPURI = req.getRequestURI();
//		System.out.println(HTTPURI);
//		//得到工程名:
//		PROJECTNAME = req.getContextPath();
//		System.out.println(HTTPURI);
		
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");	 
			String content = map.get("Content");	
			System.out.println(fromUserName+"##"+toUserName+"##"+msgType+"##"+content);
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if("0".equals(content)){
					message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.firstText());
				}else if("1".equals(content)){
					message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.secondText());
				}else if("2".equals(content)){
					message = MessageUtil.initNewsMessage(fromUserName, toUserName);
				}else if("3".equals(content)){
					message = MessageUtil.initImageMessage(fromUserName, toUserName);
				}else if("4".equals(content)){
					message = MessageUtil.initMusicMessage(fromUserName, toUserName);
				}else if("?".equals(content) || "？".equals(content)){
					message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.menuText());
				}else if("创建菜单".equals(content) || "createmenu".equalsIgnoreCase(content)){
//					AccessToken accessToken = AccessTokenThread.accessToken();
//					System.out.println("sdfa1"+accessToken.getAccess_token());
					
					String menu = JSONObject.fromObject(WeChatMainUtil.initMenu()).toString();
					String token = AccessTokenThread.accessToken.getAccess_token();
					int result = WeChatMainUtil.createMenu(token, menu);
					String msg = "";
					if(result == 0){
						msg = "创建菜单成功";
					}else{
						msg = "错误码：" + result;
					}
					message = MessageUtil.initText(fromUserName, toUserName, msg);
				}else if("删除菜单".equals(content) || "deletemenu".equalsIgnoreCase(content)){
					String token = AccessTokenThread.accessToken.getAccess_token();
					int result = WeChatMainUtil.deleteMenu(token);
					String msg = "";
					if(result == 0){
						msg = "删除菜单成功";
					}else{
						msg = "错误码：" + result;
					}
					message = MessageUtil.initText(fromUserName, toUserName, msg);
				}else  if("查询菜单".equals(content) || "querymenu".equalsIgnoreCase(content)){
//					AccessToken accessToken = AccessTokenThread.accessToken();
//					System.out.println("sdfa"+accessToken.getAccess_token());
					
					String token = AccessTokenThread.accessToken.getAccess_token();
					System.out.println("caidan");
					JSONObject jsonObject = WeChatMainUtil.queryMenu(token);
					message = MessageUtil.initText(fromUserName, toUserName, jsonObject.toString());
				}else{
					message = MessageUtil.initText(fromUserName, toUserName, "没找到需要的指令");
				}
				
//				TextMessage text = new TextMessage();
//				text.setFromUserName(toUserName);
//				text.setToUserName(fromUserName);
//				text.setMsgType("text");
//				text.setCreateTime(new Date().toString());
//				text.setContent("你发送的消息是: "+content);
//				message = Message.textMessageToXml(text);
				
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(fromUserName, toUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String token = AccessTokenThread.accessToken.getAccess_token();
					message = MessageUtil.initText(fromUserName, toUserName, token);
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					String url = map.get("EventKey");
					message = MessageUtil.initText(fromUserName, toUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
					message = MessageUtil.initText(fromUserName, toUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String label = map.get("Label");
				message = MessageUtil.initText(fromUserName, toUserName, label);
			}
			
			PrintWriter pw = resp.getWriter();
			pw.print(message);
		} catch (Exception e) {
			try {
		        PrintWriter pw = resp.getWriter();
		        pw.print("{'success':false,'msg':'" + e.getMessage() + "'}");
		        pw.close();
			} catch (Exception ex) {
				throw new RuntimeExceptionUtil(ex);
			}
		}
	}
	
	private void setReponse(HttpServletResponse reponse) {
	    reponse.setContentType("text/html;charset=UTF-8");
	    reponse.setHeader("Pragma", "No-cache");
	    reponse.setHeader("Cache-Control", "no-cache");
	    reponse.setDateHeader("Expires", -5304662868329758720L);
	    reponse.setCharacterEncoding("UTF-8");
	}
	
//	public static void main(String[] args) {
//		System.out.println(System.currentTimeMillis());
//    	AccessToken accessToken = AccessTokenThread.accessToken();
//		System.out.println("sdfa1"+accessToken.getAccess_token());
//		
//		String token = AccessTokenThread.accessToken.getAccess_token();
//		System.out.println("caidan");
//		try {
//			JSONObject jsonObject = WeChatMainUtil.queryMenu(token);
//System.out.println(jsonObject.toString());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		message = MessageUtil.initText(fromUserName, toUserName, jsonObject.toString());
//	}
}
