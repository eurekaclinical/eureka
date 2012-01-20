package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.research.ws.wadl.Request;

public class LogoutServlet extends HttpServlet {

	private static final String CAS_LOCATION = "https://eureka.cci.emory.edu/cas/logout";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		resp.sendRedirect(CAS_LOCATION);
	}
}
