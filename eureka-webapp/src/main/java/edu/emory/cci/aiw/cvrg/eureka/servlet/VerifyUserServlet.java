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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserRequest;

public class VerifyUserServlet extends HttpServlet {

	private static Logger LOGGER = LoggerFactory.getLogger(VerifyUserServlet.class);
	
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

			String code 		= req.getParameter("code");


			ClientResponse response = webResource.path("/api/user/verify/"+code)
					.accept(MediaType.TEXT_PLAIN)
					.post(ClientResponse.class);
			
			int status = response.getClientResponseStatus().getStatusCode();
			if (status >= HttpServletResponse.SC_BAD_REQUEST) {

				String msg = response.getEntity(String.class);
				req.setAttribute("error", msg);
				LOGGER.debug("Error: {}", msg);
			}
			req.getRequestDispatcher("/registration_info.jsp").forward(req, resp);
			

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
