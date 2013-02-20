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
package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class UserPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(UserPropositionListServlet.class);

	private JsonTreeData createData(DataElement element) {
		JsonTreeData d = new JsonTreeData();
		d.setData(getDisplayName(element));
		d.setKeyVal("id", String.valueOf(element.getId()));

		d.setKeyVal("data-key", element.getKey());
		d.setKeyVal("data-space", "user");
		d.setKeyVal("data-type", element.getType().toString());
		if (element.getType() == DataElement.Type.CATEGORIZATION) {
			d.setKeyVal("data-subtype", ((Category) element)
			        .getCategoricalType().toString());
		}

		return d;
	}

	private String getDisplayName(DataElement p) {
		String displayName = "";

		if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {

			displayName = p.getDisplayName() + " (" + p.getKey() + ")";

		} else {

			displayName = p.getKey();

		}

		return displayName;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

		LOGGER.debug("doGet");
		List<JsonTreeData> l = new ArrayList<JsonTreeData>();
		String eurekaServicesUrl = req.getSession().getServletContext()
		        .getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();
		User user = servicesClient.getUserByName(userName);

		List<DataElement> props = servicesClient.getUserElements(user.getId());
		for (DataElement proposition : props) {
			if (!proposition.isInSystem()) {
				JsonTreeData d = createData(proposition);
				l.add(d);
				LOGGER.debug("Added user prop: " + d.getData());
			}
		}
		LOGGER.debug("executed resource get");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
