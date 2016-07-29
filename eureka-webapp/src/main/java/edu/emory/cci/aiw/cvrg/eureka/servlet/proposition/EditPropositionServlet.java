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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.*;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.PhenotypeHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPropositionServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EditPropositionServlet.class);
	private final ServicesClient servicesClient;

	@Inject
	public EditPropositionServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String propKey = req.getParameter("key");
		Phenotype.Type propType = this.getPropTypeFromParam(req.getParameter("type"));
		try {
			List<FrequencyType> freqTypes = this.servicesClient.getFrequencyTypesAsc();
			//Find the default frequency
			FrequencyType defaultFreqType = null;
			for (FrequencyType freqType : freqTypes){
    				if(freqType.isDefault()){
        				defaultFreqType = freqType;
        				break;
    				}
			}
			List<TimeUnit> timeUnits = this.servicesClient.getTimeUnitsAsc();                     
			//Find the default timeUnit
			TimeUnit defaultTimeUnit = null;
			for (TimeUnit timeUnit : timeUnits){
    				if(timeUnit.isDefault()){
        				defaultTimeUnit = timeUnit;
        				break;
    				}
			}    
			List<RelationOperator> relOps =
					this.servicesClient.getRelationOperatorsAsc();
			List<RelationOperator> sequentialRelOps =
					new ArrayList<>();
			List<RelationOperator> contextRelOps =
					new ArrayList<>();
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
                        //Find the default relationOp
			RelationOperator defaultRelOp = null;
			for (RelationOperator relOp : relOps){
				if(relOp.isDefault()){
					defaultRelOp = relOp;
					break;
				}
			}       		
			List<ThresholdsOperator> thresholdOps = this.servicesClient
					.getThresholdsOperators();

			List<ValueComparator> valueComparators = this.servicesClient
					.getValueComparatorsAsc();
			List<ValueComparator> valueCompsUpper = new ArrayList<>();
			List<ValueComparator> valueCompsLower = new ArrayList<>();
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
				Phenotype phenotype = this.servicesClient
						.getUserPhenotype(propKey, false);
				PropertiesPhenotypeVisitor visitor = new PropertiesPhenotypeVisitor(
						this.servicesClient);
				try {
					LOGGER.debug("Visiting {}", phenotype.getKey());
					phenotype.accept(visitor);
				} catch (PhenotypeHandlingException e) {
					LOGGER.error("Visiting {}", phenotype.getKey(), e);
					throw new ServletException("Error getting phenotype properties", e.getCause());
				}
				req.setAttribute("properties", visitor.getProperties());
				req.setAttribute("proposition", phenotype);
				req.setAttribute(
						"propositionType", phenotype.getType().toString());
				propType = phenotype.getType();
			}

			String jsp = null;
			switch (propType) {
				case FREQUENCY:
					jsp = "frequency";
					break;
				case CATEGORIZATION:
					jsp = "categorization";
					break;
				case SEQUENCE:
					jsp = "sequence";
					break;
				case VALUE_THRESHOLD:
					jsp = "value_threshold";
					break;
			}
			if (jsp != null) {
				req.getRequestDispatcher("/protected/" + jsp + ".jsp").forward(req, resp);
			} else {
				throw new ServletException("Unknown phenotype type");
			}
		} catch (ClientException ex) {
			throw new ServletException("Error setting up phenotype editor", ex);
		}
	}

	private Phenotype.Type getPropTypeFromParam(String inType) {
		Phenotype.Type type = null;
		if ("categorization".equals(inType)) {
			type = Phenotype.Type.CATEGORIZATION;
		} else if ("sequence".equals(inType)) {
			type = Phenotype.Type.SEQUENCE;
		} else if ("frequency".equals(inType)) {
			type = Phenotype.Type.FREQUENCY;
		} else if ("value_threshold".equals(inType)) {
			type = Phenotype.Type.VALUE_THRESHOLD;
		}
		return type;
	}

	private static final class PropertiesPhenotypeVisitor
			implements PhenotypeVisitor {

		private final ServicesClient servicesClient;
		private final Map<String, List<String>> properties;

		private PropertiesPhenotypeVisitor(
				ServicesClient inServicesClient) {

			this.servicesClient = inServicesClient;
			this.properties = new HashMap<>();
		}

		private void handlePhenotypeField(PhenotypeField inField) throws ClientException {
			SystemPhenotype systemPhenotype = this.servicesClient
					.getSystemPhenotype(inField.getPhenotypeKey(), false);
			this.properties.put(
					inField.getPhenotypeKey(), systemPhenotype.getProperties());
		}

		@Override
		public void visit(SystemPhenotype systemPhenotype) {
			LOGGER.debug(
					"visit system element -- {}", systemPhenotype.getKey());
		}

		@Override
		public void visit(Category categoricalPhenotype) {
			LOGGER.debug(
					"visit category element -- {}",
					categoricalPhenotype.getKey());
		}

		@Override
		public void visit(Sequence sequence) throws PhenotypeHandlingException {
			LOGGER.debug("visit sequence element -- {}", sequence.getKey());
			PhenotypeField primary = sequence.getPrimaryPhenotype();
			if (primary.getType() == Phenotype.Type.SYSTEM) {
				try {
					handlePhenotypeField(primary);
				} catch (ClientException ex) {
					throw new PhenotypeHandlingException(null, ex);
				}
			}

			for (RelatedPhenotypeField field : sequence
					.getRelatedPhenotypes()) {
				PhenotypeField element = field.getPhenotypeField();
				if (element.getType() == Phenotype.Type.SYSTEM) {
					try {
						handlePhenotypeField(element);
					} catch (ClientException ex) {
						throw new PhenotypeHandlingException(null, ex);
					}
				}
			}
		}

		@Override
		public void visit(Frequency frequency) throws PhenotypeHandlingException {
			LOGGER.debug("visit frequency element -- {}", frequency.getKey());
			PhenotypeField def = frequency.getPhenotype();
			if (def.getType() == Phenotype.Type.SYSTEM) {
				try {
					handlePhenotypeField(def);
				} catch (ClientException ex) {
					throw new PhenotypeHandlingException(null, ex);
				}
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
