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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class EditUserServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		Client c = Client.create();
		
		// TODO: change hardcoding to init param in web.xml
		WebResource webResource = c.resource("http://localhost:8181/eureka-services");
		User user = webResource.path("/api/user/"+id)
				.accept(MediaType.APPLICATION_JSON)
				.get(User.class);
		
		webResource = c.resource("http://localhost:8181/eureka-services");
		List<Role> roles = webResource.path("/api/role/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Role>>() {
					// Nothing to implement, used to hold returned data.
				});

		req.setAttribute("roles", roles);
		req.setAttribute("user", user);
		req.getRequestDispatcher("/protected/edit_user.jsp").forward(req, resp);
	}
}
