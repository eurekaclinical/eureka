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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;

public class ListUserDefinedPropositionChildrenServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
	        .getLogger(ListUserDefinedPropositionChildrenServlet.class);

	private WebResource webResource;

	private String getDisplayName(DataElement p) {
		String displayName = "";

		if (p.getAbbrevDisplayName() != null
		        && !p.getAbbrevDisplayName().equals("")) {

			displayName = p.getAbbrevDisplayName();

		} else if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {

			displayName = p.getDisplayName();

		} else {

			displayName = p.getKey();

		}

		return displayName;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		String eurekaServicesUrl = config.getServletContext().getInitParameter(
		        "eureka-services-url");

		Client client;

		client = CommUtils.getClient();
		this.webResource = client.resource(eurekaServicesUrl);

	}

	private JsonTreeData createData(String data, String id) {
		JsonTreeData d = new JsonTreeData();
		d.setData(data);
		d.setKeyVal("id", id);

		return d;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		doGet(req, resp);
	}

	private void getAllData(JsonTreeData d) {
		DataElement dataElement = webResource
		        .path("/api/proposition/user/get/" + d.getId())
		        .accept(MediaType.APPLICATION_JSON).get(DataElement.class);
		LOGGER.debug("got propWrapper {}", dataElement.getId());

		if (dataElement.getType() == DataElement.Type.CATEGORIZATION) {
			CategoricalElement ce = (CategoricalElement) dataElement;
			for (DataElement de : ce.getChildren()) {
				if (de.isInSystem()) {

					JsonTreeData newData = createData(de.getKey(), de.getKey());
					newData.setType("system");
					LOGGER.debug("add sysTarget {}", de.getKey());
					d.addNodes(newData);

				}
			}

			for (DataElement userDataElement : ce.getChildren()) {
				if (!userDataElement.isInSystem()) {

					JsonTreeData newData = createData(
					        userDataElement.getAbbrevDisplayName(),
					        String.valueOf(userDataElement.getId().longValue()));
					getAllData(newData);
					newData.setType("user");
					LOGGER.debug("add user defined {}", userDataElement.getId());
					d.addNodes(newData);
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {

		List<JsonTreeData> l = new ArrayList<JsonTreeData>();
		String propId = req.getParameter("propId");

		DataElement dataElement = webResource
		        .path("/api/proposition/user/get/" + propId)
		        .accept(MediaType.APPLICATION_JSON).get(DataElement.class);

		JsonTreeData newData = createData(this.getDisplayName(dataElement),
		        propId);
		getAllData(newData);
		l.add(newData);

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, l);
	}
}
