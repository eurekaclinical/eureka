package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class SaveUserAcctWorker implements ServletWorker {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SaveUserAcctWorker.class);
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		String id = req.getParameter("id");
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		
		// validate verifyPassword equals newPassword
		String verifyPassword = req.getParameter("verifyPassword");
		if (!verifyPassword.equals(newPassword)) {
			resp.setContentType("text/html");
			resp.setStatus(resp.SC_BAD_REQUEST);
			resp.getWriter().close();
			return;
		}
		Client c;
		try {
			c = CommUtils.getClient();

			WebResource webResource = c.resource(eurekaServicesUrl);
			
			ClientResponse response = webResource
					.path("/api/user/passwd/" + id)
					.queryParam("oldPassword", oldPassword)
					.queryParam("newPassword", newPassword)
					.get(ClientResponse.class);
			
			LOGGER.debug("status = " + response.getClientResponseStatus().getStatusCode());
			
			resp.setContentType("text/html");
			if (response.getClientResponseStatus().getStatusCode() == resp.SC_OK) {
				resp.setStatus(resp.SC_OK);				
			} else {
				resp.setStatus(resp.SC_BAD_REQUEST);								
			}
			resp.getWriter().write(response.getClientResponseStatus().getStatusCode());
			resp.getWriter().close();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//resp.sendRedirect(req.getContextPath() + "/protected/user_acct?action=list");
	}
}
