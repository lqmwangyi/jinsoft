package wechatms.handlerprocess;

import bvpms.handlerProcess.BusinessLogicalFactory;
import bvpms.handlerProcess.LoginInforAction;
import bvpms.handlerProcess.ProcessEntrance;
import bvpms.util.RuntimeExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class WechatProcess extends HttpServlet {
	private static Logger log = Logger.getLogger(WechatProcess.class);
	private String BusinessPath;
	private String OperationJDBCMapKeyPath;
	private String SignatureVerificationPath;
	private String SignatureVerificationJDBC;
	private String SignatureVerificationBusinessJDBC;
	private boolean isSecurity;
	private boolean isPrint;
	private boolean isCheckConnectionType;
	private boolean isCustomOutString;
	private String LoginInforExtractorPath;
	private String SecurityWXJDBC;
	private String MainWXWebName;
	private boolean ValidateCaptcha = true;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

//		String mt = conf.getInitParameter("Validate.CAPTCHA");
//		if (mt != null) this.ValidateCaptcha = mt.equals("1");
		this.LoginInforExtractorPath = conf.getInitParameter("LoginInforExtractor.Imp");
		if (this.LoginInforExtractorPath == null)
			throw new ServletException( "还没有在web.xml文件设置LoginInforExtractor.Imp参数");
		this.SecurityWXJDBC = conf.getInitParameter("SecurityWXJDBC.name");
		if (this.SecurityWXJDBC == null)
			throw new ServletException("还没有在web.xml文件设置SecurityWXJDBC.name参数");
		this.MainWXWebName = conf.getInitParameter("MainWXWeb.name");
		if (this.MainWXWebName == null)
			throw new ServletException("还没有在web.xml文件设置MainWXWeb.name参数");
		String ValidatePermissionPath = conf.getInitParameter("ValidatePermission.Imp");

		getServletContext().setAttribute("X_Security_WXJDBC", this.SecurityWXJDBC);
		getServletContext().setAttribute("VClass", ValidatePermissionPath);
		getServletContext().setAttribute("VURL", new ConcurrentHashMap());
	}

	protected boolean mustUseSession() {
		return (this.SignatureVerificationPath == null);
	}

	protected boolean customOutString() {
		return this.isCustomOutString;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}


	protected OperationJDBCMapKey getOperationJDBCMapKey(String realClass) {
		return (OperationJDBCMapKey) BusinessLogicalFactory.createOperationJDBCMapKey(realClass);
	}

//	private LoginInforAction getPasswordExtractor(String realClass) {
//		return BusinessLogicalFactory.createLoginInforAction(realClass);
//	}

	private void process(HttpServletRequest request, HttpServletResponse reponse) {
		ProcessEntrance pe = null;
		HttpSession session = null;
		reponse.setContentType("text/html;charset=UTF-8");
		reponse.setHeader("Pragma", "No-cache");
		reponse.setHeader("Cache-Control", "no-cache");
		reponse.setDateHeader("Expires", -8356514887672791040L);
		reponse.setCharacterEncoding("UTF-8");
		try {
			if (this.ValidateCaptcha) {
				String vcode = request.getParameter("vcode");
				if ((vcode == null) || (vcode.isEmpty()))
					throw new RuntimeExceptionHandler("没有提交验证码");

				session = request.getSession(false);
				if (session == null)
					throw new RuntimeExceptionHandler("session还没有创建,不能做任何操作");

				Object CAPTCHA = session.getAttribute("VCODE_SESSION_KEY");
				if (CAPTCHA == null)
					throw new RuntimeExceptionHandler("手机器验证码已经失效");

				System.out.println("验证码校验"+ ((String) CAPTCHA));
				if (!(((String) CAPTCHA).equalsIgnoreCase(vcode)))
					throw new RuntimeExceptionHandler("验证码校验无效，请重新录入");
			}

			String username = request.getParameter("mobile");
			if ((username == null) || (username.isEmpty()))
				throw new RuntimeExceptionHandler("没有提交用户手机");

			// String username = request.getParameter("User");
			// if ((username == null) || (username.isEmpty()))
			// throw new RuntimeExceptionHandler("没有提交用户名");
			//
			// String password = request.getParameter("Password");
			// if ((password == null) || (password.isEmpty()))
			// throw new RuntimeExceptionHandler("没有提交密码");

			//LoginInforAction loginAction = getPasswordExtractor(this.LoginInforExtractorPath);
//			if (loginAction == null)
//				throw new RuntimeExceptionHandler("找不到处理登陆事件的类名称[" + this.LoginInforExtractorPath + "]");

//			pe = new ProcessEntrance(this.SecurityWXJDBC, request);
//			String dbPassword = loginAction.getPassword(username, pe.getDBEntranceInterface());
			PrintWriter pw = reponse.getWriter();
//			if ((dbPassword != null) && (!(dbPassword.isEmpty())) && (loginAction.getPasswordEncryptor().encryptPassword(password).equals(dbPassword)))
//				if (loginAction.markLoginTime(username, pe.getDBEntranceInterface())) {
//					Map operaJDBC = loginAction.getOperationJDBCMap(username, pe.getDBEntranceInterface());
//					if (session == null){
//						session = request.getSession();
//					}
//					LoginUser loginUser = loginAction.getLoginUser(username, pe.getDBEntranceInterface());
//					session.setAttribute("X_Login", loginUser);
//					session.setAttribute("X_JDBC", operaJDBC);
//					session.setAttribute("PURL", new ConcurrentHashMap());
//					if (!(loginUser.getCanDO())){
//						pw.print("{'success':false,'msg':'" + loginUser.getCanDoMsg() + "'}");
//					}else{
//						pw.print("{'success':true,'msg':'" + this.MainWebName + "'}");
//					}
//				} else {
//					pw.print("{'success':false,'msg':'记录登陆时间出现错误，登陆失败'}");
//				}
//			}else{
//				pw.print("{'success':false,'msg':'用户名或密码错误，登陆失败'}");
//			}
//			if (!(loginUser.getCanDO())){
//				pw.print("{'success':false,'msg':'" + loginUser.getCanDoMsg() + "'}");
//			}else{
//				pw.print("{'success':true,'msg':'" + this.MainWebName + "'}");
//			}
			pw.print("{'success':true,'msg':'" + this.MainWXWebName + "'}");
			pw.close();
		} catch (Exception ex) {
			try {
				PrintWriter pw = reponse.getWriter();
				pw.print("{'success':false,'msg':'" + ex.getMessage() + "'}");
				pw.close();
			} catch (Exception exx) {
				throw new RuntimeExceptionHandler(exx);
			}

			if (pe == null){
				return;
			}
			pe.closeConnection();
		} finally {
			if (pe != null){
				pe.closeConnection();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private Map<String, String> getParams(HttpServletRequest request) {
		Map params = new HashMap();
		
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String paramName = (String) e.nextElement();
			if ((!(paramName.equals("processMethod")))
					&& (!(paramName.equals("page")))
					&& (!(paramName.equals("limit")))) {
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
}
