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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

import java.util.ArrayList;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterUtil.unit;

public final class FrequencyHighLevelAbstractionConverter
        implements
		PropositionDefinitionConverter<FrequencyEntity> {

	private final Long userId;
	private final ValueComparatorDao valueCompDao;

	private PropositionDefinition primary;

	@Inject
	public FrequencyHighLevelAbstractionConverter(Long inUserId,
												  ValueComparatorDao inValueCompDao) {
		userId = inUserId;
		valueCompDao = inValueCompDao;
	}

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	@Override
	public List<PropositionDefinition> convert(FrequencyEntity entity) {
		List<PropositionDefinition> result = new
				ArrayList<PropositionDefinition>();
		HighLevelAbstractionDefinition hlad = 
				new HighLevelAbstractionDefinition(entity.getKey());
		hlad.setDisplayName(entity.getDisplayName());
		hlad.setAbbreviatedDisplayName(entity.getAbbrevDisplayName());

		if (!(entity.getAbstractedFrom() instanceof ValueThresholdGroupEntity)) {
			throw new IllegalArgumentException(
			        "frequency must be abstracted from value thresholds");
		}
		ValueThresholdGroupEntity thresholds = (ValueThresholdGroupEntity) entity
		        .getAbstractedFrom();
		List<PropositionDefinition> abstractedFrom;
		PropositionDefinitionConverter converter;
		if (thresholds.getValueThresholds().size() > 1) {
			converter = new ValueThresholdsCompoundLowLevelAbstractionConverter();
		} else {
			converter = new ValueThresholdsLowLevelAbstractionConverter
					(userId, valueCompDao);
		}
		abstractedFrom = converter.convert(thresholds);

		result.addAll(abstractedFrom);

		TemporalExtendedParameterDefinition tepd = new TemporalExtendedParameterDefinition(
		        converter.getPrimaryPropositionDefinition().getId());
		tepd.setValue(NominalValue.getInstance(converter.getPrimaryPropositionDefinition().getId()
		        + "_VALUE"));
		hlad.setRelation(tepd, tepd, new Relation());
		hlad.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
				unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
				unit(entity.getWithinAtMostUnits())));

		result.add(hlad);
		this.primary = hlad;

		return result;
	}
}
