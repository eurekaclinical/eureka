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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.*;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
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
        DataElement.Type propType = this.getPropTypeFromParam(req.getParameter("type"));
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
				DataElement dataElement = this.servicesClient
						.getUserElement(propKey, false);
				PropertiesDataElementVisitor visitor = new PropertiesDataElementVisitor(
						this.servicesClient);
				try {
					LOGGER.debug("Visiting {}", dataElement.getKey());
					dataElement.accept(visitor);
				} catch (DataElementHandlingException e) {
					LOGGER.error("Visiting {}", dataElement.getKey(), e);
					throw new ServletException("Error getting data element properties", e.getCause());
				}
				req.setAttribute("properties", visitor.getProperties());
				req.setAttribute("proposition", dataElement);
				req.setAttribute(
						"propositionType", dataElement.getType().toString());
				propType = dataElement.getType();
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
				throw new ServletException("Unknown data element type");
			}
		} catch (ClientException ex) {
			throw new ServletException("Error setting up data element editor", ex);
		}
	}

	private DataElement.Type getPropTypeFromParam(String inType) {
		DataElement.Type type = null;
		if ("categorization".equals(inType)) {
			type = DataElement.Type.CATEGORIZATION;
		} else if ("sequence".equals(inType)) {
			type = DataElement.Type.SEQUENCE;
		} else if ("frequency".equals(inType)) {
			type = DataElement.Type.FREQUENCY;
		} else if ("value_threshold".equals(inType)) {
			type = DataElement.Type.VALUE_THRESHOLD;
		}
		return type;
	}

	private static final class PropertiesDataElementVisitor
			implements DataElementVisitor {

		private final ServicesClient servicesClient;
		private final Map<String, List<String>> properties;

		private PropertiesDataElementVisitor(
				ServicesClient inServicesClient) {

			this.servicesClient = inServicesClient;
			this.properties = new HashMap<>();
		}

		private void handleDataElementField(DataElementField inField) throws ClientException {
			SystemElement systemElement = this.servicesClient
					.getSystemElement(inField.getDataElementKey(), false);
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
		public void visit(Sequence sequence) throws DataElementHandlingException {
			LOGGER.debug("visit sequence element -- {}", sequence.getKey());
			DataElementField primary = sequence.getPrimaryDataElement();
			if (primary.getType() == DataElement.Type.SYSTEM) {
				try {
					handleDataElementField(primary);
				} catch (ClientException ex) {
					throw new DataElementHandlingException(null, ex);
				}
			}

			for (RelatedDataElementField field : sequence
					.getRelatedDataElements()) {
				DataElementField element = field.getDataElementField();
				if (element.getType() == DataElement.Type.SYSTEM) {
					try {
						handleDataElementField(element);
					} catch (ClientException ex) {
						throw new DataElementHandlingException(null, ex);
					}
				}
			}
		}

		@Override
		public void visit(Frequency frequency) throws DataElementHandlingException {
			LOGGER.debug("visit frequency element -- {}", frequency.getKey());
			DataElementField def = frequency.getDataElement();
			if (def.getType() == DataElement.Type.SYSTEM) {
				try {
					handleDataElementField(def);
				} catch (ClientException ex) {
					throw new DataElementHandlingException(null, ex);
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
