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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class SystemPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(SystemPropositionListServlet.class);

	private JsonTreeData createData(SystemElement element) {
		JsonTreeData d = new JsonTreeData();
		d.setData(this.getDisplayName(element));
		d.setKeyVal("id", element.getKey());

		// SBA - not implemented for for sub-children so I need to set it
		// manually for now.
		// if (proposition.isParent() || proposition.getChildren().size() > 0) {
		// d.setKeyVal("class", "jstree-closed");
		// }
		d.setKeyVal("class", "jstree-closed");
		d.setKeyVal("data-space", "system");
		d.setKeyVal("data-type", element.getSystemType().toString());
		d.setKeyVal("data-proposition", element.getKey());

		return d;
	}

	private String getDisplayName(SystemElement p) {
		String displayName = "";

		if (p.getAbbrevDisplayName() != null
		        && !p.getAbbrevDisplayName().equals("")) {

			displayName = p.getAbbrevDisplayName() + "(" + p.getKey() + ")";

		} else if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {

			displayName = p.getDisplayName() + "(" + p.getKey() + ")";

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
		Client client = CommUtils.getClient();
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();

		LOGGER.debug("got username {}", userName);
		WebResource webResource = client.resource(eurekaServicesUrl);
		User user = webResource.path("/api/user/byname/" + userName)
		        .accept(MediaType.APPLICATION_JSON).get(User.class);

		String propId = req.getParameter("id");

		if (propId == null) {
			throw new ServletException("Invalid parameter id: " + propId);
		}

		if (propId.equals("root")) {
			List<SystemElement> props = webResource.path(
			        "/api/proposition/system/" + user.getId() + "/list").get(
			        new GenericType<List<SystemElement>>() {
				        // Nothing to implement, used to hold returned data.
			        });
			for (SystemElement proposition : props) {
				JsonTreeData d = createData(proposition);
				l.add(d);
			}

		} else {
			String path = UriBuilder.fromPath("/").segment("api")
			        .segment("proposition").segment("system")
			        .segment("" + user.getId()).segment(propId).build()
			        .toString();
			SystemElement propWrapper = webResource.path(path)
			        .accept(MediaType.APPLICATION_JSON)
			        .get(SystemElement.class);

			for (SystemElement propChild : propWrapper.getChildren()) {
				JsonTreeData newData = createData(propChild);
				newData.setType("system");
				l.add(newData);
			}
		}

		LOGGER.debug("executed resource get");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
