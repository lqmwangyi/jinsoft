package sms;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import jinsoftms.AbstractBusiness;
import jinsoftms.database.khdata.LoginUser;
import jinsoftms.util.RuntimeExceptionUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.chainsaw.Main;
import org.dom4j.Document;   
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;   
import org.dom4j.Element;   

public class sendsms extends AbstractBusiness{
	
	private static String Url = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";
//	private static String account = "cf_jingsoft";
//	private static String password = "23192319";
//	private static int mobile_code;
	
	public boolean send(int mobile_code, String mobile, String account, String password) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		//int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		System.out.println("验证码 "+mobile_code);
		
	    String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。"); 

		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", account), 
			    new NameValuePair("password", password), //密码可以使用明文密码或使用32位MD5加密
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


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
						
			this.resultstr = msg;
			 if("2".equals(code)){
//				this.resultstr="短信提交成功";
				return true;
			}
			return false;
		} catch (HttpException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		} catch (DocumentException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		}	
		
	}
	
	public boolean process(Map<String, String> paramMap, LoginUser paramLoginUser) {
		System.out.println("进入手机发送");
		if(paramMap.containsKey("account")){
			this.resultstr="没有设置公司手机验证码账号，请重新设置!";
			return false;
		}
		
		if(paramMap.containsKey("password")){
			this.resultstr="没有设置公司手机验证码账号，请重新设置!";
			return false;
		}
		
		if(paramMap.containsKey("mobile")){
			this.resultstr="没有设置公司手机验证码账号，请重新设置!";
			return false;
		}
		
		if(paramMap.containsKey("vcode")){
			this.resultstr="没有设置公司手机验证码账号，请重新设置!";
			return false;
		}
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
			
		//client.getParams().setContentCharset("GBK");		
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

		
		//int mobile_code = (int)((Math.random()*9+1)*100000);

		//System.out.println(mobile);
		System.out.println("验证码 "+paramMap.get("vcode"));
		
	    String content = new String("您的验证码是：" + paramMap.get("vcode") + "。请不要把验证码泄露给其他人。"); 

	    System.out.println(paramMap.get("account")+"## "+paramMap.get("password")+ "### "+paramMap.get("mobile"));
		NameValuePair[] data = {//提交短信
			    new NameValuePair("account", paramMap.get("account")), 
			    new NameValuePair("password", paramMap.get("password")), //密码可以使用明文密码或使用32位MD5加密
			    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
			    new NameValuePair("mobile", paramMap.get("mobile")), 
			    new NameValuePair("content", content),
		};
		
		method.setRequestBody(data);		
		
		
		try {
			client.executeMethod(method);	
			
			String SubmitResult =method.getResponseBodyAsString();
					
			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult); 
			Element root = doc.getRootElement();


			String code = root.elementText("code");	
			String msg = root.elementText("msg");	
			String smsid = root.elementText("smsid");	
			
			
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
			
			this.resultstr = msg;			
			 if("2".equals(code)){
//				System.out.println("短信提交成功");
				return true;
			}
			return false;
		} catch (HttpException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		} catch (IOException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		} catch (DocumentException e) {
			throw new RuntimeExceptionUtil(e.getMessage());
		}	
	}
	
	public static void main(String[] args) {
		String str = "1234567890";
		char[] ch = new char[4];
		Random random = new Random();
		for (int i = 0; i < 4; ++i) {
			ch[i] = str.charAt(random.nextInt(str.length()));
		}
		String rand = new String(ch);
		System.out.println(rand);
	}
}