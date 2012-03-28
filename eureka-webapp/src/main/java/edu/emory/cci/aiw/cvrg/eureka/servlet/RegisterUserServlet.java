package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;

public class RegisterUserServlet extends HttpServlet {

	private static Logger LOGGER = LoggerFactory.getLogger(RegisterUserServlet.class);
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Client c;
		try {
			c = CommUtils.getClient();

			String eurekaServicesUrl = req.getSession().getServletContext()
					.getInitParameter("eureka-services-url");

			WebResource webResource = c.resource(eurekaServicesUrl);

			String email 		= req.getParameter("email");
			String verifyEmail 	= req.getParameter("verifyEmail");
			String firstName 	= req.getParameter("firstName");
			String lastName 	= req.getParameter("lastName");
			String organziation = req.getParameter("organization");
			String password 	= req.getParameter("password");
			String verifyPassword = req.getParameter("verifyPassword");

			UserRequest userRequest = new UserRequest();
			userRequest.setFirstName(firstName);
			userRequest.setLastName(lastName);
			userRequest.setEmail(email);
			userRequest.setVerifyEmail(verifyEmail);
			userRequest.setOrganization(organziation);
			userRequest.setVerifyPassword(verifyPassword);
			userRequest.setPassword(password);

			ClientResponse response = webResource.path("/api/user/add")
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.TEXT_PLAIN)
					.post(ClientResponse.class, userRequest);
			
			int status = response.getClientResponseStatus().getStatusCode();
			if (status < HttpServletResponse.SC_BAD_REQUEST) {
				resp.setStatus(status);				
				resp.getWriter().close();
			} else {
				resp.setContentType("text/plain");
				String msg = response.getEntity(String.class);
				LOGGER.debug("Error: {}", msg);
				resp.setStatus(status);
				resp.setContentLength(msg.length());
				resp.getWriter().write(msg);
				resp.getWriter().close();
			}
			

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
