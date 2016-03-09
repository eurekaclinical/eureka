package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigParams;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemPhenotype;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.protempa.PropositionDefinition;
import org.protempa.proposition.PropositionUtil;

public class SearchSystemPropositionJSTreeV3Servlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Pattern PATTERN = Pattern.compile("[^a-zA-Z0-9]");
	private final ServicesClient servicesClient;

	@Inject
	public SearchSystemPropositionJSTreeV3Servlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private JsonTreeData createData(SystemPhenotype phenotype) {
		JsonTreeData d = new JsonTreeData();
		d.setState("closed");
		d.setId(phenotype.getKey());
		d.setData(this.getDisplayName(phenotype));
		d.setText(this.getDisplayName(phenotype));
		d.setKeyVal("id", phenotype.getKey());

		String properties = StringUtils.join(phenotype.getProperties(), ",");
		d.setKeyVal("data-properties", properties);
		d.setKeyVal("data-key", phenotype.getKey());
		d.setKeyVal("data-space", "system");
		d.setKeyVal("data-type", phenotype.getSystemType().toString());
		d.setKeyVal("data-proposition", phenotype.getKey());
		d.setChildren(phenotype.isInternalNode());

		return d;
	}

	private String getDisplayName(SystemPhenotype p) {
		String displayName = "";

		if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {
			displayName = p.getDisplayName() + " (" + p.getKey() + ")";

		} else {

			displayName = p.getKey();

		}

		return displayName;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String searchKey = req.getParameter("str");

		List<JsonTreeData> l = new ArrayList<>();
		if (searchKey == null) {
			throw new ServletException("Search key is null");
		}

		try {

			List<SystemPhenotype> props = servicesClient
					.getSystemPhenotypeSearchResultsBySearchKey(searchKey);

			for (SystemPhenotype proposition : props) {
				JsonTreeData d = createData(proposition);
				l.add(d);
			}

			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			MAPPER.writeValue(out, l);

		} catch (ClientException e) {
			throw new ServletException("Error getting search results", e);
		}


	}


}
