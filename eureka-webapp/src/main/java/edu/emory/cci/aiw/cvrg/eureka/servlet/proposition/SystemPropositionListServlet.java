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

import com.sun.jersey.api.client.UniformInterfaceException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class SystemPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemPropositionListServlet.class);

	private JsonTreeData createData(SystemElement element) {
		JsonTreeData d = new JsonTreeData();
		d.setState("closed");
		d.setData(this.getDisplayName(element));
		d.setKeyVal("id", element.getKey());

		String properties = StringUtils.join(element.getProperties(), ",");
		d.setKeyVal("data-properties", properties);
		// SBA - not implemented for for sub-children so I need to set it
		// manually for now.
		// if (proposition.isParent() || proposition.getChildren().size() > 0) {
		// d.setKeyVal("class", "jstree-closed");
		// }
//		d.setKeyVal("class", "jstree-closed");
		d.setKeyVal("data-key", element.getKey());
		d.setKeyVal("data-space", "system");
		d.setKeyVal("data-type", element.getSystemType().toString());
		d.setKeyVal("data-proposition", element.getKey());

		return d;
	}

	private String getDisplayName(SystemElement p) {
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
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();
		LOGGER.debug("got username {}", userName);

		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		UserInfo user = servicesClient.getUserByName(userName);

		String propKey = req.getParameter("key");

		if (propKey == null) {
			throw new ServletException("Invalid parameter id: " + propKey);
		}
		
		try {
			if (propKey.equals("root")) {
				List<SystemElement> props = servicesClient.getSystemElements(user.getId());
				for (SystemElement proposition : props) {
					JsonTreeData d = createData(proposition);
					l.add(d);
				}
			} else {
				SystemElement element = servicesClient.getSystemElement(user
						.getId(), propKey);
				for (SystemElement propChild : element.getChildren()) {
					JsonTreeData newData = createData(propChild);
					newData.setType("system");
					l.add(newData);
				}
			}
		} catch (UniformInterfaceException e) {
			throw new ServletException("Error getting proposition list", e);
		}


		LOGGER.debug("executed resource get");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
