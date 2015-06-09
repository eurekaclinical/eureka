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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class SystemPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemPropositionListServlet.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ServicesClient servicesClient;
	private final PropositionListSupport propListSupport;

	@Inject
	public SystemPropositionListServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
		this.propListSupport = new PropositionListSupport();
	}

	private JsonTreeData createData(SystemElement element) {
		JsonTreeData d = new JsonTreeData();
		d.setState("closed");
		d.setId(element.getKey());
		d.setData(this.propListSupport.getDisplayName(element));
		d.setText(this.propListSupport.getDisplayName(element));
		d.setKeyVal("id", element.getKey());

		String properties = StringUtils.join(element.getProperties(), ",");
		d.setKeyVal("data-properties", properties);

		d.setKeyVal("data-key", element.getKey());
		d.setKeyVal("data-space", "system");
		d.setKeyVal("data-type", element.getSystemType().toString());
		d.setKeyVal("data-proposition", element.getKey());
		d.setChildren(element.isInternalNode());

		return d;
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
		String propKey = req.getParameter("key");

		if (propKey == null) {
			throw new ServletException("Invalid proposition id: " + propKey);
		}
		List<JsonTreeData> l;
		try {
			if (propKey.equals("root")) {
				List<SystemElement> props = this.servicesClient.getSystemElements();
				l = new ArrayList<>(props.size());
				for (SystemElement proposition : props) {
					JsonTreeData d = createData(proposition);
					l.add(d);
				}
			} else {
				SystemElement element = this.servicesClient.getSystemElement(propKey, false);
				List<SystemElement> children = element.getChildren();
				l = new ArrayList<>(children.size());
				for (SystemElement propChild : children) {
					JsonTreeData newData = createData(propChild);
					newData.setType("system");
					l.add(newData);
				}
			}
		} catch (ClientException e) {
			throw new ServletException("Error getting proposition list", e);
		}


		LOGGER.debug("executed resource get");

		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		MAPPER.writeValue(out, l);
	}
}
