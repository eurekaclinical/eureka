package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserRequest;

public class RegisterUserServlet extends HttpServlet {

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
			c = this.getClient();

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
					.accept(MediaType.TEXT_PLAIN)
					.post(ClientResponse.class, userRequest);
			
			System.out.println("Registered: " + firstName + " " + lastName);

			

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private Client getClient() throws KeyManagementException,
			NoSuchAlgorithmException {
		TrustManager trustManager = new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] inArg0,
					String inArg1) throws CertificateException {
				// nothing todo
			}

			@Override
			public void checkClientTrusted(X509Certificate[] inArg0,
					String inArg1) throws CertificateException {
				// nothing to do
			}
		};
		ClientConfig clientConfig = new DefaultClientConfig();
		SSLContext sslContext = SSLContext.getInstance("SSLv3");
		sslContext.init(null, new TrustManager[] { trustManager }, null);
		clientConfig.getProperties().put(
				HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
				new HTTPSProperties(null, sslContext));
		return new Client().create(clientConfig);
	}
}
