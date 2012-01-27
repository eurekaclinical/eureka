package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class UserManager extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		ServletWorker worker = null;

		if (action.equals("list")) {
			worker = new ListUsersWorker();
			worker.execute(req, resp);

		} else if (action.equals("edit")) {
			worker = new EditUserWorker();
			worker.execute(req, resp);
			
		} else if (action.equals("save")) {
			worker = new SaveUserWorker();
			worker.execute(req, resp);
			
		} else if (action.equals("register")) {
			worker = new RegisterUserWorker();
			worker.execute(req, resp);
			
		} else {
			
		}
	}
}
