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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserRequest;

public class RegisterUserServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Client c = Client.create();

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		WebResource webResource = c.resource(eurekaServicesUrl);

		String email = req.getParameter("email");
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String organziation = req.getParameter("organization");
		String password = req.getParameter("password");
		String verifyPassword = req.getParameter("verifyPassword");

		UserRequest userRequest = new UserRequest();
		userRequest.setFirstName(firstName);
		userRequest.setLastName(lastName);
		userRequest.setEmail(email);
		userRequest.setOrganization(organziation);
		userRequest.setVerifyPassword(verifyPassword);
		userRequest.setPassword(password);

		ClientResponse response = webResource.path("/api/user/add")
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, userRequest);

		System.out.println("response = " + response.getStatus());

		resp.sendRedirect(req.getContextPath() );

	}
}
