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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Phenotype;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PhenotypeField;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class ListUserDefinedPropositionChildrenServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ListUserDefinedPropositionChildrenServlet.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ServicesClient servicesClient;
	private final PropositionListSupport propListSupport;

	@Inject
	public ListUserDefinedPropositionChildrenServlet(
			ServicesClient inClient) {
		this.servicesClient = inClient;
		this.propListSupport = new PropositionListSupport();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	private JsonTreeData createData(String data, String key) {
		JsonTreeData d = new JsonTreeData();
		d.setData(data);
		d.setKeyVal("key", key);
		d.setKeyVal("data-key", key);

		return d;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private void getAllData(JsonTreeData d) throws ClientException {
		Phenotype phenotype = this.servicesClient.getUserPhenotype(d.getAttr().get("data-key"), false);

		if (phenotype.getType() == Phenotype.Type.CATEGORIZATION) {
			Category ce = (Category) phenotype;
			for (PhenotypeField de : ce.getChildren()) {
				if (de.isInSystem()) {
					JsonTreeData newData = createData(this.propListSupport.getDisplayName(de),
							de.getPhenotypeKey());
					newData.setType("system");
					LOGGER.debug("add sysTarget {}", de.getPhenotypeKey());
					d.setChildren(true);

				}
			}

			for (PhenotypeField userPhenotype : ce.getChildren()) {
				if (!userPhenotype.isInSystem()) {

					JsonTreeData newData = createData(
							userPhenotype.getPhenotypeDescription(),
							userPhenotype.getPhenotypeKey());
					getAllData(newData);
					newData.setType("user");
					LOGGER.debug("add user defined {}",
							userPhenotype.getPhenotypeKey());
					d.setChildren(true);
				}
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<JsonTreeData> l = new ArrayList<>();
		String propKey = req.getParameter("propKey");

		Phenotype phenotype;
		try {
			phenotype = this.servicesClient.getUserPhenotype(propKey, false);

			JsonTreeData newData = createData(this.propListSupport.getDisplayName(phenotype),
					propKey);
			getAllData(newData);
			l.add(newData);

			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			MAPPER.writeValue(out, l);
		} catch (ClientException ex) {
			throw new ServletException(
					"error getting user defined phenotype " + propKey, ex);
		}
	}
}
