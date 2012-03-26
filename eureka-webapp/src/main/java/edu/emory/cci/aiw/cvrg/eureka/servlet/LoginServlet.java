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
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;

public class LoginServlet extends HttpServlet {

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
			// TODO: not currently handling invalid response.
			ClientResponse response = webResource.path("/api/user/login/" 
										+ userName+"/"+ new Date())
					.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		} catch (KeyManagementException kme) {
			throw new ServletException(kme);

		} catch (NoSuchAlgorithmException nsae) {
			throw new ServletException(nsae);
		}
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
}
