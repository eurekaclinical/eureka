package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
                /*
                 * We need to redirect here rather than forward so that 
                 * logout.jsp gets a request object without a user. Otherwise,
                 * the button bar will think we're still logged in.
                 */
                resp.sendRedirect(req.getContextPath() + "/logout.jsp");
	}
}
