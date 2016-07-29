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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Phenotype;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class EditorHomeServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger
		(EditorHomeServlet.class);

	private final ServicesClient servicesClient;

	@Inject
	public EditorHomeServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	private JsonTreeData createData(String key, String data) {
		JsonTreeData d = new JsonTreeData();
		d.setData(data);
		d.setKeyVal("key", key);

		return d;
	}

	private String getDisplayName(Phenotype e) {
		String displayName = "";

		if (e.getDisplayName() != null && !e.getDisplayName().equals
			("")) {

			displayName = e.getDisplayName() + "(" + e.getKey() + ")";

		} else {

			displayName = e.getKey();

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

		List<JsonTreeData> l = new ArrayList<>();
		List<Phenotype> props;
		try {
			props = this.servicesClient.getUserPhenotypes(true);
		} catch (ClientException ex) {
			throw new ServletException("Error getting user-defined phenotypes", ex);
		}
                
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		for (Phenotype proposition : props) {
			JsonTreeData d = createData(
				proposition.getKey(),
				this.getDisplayName(proposition));
			d.setKeyVal("description",
				proposition.getDescription());
			d.setKeyVal("displayName", proposition.getDisplayName());
			d.setKeyVal("id", proposition.getId().toString());

			if (null != proposition.getType()) switch (proposition.getType()) {
				case CATEGORIZATION:
					d.setKeyVal("type", "Categorical");
					break;
 				case SEQUENCE:
					d.setKeyVal("type", "Sequence");
					break;
				case FREQUENCY:
					d.setKeyVal("type", "Frequency");
					break;
				case VALUE_THRESHOLD:
					d.setKeyVal("type", "Value Threshold");
					break;
				default:
					break;
			}

			if (proposition.getCreated() != null) {
				LOGGER.debug(
					"created date: " + df.format(proposition.getCreated
						()));
				d.setKeyVal("created", df.format(proposition.getCreated
					()));
			}
			if (proposition.getLastModified() != null) {
				d.setKeyVal(
					"lastModified", df.format(proposition
					.getLastModified()));
			}
			l.add(d);
			LOGGER.debug("Added user prop: " + d.getData());
		}

		req.setAttribute("props", l);
		req.getRequestDispatcher("/protected/editor_home.jsp").forward(
			req, resp);
	}
}
