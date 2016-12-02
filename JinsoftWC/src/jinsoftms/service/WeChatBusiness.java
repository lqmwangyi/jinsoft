package jinsoftms.service;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class register
 */
public class WeChatBusiness extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 数据库名称
	 */
	public static String SecurityJDBC;
	
    /**
     * @throws ServletException 
     * @see HttpServlet#HttpServlet()
     */
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        // TODO Auto-generated constructor stub
        SecurityJDBC = conf.getInitParameter("SecurityJDBC.name");
		if (SecurityJDBC == null) throw new ServletException("还没有在web.xml文件设置SecurityJDBC.name参数");
		
		getServletContext().setAttribute("X_Security_JDBC", SecurityJDBC);
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
	
	private void process(HttpServletRequest request, HttpServletResponse response) {
		setReponse(response);
    	HttpSession session = null;
    	
	}
	
	private void setReponse(HttpServletResponse reponse) {
	    reponse.setContentType("text/html;charset=UTF-8");
	    reponse.setHeader("Pragma", "No-cache");
	    reponse.setHeader("Cache-Control", "no-cache");
	    reponse.setDateHeader("Expires", -5304662868329758720L);
	    reponse.setCharacterEncoding("UTF-8");
	}

}
