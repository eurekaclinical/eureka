/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import java.util.Collections;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterUtil.unit;

public final class FrequencySliceAbstractionConverter implements
		PropositionDefinitionConverter<FrequencyEntity> {

	private PropositionDefinition primary;

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		SliceDefinition result = new SliceDefinition(entity.getKey());

		result.setMinIndex(entity.getAtLeastCount());
		result.addAbstractedFrom(entity.getAbstractedFrom().getKey());
		result.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
		        unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
		        unit(entity.getWithinAtMostUnits())));

		this.primary = result;

		return Collections.<PropositionDefinition> singletonList(result);
	}
}
