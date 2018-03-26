package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*-
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2016 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.etl.entity.PatientSetExtractionConfig;
import java.util.Collection;
import org.protempa.dest.QueryResultsHandlerProcessingException;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.value.Value;

/**
 *
 * @author Andrew Post
 */
class PatientIdExtractor {
	private final String aliasPropId;
	private final String aliasFieldNameProperty;
	private final String aliasFieldName;
	private final String aliasPatientIdPropertyName;

	PatientIdExtractor(PatientSetExtractionConfig inPatientSetExtractionConfig) {
		this.aliasPropId = inPatientSetExtractionConfig.getAliasPropositionId();
		this.aliasFieldNameProperty = inPatientSetExtractionConfig.getAliasFieldNameProperty();
		this.aliasFieldName = inPatientSetExtractionConfig.getAliasFieldName();
		this.aliasPatientIdPropertyName = inPatientSetExtractionConfig.getAliasPatientIdProperty();
		
		assert this.aliasPropId != null : "aliasPropId cannot be null";
		assert this.aliasPatientIdPropertyName != null : "aliasPatientIdPropertyName cannot be null";
	}
	
	String extract(String keyId, Collection<Proposition> propositions) throws QueryResultsHandlerProcessingException {
		for (Proposition proposition : propositions) {
			String propId = proposition.getId();
			if (propId.equals(this.aliasPropId)) {
				boolean yes = this.aliasFieldNameProperty == null;
				if (!yes) {
					Value aliasFieldNameVal = proposition.getProperty(this.aliasFieldNameProperty);
					yes = this.aliasFieldName == null || this.aliasFieldName.equals(aliasFieldNameVal.getFormatted());
				}
				if (yes) {
					Value val = proposition.getProperty(this.aliasPatientIdPropertyName);
					if (val != null) {
						return val.getFormatted();
					}
				}
			}
		}
		throw new QueryResultsHandlerProcessingException("No patient id for keyId " + keyId);
	}
}
