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
package edu.emory.cci.aiw.cvrg.eureka.servlet.cohort;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.BinaryOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PhenotypeField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Literal;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.authentication.WebappAuthenticationSupport;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveCohortServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SaveCohortServlet.class);
	private final ServicesClient servicesClient;
	private final WebappAuthenticationSupport authenticationSupport;

	@Inject
	public SaveCohortServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
		this.authenticationSupport = new WebappAuthenticationSupport(this.servicesClient);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		LOGGER.debug("SaveCohortServlet");
		CohortJson cohortJson = MAPPER.readValue(req.getReader(), CohortJson.class);
		CohortDestination cohortDestination = new CohortDestination();
		try {
			User user = this.authenticationSupport.getMe(req);
			cohortDestination.setId(cohortJson.getId());
			cohortDestination.setOwnerUserId(user.getId());
			cohortDestination.setName(cohortJson.getName());
			cohortDestination.setDescription(cohortJson.getDescription());
			cohortDestination.setCohort(cohortJson.toCohort());
			if (cohortDestination.getId() == null) {
				this.servicesClient.createDestination(cohortDestination);
			} else {
				this.servicesClient.updateDestination(cohortDestination);
			}
		} catch (ClientException e) {
			switch (e.getResponseStatus()) {
				case UNAUTHORIZED:
					this.authenticationSupport.needsToLogin(req, resp);
					break;
				default:
					resp.setStatus(e.getResponseStatus().getStatusCode());
					resp.getWriter().write(e.getMessage());
			}
		}
	}
	
	private static class CohortJson {
		private Long id;
		private String name;
		private String description;
		private PhenotypeField[] phenotypes;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public PhenotypeField[] getPhenotypes() {
			return phenotypes;
		}

		public void setPhenotypes(PhenotypeField[] phenotypes) {
			this.phenotypes = phenotypes;
		}
		
		private Cohort toCohort() {
			Cohort cohort = new Cohort();
			Node node;
			if (phenotypes.length == 1) {
				Literal literal = new Literal();
				literal.setName(phenotypes[0].getPhenotypeKey());
				node = literal;
			} else if (phenotypes.length > 1) {
				boolean first = true;
				Node prev = null;
				for (int i = phenotypes.length - 1; i >= 0; i--) {
					Literal literal = new Literal();
					literal.setName(phenotypes[i].getPhenotypeKey());
					if (first) {
						first = false;
						prev = literal;
					} else {
						BinaryOperator binaryOperator = new BinaryOperator();
						binaryOperator.setOp(BinaryOperator.Op.OR);
						binaryOperator.setLeftNode(literal);
						binaryOperator.setRightNode(prev);
						prev = binaryOperator;
					}
				}
				node = prev;
			} else {
				node = null;
			}
			cohort.setNode(node);
			return cohort;
		}
	}

	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}
}
