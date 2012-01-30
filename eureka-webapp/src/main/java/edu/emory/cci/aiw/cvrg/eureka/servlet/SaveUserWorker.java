package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

public class SaveUserWorker extends AbstractWorker {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		String id = req.getParameter("id");
		String activeStatus = req.getParameter("active");
		System.out.println("activeStatus: " + activeStatus);
		boolean isActivated = false;

		if (activeStatus != null) {

			isActivated = true;

		}
		System.out.println("active status: " + isActivated);
		Client c;
		try {
			c = this.getClient();
			System.out.println("id = " + id);

			WebResource webResource = c.resource(eurekaServicesUrl);
			User user = webResource.path("/api/user/" + id)
					.accept(MediaType.APPLICATION_JSON).get(User.class);
			String[] roles = req.getParameterValues("role");
			List<Role> userRoles = new ArrayList<Role>();
			for (String roleId : roles) {
				Role role = webResource.path("/api/role/" + roleId)
						.accept(MediaType.APPLICATION_JSON).get(Role.class);
				userRoles.add(role);
				System.out.println("role = " + roleId);
			}
			user.setRoles(userRoles);
			user.setActive(isActivated);
			webResource = c.resource(eurekaServicesUrl);
			ClientResponse response = webResource.path("/api/user/put")
					.type(MediaType.APPLICATION_JSON)
					.put(ClientResponse.class, user);
			System.out.println("response = " + response.getStatus());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		resp.sendRedirect(req.getContextPath() + "/protected/user?action=list");
	}
}
