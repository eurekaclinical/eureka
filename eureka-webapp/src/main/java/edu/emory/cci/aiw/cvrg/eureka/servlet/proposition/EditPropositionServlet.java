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
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.RelatedDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EditPropositionServlet.class);

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
		List<FrequencyType> freqTypes = servicesClient.getFrequencyTypesAsc();
		FrequencyType defaultFreqType = servicesClient.getDefaultFrequencyType();
		List<TimeUnit> timeUnits = servicesClient.getTimeUnitsAsc();
		TimeUnit defaultTimeUnit = servicesClient.getDefaultTimeUnit();
		List<RelationOperator> relOps = 
				servicesClient.getRelationOperatorsAsc();
		List<RelationOperator> sequentialRelOps =
				new ArrayList<RelationOperator>();
		List<RelationOperator> contextRelOps = 
				new ArrayList<RelationOperator>();
		for (RelationOperator relOp : relOps) {
			switch (relOp.getType()) {
				case SEQUENTIAL:
					sequentialRelOps.add(relOp);
				case OVERLAPPING:
					contextRelOps.add(relOp);
					break;
				default:
					throw new AssertionError(
							"Unexpected relation type: " + relOp);
			}
		}
		RelationOperator defaultRelOp = 
				servicesClient.getDefaultRelationOperator();
		List<ThresholdsOperator> thresholdOps = servicesClient
				.getThresholdsOperators();

		List<ValueComparator> valueComparators = servicesClient
				.getValueComparatorsAsc();
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
					throw new AssertionError(
							"Unexpected threshold: " + vc.getThreshold());
			}
		}

		req.setAttribute("timeUnits", timeUnits);
		req.setAttribute("sequentialRelationOps", sequentialRelOps);
		req.setAttribute("contextRelationOps", contextRelOps);
		req.setAttribute("thresholdsOperators", thresholdOps);
		req.setAttribute("valueComparatorsUpper", valueCompsUpper);
		req.setAttribute("valueComparatorsLower", valueCompsLower);
		req.setAttribute("defaultTimeUnit", defaultTimeUnit);
		req.setAttribute("defaultRelationOp", defaultRelOp);
		req.setAttribute("frequencyTypes", freqTypes);
		req.setAttribute("defaultFrequencyType", defaultFreqType);

		if ((propKey != null) && (!propKey.equals(""))) {
			DataElement dataElement = servicesClient
					.getUserElement(user.getId(), propKey);
			PropertiesDataElementVisitor visitor = new PropertiesDataElementVisitor(
					user.getId(), servicesClient);
			try {
				LOGGER.debug("Visiting {}", dataElement.getKey());
				dataElement.accept(visitor);
			} catch (DataElementHandlingException e) {
				LOGGER.error("Visiting {}", dataElement.getKey(), e);
				throw new AssertionError("Should never happen: " + e.getMessage());
			}
			req.setAttribute("properties", visitor.getProperties());
			req.setAttribute("proposition", dataElement);
			req.setAttribute(
					"propositionType", dataElement.getType().toString());
		}

		req.getRequestDispatcher("/protected/editor.jsp").forward(req, resp);
	}

	private static final class PropertiesDataElementVisitor
			implements DataElementVisitor {
		private final Long userId;
		private final ServicesClient servicesClient;
		private final Map<String, List<String>> properties;

		private PropertiesDataElementVisitor(Long inUserId,
				ServicesClient inServicesClient) {

			this.userId = inUserId;
			this.servicesClient = inServicesClient;
			this.properties = new HashMap<String, List<String>>();
		}

		private void handleDataElementField(DataElementField inField) {
			SystemElement systemElement = this.servicesClient
					.getSystemElement(this.userId, inField.getDataElementKey());
			this.properties.put(
					inField.getDataElementKey(), systemElement.getProperties());
		}

		@Override
		public void visit(SystemElement systemElement) {
			LOGGER.debug(
					"visit system element -- {}", systemElement.getKey());
		}

		@Override
		public void visit(Category categoricalElement) {
			LOGGER.debug(
					"visit category element -- {}",
					categoricalElement.getKey());
		}

		@Override
		public void visit(Sequence sequence) {
			LOGGER.debug("visit sequence element -- {}", sequence.getKey());
			DataElementField primary = sequence.getPrimaryDataElement();
			if (primary.getType() == DataElement.Type.SYSTEM) {
				handleDataElementField(primary);
			}

			for (RelatedDataElementField field : sequence
					.getRelatedDataElements()) {
				DataElementField element = field.getDataElementField();
				if (element.getType() == DataElement.Type.SYSTEM) {
					handleDataElementField(element);
				}
			}
		}

		@Override
		public void visit(Frequency frequency) {
			LOGGER.debug("visit frequency element -- {}", frequency.getKey());
			DataElementField def = frequency.getDataElement();
			if (def.getType() == DataElement.Type.SYSTEM) {
				handleDataElementField(def);
			}
		}

		@Override
		public void visit(ValueThresholds thresholds) {
			LOGGER.debug(
					"visit threshold element -- {}", thresholds.getKey());
		}

		public Map<String, List<String>> getProperties() {
			return this.properties;
		}
	}
}
