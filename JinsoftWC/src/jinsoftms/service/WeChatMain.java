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

	
	/**
	 * 1、关于https
	 * 2、access_token最好保存起来 解决一半
	 * 3、xml 支持CDATA 解决
	 * 
	 * http://jingsoft.tunnel.mobi/jinsoftwechat/main jinsoft
	 * http://jinsoft.tunnel.mobi/WeChat/main jinsoft
	 * http://mrjinglan.sinaapp.com/ weixin
	 * 
	 * ngrok -config ngrok.cfg -subdomain jinsoft 8011
	 * http://www.tunnel.mobi/
	 */
	private static final long serialVersionUID = 1L;  
	
	private String SecurityHttpURL;
	private String SecurityJDBC;
	/**
	 * 初始化access_token
	 */
	public void init(ServletConfig conf) throws ServletException { 
		super.init(conf);
		this.SecurityHttpURL = conf.getInitParameter("HTTPURL.name");
		if (this.SecurityHttpURL == null) throw new ServletException("还没有在web.xml文件设置HTTPURL.name参数");
		this.SecurityJDBC = conf.getInitParameter("SecurityJDBC.name");
		if (this.SecurityJDBC == null) throw new ServletException("还没有在web.xml文件设置SecurityJDBC.name参数");
		
		getServletContext().setAttribute("X_Security_JDBC", this.SecurityJDBC);
		getServletContext().setAttribute("X_Security_HttpURL", this.SecurityHttpURL);
		
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
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");	 
			String content = map.get("Content");	
			
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
				}else if("创建菜单".equals(content) || "CreateMenu".equalsIgnoreCase(content)){
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
	

}
