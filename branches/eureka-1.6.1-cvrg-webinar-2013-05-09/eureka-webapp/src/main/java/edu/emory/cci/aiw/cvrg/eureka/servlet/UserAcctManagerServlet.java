/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct.ListUserAcctWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct.SaveUserAcctWorker;

public class UserAcctManagerServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger
			(UserAcctManagerServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		ServletWorker worker;

		if (action != null && action.equals("save")) {
			LOGGER.info("Saving user");
			worker = new SaveUserAcctWorker(getServletContext());
		} else {
			LOGGER.info("Listing user");
			worker = new ListUserAcctWorker();
		}
		worker.execute(req, resp);
	}
}
