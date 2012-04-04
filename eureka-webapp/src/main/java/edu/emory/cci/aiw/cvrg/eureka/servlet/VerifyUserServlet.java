package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;

/**
 * Servlet to handle user verification requests.
 * 
 * @author sagrava
 * @author hrathod
 * 
 */
public class VerifyUserServlet extends HttpServlet {

	/**
	 * Used for serialization/deserialization.
	 */
	private static final long serialVersionUID = -737043484641381552L;
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VerifyUserServlet.class);

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

			String code = req.getParameter("code");

			ClientResponse response = webResource
					.path("/api/user/verify/" + code)
					.accept(MediaType.TEXT_PLAIN).post(ClientResponse.class);

			int status = response.getClientResponseStatus().getStatusCode();
			if (status >= HttpServletResponse.SC_BAD_REQUEST) {

				String msg = response.getEntity(String.class);
				req.setAttribute("error", msg);
				LOGGER.debug("Error: {}", msg);
			}
			req.getRequestDispatcher("/registration_info.jsp").forward(req,
					resp);

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
