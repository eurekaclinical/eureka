/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;

import org.eurekaclinical.eureka.client.comm.Category;
import org.eurekaclinical.eureka.client.comm.Phenotype;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import javax.inject.Singleton;

@Singleton
public class UserPropositionListServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserPropositionListServlet.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final long serialVersionUID = 1L;
	private final Injector injector;
	private final PropositionListSupport propListSupport;

	@Inject
	public UserPropositionListServlet(Injector inInjector) {
		this.injector = inInjector;
		this.propListSupport = new PropositionListSupport();
	}

	private JsonTreeData createData(Phenotype element) {
		JsonTreeData d = new JsonTreeData();
		d.setData(this.propListSupport.getDisplayName(element));
		d.setText(this.propListSupport.getDisplayName(element));
		d.setKeyVal("id", String.valueOf(element.getId()));
		d.setKeyVal("data-key", element.getKey());
		d.setKeyVal("data-space", "user");
		d.setKeyVal("data-type", element.getType().toString());
		if (element.getType() == Phenotype.Type.CATEGORIZATION) {
			d.setKeyVal("data-subtype", ((Category) element)
					.getCategoricalType().toString());
		}

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

		ServicesClient servicesClient = this.injector.getInstance(ServicesClient.class);
		List<Phenotype> props;
		try {
			props = servicesClient.getUserPhenotypes(false);
		} catch (ClientException ex) {
			throw new ServletException("Error getting user-defined phenotype list", ex);
		}
		List<JsonTreeData> l = new ArrayList<>(props.size());
		for (Phenotype proposition : props) {
			JsonTreeData d = createData(proposition);
			l.add(d);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Added user prop: {}", d.getData());
			}
		}

		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		MAPPER.writeValue(out, l);
	}
}
