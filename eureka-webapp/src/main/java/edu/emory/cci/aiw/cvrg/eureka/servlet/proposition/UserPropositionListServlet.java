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

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;


public class UserPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserPropositionListServlet.class);



	private JsonTreeData createData(PropositionWrapper proposition) {
		JsonTreeData d = new JsonTreeData();
		d.setData(proposition.getAbbrevDisplayName());
		d.setKeyVal("id", String.valueOf(proposition.getId()));

		return d;
	}

    private String getDisplayName(PropositionWrapper p) {
        String displayName = "";

        if (p.getAbbrevDisplayName() != null && !p.getAbbrevDisplayName().equals("")) {

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

		List<PropositionWrapper> props = webResource.path("/api/proposition/user/list/"+ user.getId())
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<PropositionWrapper>>() {
					// Nothing to implement, used to hold returned data.
				});
		for (PropositionWrapper proposition : props) {
			JsonTreeData d = createData(proposition);
			l.add(d);
			LOGGER.debug("Added user prop: " + d.getData());
		}
		LOGGER.debug("executed resource get");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
