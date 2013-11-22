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
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class ListUserAcctWorker implements ServletWorker {

	private final ServicesClient servicesClient;

	public ListUserAcctWorker (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();
		UserInfo user;
		try {
			user = this.servicesClient.getUserByName(userName);
		} catch (ClientException ex) {
			throw new ServletException("Error getting user " + userName, ex);
		}

		Date now = Calendar.getInstance().getTime();
		Date expiration = user.getPasswordExpiration();
		String passwordExpiration;
		if (expiration != null && now.after(expiration)) {
			passwordExpiration = "Your password has expired. Please change it below.";
		} else {
			passwordExpiration = "";
		}
		req.setAttribute("passwordExpiration", passwordExpiration);

		req.setAttribute("user", user);
		req.getRequestDispatcher("/protected/acct.jsp").forward(req, resp);
	}
}
