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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class LogoutServlet extends HttpServlet {

	private final WebappProperties webappProperties;

	@Inject
	public LogoutServlet(WebappProperties inProperties) {
		this.webappProperties = inProperties;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		/*
		 * We need to redirect here rather than forward so that 
		 * logout.jsp gets a request object without a user. Otherwise,
		 * the button bar will think we're still logged in.
		 */
		StringBuilder buf = new StringBuilder();
		String casLogoutUrl = webappProperties.getCasLogoutUrl();
		buf.append(casLogoutUrl);
		String awaitingActivation = req.getParameter("awaitingActivation");
		boolean aaEmpty = StringUtils.isEmpty(awaitingActivation);
		if (!aaEmpty && BooleanUtils.toBooleanObject(awaitingActivation) == null) {
			resp.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		String notRegistered = req.getParameter("notRegistered");
		boolean nrEmpty = StringUtils.isEmpty(notRegistered);
		if (!nrEmpty && BooleanUtils.toBooleanObject(notRegistered) == null) {
			resp.sendError(HttpStatus.SC_BAD_REQUEST);
			return;
		}
		if (!aaEmpty || !nrEmpty) {
			buf.append('?');
		}
		if (!aaEmpty) {
			buf.append("awaitingActivation=").append(awaitingActivation);
		}
		if (!aaEmpty && !nrEmpty) {
			buf.append('&');
		}
		if (!nrEmpty) {
			buf.append("notRegistered=").append(notRegistered);
		}
		log("URL IS " + buf.toString());
		resp.sendRedirect(buf.toString());
	}
}
