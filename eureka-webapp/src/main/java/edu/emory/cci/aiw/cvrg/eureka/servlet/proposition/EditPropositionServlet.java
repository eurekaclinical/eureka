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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import java.util.ArrayList;

public class EditPropositionServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
        String propKey = req.getParameter("key");

		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();

		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		User user = servicesClient.getUserByName(userName);
		List<TimeUnit> timeUnits = servicesClient.getTimeUnits();
		List<RelationOperator> operators = servicesClient
				.getRelationOperators();
		List<ThresholdsOperator> thresholdOps = 
				servicesClient.getThresholdsOperators();
		
		List<ValueComparator> valueComparators = 
				servicesClient.getValueComparatorsAsc();
		List<ValueComparator> valueCompsUpper = new ArrayList<ValueComparator>();
		List<ValueComparator> valueCompsLower = new ArrayList<ValueComparator>();
		for (ValueComparator vc : valueComparators) {
			switch (vc.getThreshold()) {
				case BOTH:
					valueCompsUpper.add(vc);
					valueCompsLower.add(vc);
					break;
				case UPPER_ONLY:
					valueCompsUpper.add(vc);
					break;
				case LOWER_ONLY:
					valueCompsLower.add(vc);
					break;
				default:
					throw new AssertionError("Invalid threshold: " + vc.getThreshold());
			}
		}

		req.setAttribute("timeUnits", timeUnits);
		req.setAttribute("operators", operators);
		req.setAttribute("thresholdsOperators", thresholdOps);
		req.setAttribute("valueComparatorsUpper", valueCompsUpper);
		req.setAttribute("valueComparatorsLower", valueCompsLower);

		if ((propKey != null) && (!propKey.equals(""))) {
			DataElement dataElement = servicesClient.getUserElement
					(user.getId(), propKey);
			req.setAttribute("proposition", dataElement);
			req.setAttribute("propositionType", 
					dataElement.getType().toString());
		}

		req.getRequestDispatcher("/protected/editor.jsp").forward(req, resp);
	}
}
