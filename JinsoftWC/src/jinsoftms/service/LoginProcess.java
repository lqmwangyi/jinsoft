package jinsoftms.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jinsoftms.util.RuntimeExceptionUtil;

public class LoginProcess extends HttpServlet
{
  private String LoginInforExtractorPath;
  private String SecurityJDBC;
  private String MainWebName;
  private boolean ValidateCaptcha = true;

  public void init(ServletConfig conf)
    throws ServletException
  {
    super.init(conf);
    String mt = conf.getInitParameter("Validate.CAPTCHA");
    if (mt != null) this.ValidateCaptcha = mt.equals("1");
    this.LoginInforExtractorPath = conf.getInitParameter("LoginInforExtractor.Imp");
    if (this.LoginInforExtractorPath == null) throw new ServletException("还没有在web.xml文件设置LoginInforExtractor.Imp参数");
    this.SecurityJDBC = conf.getInitParameter("SecurityJDBC.name");
    if (this.SecurityJDBC == null) throw new ServletException("还没有在web.xml文件设置SecurityJDBC.name参数");
    this.MainWebName = conf.getInitParameter("MainWeb.name");
    if (this.MainWebName == null) throw new ServletException("还没有在web.xml文件设置MainWeb.name参数");
    String ValidatePermissionPath = conf.getInitParameter("ValidatePermission.Imp");

    getServletContext().setAttribute("X_Security_JDBC", this.SecurityJDBC);
    getServletContext().setAttribute("VClass", ValidatePermissionPath);
    getServletContext().setAttribute("VURL", new ConcurrentHashMap());
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    process(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    process(req, resp);
  }

  private LoginInforAction getPasswordExtractor(String realClass) {
    return BusinessLogicalFactory.createLoginInforAction(realClass);
  }

  private void process(HttpServletRequest request, HttpServletResponse reponse)
  {
    ProcessEntrance pe = null;
    HttpSession session = null;
    reponse.setContentType("text/html;charset=UTF-8");
    reponse.setHeader("Pragma", "No-cache");
    reponse.setHeader("Cache-Control", "no-cache");
    reponse.setDateHeader("Expires", -2111278027177459712L);
    reponse.setCharacterEncoding("UTF-8");
    try {
      if (this.ValidateCaptcha) {
        String vcode = request.getParameter("VCode");
        if ((vcode == null) || (vcode.isEmpty()))
          throw new RuntimeExceptionUtil("没有提交验证码");

        session = request.getSession(false);
        if (session == null)
          throw new RuntimeExceptionUtil("session还没有创建,不能做任何操作");

        Object CAPTCHA = session.getAttribute("KAPTCHA_SESSION_KEY");
        if (CAPTCHA == null)
          throw new RuntimeExceptionUtil("服务器验证码已经失效");

        if (!(((String)CAPTCHA).equalsIgnoreCase(vcode)))
          throw new RuntimeExceptionUtil("验证码校验无效，请重新录入");
      }

      String username = request.getParameter("User");
      if ((username == null) || (username.isEmpty()))
        throw new RuntimeExceptionUtil("没有提交用户名");

      String password = request.getParameter("Password");
      if ((password == null) || (password.isEmpty()))
        throw new RuntimeExceptionUtil("没有提交密码");

      LoginInforAction loginAction = getPasswordExtractor(this.LoginInforExtractorPath);
      if (loginAction == null)
        throw new RuntimeExceptionUtil("找不到处理登陆事件的类名称[" + this.LoginInforExtractorPath + "]");

      pe = new ProcessEntrance(this.SecurityJDBC, request);
      String dbPassword = loginAction.getPassword(username, pe.getDBEntranceInterface());
      PrintWriter pw = reponse.getWriter();
      if ((dbPassword != null) && (!(dbPassword.isEmpty())) && (loginAction.getPasswordEncryptor().encryptPassword(password).equals(dbPassword)))
        if (loginAction.markLoginTime(username, pe.getDBEntranceInterface())) {
          Map operaJDBC = loginAction.getOperationJDBCMap(username, pe.getDBEntranceInterface());
          if (session == null)
            session = request.getSession();

          LoginUser loginUser = loginAction.getLoginUser(username, pe.getDBEntranceInterface());
          session.setAttribute("X_Login", loginUser);
          session.setAttribute("X_JDBC", operaJDBC);
          session.setAttribute("PURL", new ConcurrentHashMap());
          if (!(loginUser.getCanDO()))
            pw.print("{'success':false,'msg':'" + loginUser.getCanDoMsg() + "'}");
          else
            pw.print("{'success':true,'msg':'" + this.MainWebName + "'}");
        }
        else {
          pw.print("{'success':false,'msg':'记录登陆时间出现错误，登陆失败'}");
        }
      else
        pw.print("{'success':false,'msg':'用户名或密码错误，登陆失败'}");

      pw.close();
    } catch (Exception ex) {
      try {
        PrintWriter pw = reponse.getWriter();
        pw.print("{'success':false,'msg':'" + ex.getMessage() + "'}");
        pw.close();
      } catch (Exception exx) {
        throw new RuntimeExceptionUtil(exx);
      }

      if (pe == null) return; pe.closeConnection(); } finally { if (pe != null) pe.closeConnection();
    }
  }
}