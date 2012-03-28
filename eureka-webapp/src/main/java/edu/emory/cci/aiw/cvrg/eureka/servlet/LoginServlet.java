package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class LoginServlet extends HttpServlet {

	private static final String GET_BY_NAME_URL = "/api/user/byname/";
	private static final String PUT_USER_URL = "/api/user/put";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		Client client;
		try {
			client = CommUtils.getClient();
			Principal principal = req.getUserPrincipal();
			String userName = principal.getName();

			WebResource webResource = client.resource(eurekaServicesUrl);
			User user = webResource.path(GET_BY_NAME_URL + userName)
					.accept(MediaType.APPLICATION_JSON).get(User.class);
			user.setLastLogin(new Date());
			ClientResponse response = webResource.path(PUT_USER_URL)
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.TEXT_PLAIN)
					.put(ClientResponse.class, user);
			if (response.getClientResponseStatus() != Status.OK) {
				throw new ServletException(
						"Could not update user, got response "
								+ response.getClientResponseStatus().toString());
			}
		} catch (KeyManagementException kme) {
			throw new ServletException(kme);

		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		}
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
