package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class ListUsersWorker implements ServletWorker {

	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Client c = Client.create();
		// TODO: change hardcoding to init param in web.xml
		WebResource webResource = c.resource("http://localhost:8181/eureka-services");
		List<User> users = webResource.path("/api/user/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<User>>() {
					// Nothing to implement, used to hold returned data.
				});
		req.setAttribute("users", users);
		req.getRequestDispatcher("/protected/admin.jsp").forward(req, resp);
	}
}
