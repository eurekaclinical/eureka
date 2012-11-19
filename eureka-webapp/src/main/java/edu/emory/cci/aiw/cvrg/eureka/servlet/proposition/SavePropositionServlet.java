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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement.Type;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

public class SavePropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(SavePropositionServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

		LOGGER.debug("SavePropositionServlet");
		String id = req.getParameter("id");
		String propositions = req.getParameter("proposition");
		String type = req.getParameter("type");
		String name = req.getParameter("name");
		String description = req.getParameter("description");

		String eurekaServicesUrl = req.getSession().getServletContext()
		        .getInitParameter("eureka-services-url");

		ObjectMapper mapper = new ObjectMapper();
		List<UserProposition> props = null;
		try {
			props = mapper.readValue(propositions,
			        new TypeReference<List<UserProposition>>() {
			        });
		} catch (Exception e) {
			e.printStackTrace();
		}

		Client client = CommUtils.getClient();
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();

		WebResource webResource = client.resource(eurekaServicesUrl);
		User user = webResource.path("/api/user/byname/" + userName)
		        .accept(MediaType.APPLICATION_JSON).get(User.class);

		DataElement element;

		if (type.equals("CATEGORIZATION")) {
			List<DataElement> children = new ArrayList<DataElement>(
			        props.size());
			element = new CategoricalElement();
			element.setType(DataElement.Type.CATEGORIZATION);
			for (UserProposition userProposition : props) {
				System.out.println(userProposition.getId());
				LOGGER.debug(userProposition.getId());

				DataElement child;
				if (userProposition.getType().equals("system")) {
					child = new SystemElement();
					child.setInSystem(true);
					child.setKey(userProposition.getId());
				} else {
					if (userProposition.getType().equals("categorical")) {
						child = new CategoricalElement();
					} else { // if
							 // (userProposition.getType().equals("sequence")) {
						child = new Sequence();
					}
					child.setId(Long.valueOf(userProposition.getId()));
				}
				child.setSummarized(true);
				children.add(child);

			}
			((CategoricalElement) element).setChildren(children);

		} else { // if (type.equals("SEQUENCE")) {
			element = new Sequence();
			element.setType(DataElement.Type.SEQUENCE);
		}
		// } else if (type.equals("FREQUENCY")) {
		// pw.setType(DataElement.Type.FREQUENCY);
		// } else {
		// pw.setType(DataElement.Type.VALUE_THRESHOLD);
		// }

		if (id != null && !id.equals("")) {
			element.setId(Long.valueOf(id));
		}

		element.setAbbrevDisplayName(name);
		element.setInSystem(false);

		element.setAbbrevDisplayName(name);
		element.setDisplayName(description);
		element.setUserId(user.getId());

		ClientResponse response = webResource
		        .path("/api/proposition/user/validate/" + user.getId())
		        .type(MediaType.APPLICATION_JSON)
		        .post(ClientResponse.class, element);

		int status = response.getClientResponseStatus().getStatusCode();
		if (status != HttpServletResponse.SC_OK) {

			String msg = response.getEntity(String.class);
			req.setAttribute("error", msg);
			LOGGER.debug("Error: {}", msg);
		}

		if (element.getId() != null) {
			webResource.path("/api/proposition/user/update")
			        .type(MediaType.APPLICATION_JSON)
			        .accept(MediaType.TEXT_PLAIN)
			        .put(ClientResponse.class, element);

		} else {
			if (element.getType() == Type.SEQUENCE) {
				webResource.path("/api/proposition/user/create/sequence")
				        .type(MediaType.APPLICATION_JSON)
				        .accept(MediaType.TEXT_PLAIN)
				        .post(ClientResponse.class, element);
			}
		}

	}
}
