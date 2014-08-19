package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import org.protempa.proposition.value.NominalValue;

/**
 *
 * @author Andrew Post
 */
public class AbstractConverter {
	private final ConversionSupport conversionSupport;

	AbstractConverter() {
		this.conversionSupport = new ConversionSupport();
	}
	
	protected String asValueString(DataElementEntity dataElement) {
		return this.conversionSupport.asValueString(dataElement);
	}
	
	protected NominalValue asValue(DataElementEntity dataElement) {
		return this.conversionSupport.asValue(dataElement);
	}
	
	protected String toPropositionId(DataElementEntity dataElement) {
		return this.conversionSupport.toPropositionId(dataElement);
	}
	
	protected String toPropositionIdWrapped(DataElementEntity dataElement) {
		return this.conversionSupport.toPropositionIdWrapped(dataElement);
	}
}
