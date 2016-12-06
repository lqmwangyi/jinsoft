package wechatms.handlerprocess;

import bvpms.util.RuntimeExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class VCode extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 短信验证码 账号 密码 密码支持md5加密吗
	 */
	private String SMSACCOUNT;
	private String SMSPASSWORD;
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		this.SMSACCOUNT = conf.getInitParameter("sms.account");
		if (this.SMSACCOUNT == null)
			throw new ServletException("还没有在web.xml文件设置手机验证码账号sms.account参数");
		this.SMSPASSWORD = conf.getInitParameter("sms.password");
		if (this.SMSPASSWORD == null)
			throw new ServletException("还没有在web.xml文件设置手机验证码密码sms.password参数");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) {
		String vcode = (String) request.getSession().getAttribute(
				"VCODE_SESSION_KEY");
		if ((vcode == null) || (vcode.equals(""))) {
			String str = "1234567890";
			char[] ch = new char[4];
			Random random = new Random();
			for (int i = 0; i < 4; ++i) {
				ch[i] = str.charAt(random.nextInt(str.length()));
			}
			vcode = new String(ch);
			request.getSession().setAttribute("VCODE_SESSION_KEY", vcode);
		}
		if ((vcode != null) && (!(vcode.equals("")))) {
//			if(!params.containsKey("account")){
//				 throw new RuntimeExceptionHandler("没有设置公司手机验证码账号，请重新设置!");
//			}
//			
//			if(!params.containsKey("password")){
//				throw new RuntimeExceptionHandler("没有设置公司手机验证码密码，请重新设置!");
//			}
//			
//			if(!params.containsKey("vcode")){
//				throw new RuntimeExceptionHandler("没有提交需要验证的验证码!");
//			}
			String msg = "";
			String mobile = request.getParameter("mobile");
			System.out.println("需要验证的手机号"+ mobile);
			if(mobile=="" || mobile.equals("") || mobile.isEmpty()){
				 msg = ("没有提交需要验证的手机号!");
			}
			
			
			HttpClient client = new HttpClient(); 
			PostMethod method = new PostMethod(Url); 
				
			//client.getParams().setContentCharset("GBK");		
			client.getParams().setContentCharset("UTF-8");
			method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

			
			//int mobile_code = (int)((Math.random()*9+1)*100000);

			System.out.println("验证码 "+ vcode);
			
		    String content = new String("您的验证码是：" + vcode + "。请不要把验证码泄露给其他人。"); 

		    System.out.println(this.SMSACCOUNT+"## "+this.SMSPASSWORD+ "### "+ mobile);
			NameValuePair[] data = {//提交短信
				    new NameValuePair("account", this.SMSACCOUNT), 
				    new NameValuePair("password", this.SMSPASSWORD), //密码可以使用明文密码或使用32位MD5加密
				    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
				    new NameValuePair("mobile", mobile), 
				    new NameValuePair("content", content),
			};
			
			method.setRequestBody(data);		
			
			
			try {
				client.executeMethod(method);	
				
				String SubmitResult =method.getResponseBodyAsString();
						
				//System.out.println(SubmitResult);

				Document doc = DocumentHelper.parseText(SubmitResult); 
				Element root = doc.getRootElement();


				msg = root.elementText("msg");	
				String code = root.elementText("code");	
				String smsid = root.elementText("smsid");	
				
				
				System.out.println(code);
				System.out.println(msg);
				System.out.println(smsid);
				response.setContentType("text/html;charset=UTF-8");
				response.setHeader("Pragma", "No-cache");
		    	response.setHeader("Cache-Control", "no-cache");
			    response.setDateHeader("Expires", -8356514887672791040L);
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				if("2".equals(code)){
					pw.print("{'success':true,'msg':'" + msg + "'}");
				}else{
					pw.print("{'success':false,'msg':'" + msg + "'}");
				}
				pw.close();
			} catch (HttpException e) {
				throw new RuntimeExceptionHandler(e.getMessage());
			} catch (IOException e) {
				throw new RuntimeExceptionHandler(e.getMessage());
			} catch (DocumentException e) {
				throw new RuntimeExceptionHandler(e.getMessage());
			}
		}
	}

}
