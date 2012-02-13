package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct.ListUserAcctWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct.SaveUserAcctWorker;

public class UserAcctManagerServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		ServletWorker worker = null;

		if (action.equals("list")) {
			worker = new ListUserAcctWorker();
			worker.execute(req, resp);
		} 
		else if (action.equals("save")) {
			worker = new SaveUserAcctWorker();
			worker.execute(req, resp);			
			
		} else {
			
		}
	}
}
