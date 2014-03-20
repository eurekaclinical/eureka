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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUser;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import edu.emory.cci.aiw.cvrg.eureka.webapp.authentication.WebappAuthenticationSupport;

public class ListUserAcctWorker implements ServletWorker {

	private final ServicesClient client;
	private final WebappAuthenticationSupport authenticationSupport;

	public ListUserAcctWorker(ServicesClient inClient) {
		this.client = inClient;
		this.authenticationSupport = new WebappAuthenticationSupport(this.client);
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		User user = this.authenticationSupport.getMe(req);
		Date now = new Date();
		Date expiration = user instanceof LocalUser ? ((LocalUser) user).getPasswordExpiration() : null;
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
