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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.EditUserWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.ListUsersWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.SaveUserWorker;

public class AdminManagerServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!req.isUserInRole("admin")) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else {
			String action = req.getParameter("action");
			ServletWorker worker = null;
			if (action == null) {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write("The action parameter is required");
			} else if (action.equals("list")) {
				worker = new ListUsersWorker();
				worker.execute(req, resp);
			} else if (action.equals("edit")) {
				worker = new EditUserWorker();
				worker.execute(req, resp);
			} else if (action.equals("save")) {
				worker = new SaveUserWorker();
				worker.execute(req, resp);
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				resp.getWriter().write("Invalid action parameter " + action + ". Allowed values are list, edit and save.");
			}
		}
	}
}
