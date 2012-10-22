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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.EditUserWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.ListUsersWorker;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.admin.SaveUserWorker;

public class AdminManagerServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		ServletWorker worker = null;

		if (action.equals("list")) {
			worker = new ListUsersWorker();
			worker.execute(req, resp);

		} else if (action.equals("edit")) {
			worker = new EditUserWorker();
			worker.execute(req, resp);
			
		} else if (action.equals("save")) {
			worker = new SaveUserWorker();
			worker.execute(req, resp);			
			
		} else {
			
		}
	}
}
