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
package edu.emory.cci.aiw.cvrg.eureka.services.transformation;

import static edu.emory.cci.aiw.cvrg.eureka.services.transformation.PropositionDefinitionPackagerUtil.unit;

import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class FrequencyHighLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<FrequencyEntity, HighLevelAbstractionDefinition> {

	private final ValueComparatorDao valueCompDao;

	@Inject
	public FrequencyHighLevelAbstractionPackager(
	        ValueComparatorDao inValueCompDao) {
		valueCompDao = inValueCompDao;
	}

	@Override
	public HighLevelAbstractionDefinition pack(FrequencyEntity entity) {
		HighLevelAbstractionDefinition result = new HighLevelAbstractionDefinition(
		        entity.getKey());

		if (!(entity.getAbstractedFrom() instanceof ValueThresholdGroupEntity)) {
			throw new IllegalArgumentException(
			        "frequency must be abstracted from value thresholds");
		}
		ValueThresholdGroupEntity thresholds = (ValueThresholdGroupEntity) entity
		        .getAbstractedFrom();
		PropositionDefinition abstractedFrom;
		if (thresholds.getValueThresholds().size() > 1) {
			ValueThresholdsCompoundLowLevelAbstractionPackager cllaPackager = new ValueThresholdsCompoundLowLevelAbstractionPackager();
			abstractedFrom = cllaPackager.pack(thresholds);
		} else {
			ValueThresholdsLowLevelAbstractionPackager llaPackager = new ValueThresholdsLowLevelAbstractionPackager(
			        valueCompDao);
			abstractedFrom = llaPackager.pack(thresholds);
		}

		TemporalExtendedParameterDefinition tepd = new TemporalExtendedParameterDefinition(
		        abstractedFrom.getId());
		tepd.setValue(NominalValue.getInstance(abstractedFrom.getId()
		        + "_VALUE"));
		result.setRelation(tepd, tepd, new Relation());
		result.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
		        unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
		        unit(entity.getWithinAtMostUnits())));

		return result;
	}
}
