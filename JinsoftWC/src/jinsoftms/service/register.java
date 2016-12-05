package jinsoftms.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jinsoftms.database.khdata.LoginUser;
import jinsoftms.handlerprocess.BusinessInterface;
import jinsoftms.handlerprocess.DBEntranceInterface;
import jinsoftms.util.RuntimeExceptionUtil;
import jinsoftms.util.SessionRuntimeExceptionHandler;
import jinsoftms.util.factory.ProcessFactory;

/**
 * Servlet implementation class register
 */
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 数据库名称
	 */
	public static String SecurityJDBCNAME;
	
	/**
	 * 短信验证码
	 * 账号 
	 * 密码
	 * 密码支持md5加密吗
	 */
	public static String SMSACCOUNT;
	public static String SMSPASSWORD;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        // TODO Auto-generated constructor stub
//        SecurityJDBCNAME = conf.getInitParameter("SecurityJDBC.name");
//		if (SecurityJDBCNAME == null) throw new ServletException("还没有在web.xml文件设置SecurityJDBC.name参数");
		SMSACCOUNT = conf.getInitParameter("sms.account");
	  	if (SMSACCOUNT == null) throw new ServletException("还没有在web.xml文件设置sms.account参数");
	  	SMSPASSWORD = conf.getInitParameter("sms.password");
	  	if (SMSPASSWORD == null) throw new ServletException("还没有在web.xml文件设置sms.password参数");
//		getServletContext().setAttribute("X_Security_JDBC", SecurityJDBCNAME);
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, resp);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void process(HttpServletRequest request, HttpServletResponse response) {
		try {
			setReponse(response);
			Map params = null;	
			
//	    	HttpSession session = getSession(request);
//	    	LoginUser loginUser = getLoginUser(session);
	    	
	    	String processPath = request.getServletPath().substring(1);
	    	System.out.println("方法名称路径"+processPath);
	    	
	    	BusinessInterface bi = getBusinessLogical(processPath);
	    	//获取网络参数
	    	params = getParams(request);
	    	
	    	PrintWriter pw = response.getWriter();
	    	if(bi.process(params, null)){
	    		pw.print(bi.getResultString());
	    	}
		} catch (Exception e) {
			try {
		        PrintWriter pw = response.getWriter();
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
	
	public BusinessInterface getBusinessLogical(String realClass){
	    return ProcessFactory.createBusinessLogical(realClass);
	}
	
	protected HttpSession getSession(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session == null) throw new SessionRuntimeExceptionHandler("session还没有创建,不能做任何操作");
	    return session;
	}

	protected LoginUser getLoginUser(HttpSession session) {
		Object obj = session.getAttribute("X_Login");
		if (obj == null)
			throw new SessionRuntimeExceptionHandler("还没有登录不能做任何操作");

		return ((LoginUser)obj);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, String> getParams(HttpServletRequest request) {
	    Map params = new HashMap();

	    Enumeration e = request.getParameterNames();
	    while (e.hasMoreElements()) {
	      String paramName = (String)e.nextElement();
	      if ((!(paramName.equals("processMethod"))) && 
	        (!(paramName.equals("page"))) && 
	        (!(paramName.equals("limit")))) {
	        String[] paramValues = request.getParameterValues(paramName);
	        if ((paramValues != null) && (paramValues.length >= 1))
	          if (paramValues.length > 1) {
	            StringBuffer sbv = new StringBuffer(paramValues[0]);
	            for (int i = 1; i < paramValues.length; ++i)
	              sbv.append(",").append(paramValues[i]);

	            params.put(paramName, sbv.toString());
	          } else {
	            params.put(paramName, paramValues[0]);
	          }
	        else
	          params.put(paramName, null);
	      }
	    }

	    return params;
	}
	
	protected void doResultStr(boolean isOK, PrintWriter pw, HttpServletResponse resp, BusinessInterface bli, DBEntranceInterface dbe) {
		
	    if (customOutString()) {
	    	pw.print(bli.getResultString());
	    } else if (isOK) {
	    	String resultstr = bli.getResultString();
	    	if ((resultstr == null) || (resultstr.equals(""))){
		        if (dbe.getOutStringUseDoubleQuote()){
		          pw.print("{\"success\":true}");
		        }else{
		          pw.print("{'success':true}");
		        }
	    	} else if (dbe.getOutStringUseDoubleQuote()){
		        pw.print("{\"success\":true," + bli.getResultString() + "}");
	    	} else{
		        pw.print("{'success':true," + bli.getResultString() + "}");
	    	}
	    } else if (dbe.getOutStringUseDoubleQuote()) {
	    	pw.print("{\"success\":false,\"msg\":\"" + bli.getResultString() + "\"}");
	    } else {
	    	pw.print("{'success':false,'msg':'" + bli.getResultString() + "'}");
	    }

	    pw.close();
	}
	
	protected boolean customOutString() {
    	return false;
	}
}
