/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;

public class SaveUserAcctWorker implements ServletWorker {

	private static Logger LOGGER = LoggerFactory.getLogger(SaveUserAcctWorker.class);
	
	private ResourceBundle messages;

	public SaveUserAcctWorker(ServletContext ctx) {
		String localizationContextName = ctx.getInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext");
		this.messages = ResourceBundle.getBundle(localizationContextName);
	}
	
	

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);

		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");
		
		// validate verifyPassword equals newPassword
		String verifyPassword = req.getParameter("verifyPassword");
		if (!verifyPassword.equals(newPassword)) {
			resp.setContentType("text/html");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		resp.setContentType("text/html");
		try {
			servicesClient.changePassword(oldPassword, newPassword);
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(HttpServletResponse.SC_OK);
		} catch (ClientException e) {
			resp.setContentType("text/plain");
			if (ClientResponse.Status.PRECONDITION_FAILED.equals(e.getResponseStatus())) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write(e.getMessage());
			} else {
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				String msg = messages.getString("passwordChange.error.internalServerError");
				String formattedMsg = MessageFormat.format(msg, "aiwhelp@emory.edu");
				resp.getWriter().write(formattedMsg);
			}
			
		}

	}
}
