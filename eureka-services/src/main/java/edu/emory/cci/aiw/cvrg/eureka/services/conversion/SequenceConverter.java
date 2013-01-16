/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropertyConstraint;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterUtil.unit;

final class SequenceConverter
        implements
		PropositionDefinitionConverter<SequenceEntity> {

	private final ValueComparatorDao valueCompDao;

	private PropositionDefinition primary;

	@Inject
	public SequenceConverter(ValueComparatorDao inValueCompDao) {
		valueCompDao = inValueCompDao;
	}

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	@Override
	public List<PropositionDefinition> convert(SequenceEntity proposition) {
		HighLevelAbstractionDefinition hlad = 
				new HighLevelAbstractionDefinition(
		        proposition.getKey());
		hlad.setDisplayName(proposition.getDisplayName());
		hlad.setAbbreviatedDisplayName(proposition.getAbbrevDisplayName());
		for (Relation rel : proposition.getRelations()) {
			TemporalExtendedPropositionDefinition tepdLhs = 
					buildExtendedProposition(rel
			        .getLhsExtendedProposition());

			TemporalExtendedPropositionDefinition tepdRhs = 
					buildExtendedProposition(rel
			        .getRhsExtendedProposition());

			hlad.add(tepdLhs);
			hlad.add(tepdRhs);
			hlad.setRelation(tepdLhs, tepdRhs, buildRelation(rel));
		}
		
		this.primary = hlad;

		return Collections.<PropositionDefinition> singletonList(hlad);
	}

	private TemporalExtendedPropositionDefinition buildExtendedProposition(
	        ExtendedProposition ep) {
		TemporalExtendedPropositionDefinition tepd = 
				new TemporalExtendedPropositionDefinition(
				ep.getId().toString());

		if (ep.getPropertyConstraint() != null) {
			PropertyConstraint pc = new PropertyConstraint();
			pc.setPropertyName(ep.getPropertyConstraint().getPropertyName());
			pc.setValue(ValueType.VALUE.parse(ep.getPropertyConstraint()
			        .getValue()));
			pc.setValueComp(ValueComparator.EQUAL_TO);

			tepd.getPropertyConstraints().add(pc);
		}
		tepd.setMinLength(ep.getMinDuration());
		tepd.setMinLengthUnit(unit(ep.getMinDurationTimeUnit()));
		tepd.setMaxLength(ep.getMaxDuration());
		tepd.setMaxLengthUnit(unit(ep.getMaxDurationTimeUnit()));

		return tepd;
	}

	private org.protempa.proposition.interval.Relation buildRelation(
	        Relation rel) {
		return new org.protempa.proposition.interval.Relation(null, null, null,
		        null, rel.getMins1f2(), unit(rel.getMins1f2TimeUnit()),
		        rel.getMaxs1f2(), unit(rel.getMaxs1f2TimeUnit()),
		        rel.getMinf1s2(), unit(rel.getMinf1s2TimeUnit()),
		        rel.getMaxf1s2(), unit(rel.getMaxf1s2TimeUnit()), null, null,
		        null, null);
	}
}
