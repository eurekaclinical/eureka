/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
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
		try {
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
		} catch (ClientException ex) {
			switch (ex.getResponseStatus()) {
				case UNAUTHORIZED:
					this.authenticationSupport.needsToLogin(req, resp);
					break;
				default:
					resp.setStatus(ex.getResponseStatus().getStatusCode());
					resp.getWriter().write(ex.getMessage());
			}
		}
	}
}
