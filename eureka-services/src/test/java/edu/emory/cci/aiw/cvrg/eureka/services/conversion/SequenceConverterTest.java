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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import junit.framework.Assert;
import org.junit.Test;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.AbsoluteTimeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SequenceConverterTest
		extends FrequencySliceAbstractionConverterTest {

	private static final Integer MIN = Integer.valueOf(1);
	private static final Integer MAX = Integer.valueOf(2);
	private final TimeUnit timeUnit;
	private long counter = 0;

	public SequenceConverterTest() {
		this.timeUnit = new TimeUnit();
		this.timeUnit.setId(1L);
		this.timeUnit.setName(AbsoluteTimeUnit.DAY.getName());
		this.timeUnit.setDescription(AbsoluteTimeUnit.DAY.getPluralName());
		this.timeUnit.setRank(1);
	}

	private TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	private DataElementEntity createDataElement(long id, String suffix) {
		SystemProposition entity = new SystemProposition();
		entity.setId(Long.valueOf(id));
		entity.setSystemType(SystemProposition.SystemType.EVENT);
		entity.setKey("Encounter" + suffix);
		entity.setDescription("Encounter" + suffix);
		entity.setDisplayName("Encounter" + suffix);
		return entity;
	}

	private ExtendedDataElement createPrimaryExtendedProposition() {
		ExtendedDataElement proposition = new ExtendedDataElement();
		proposition.setId(Long.valueOf(counter++));
		proposition
				.setDataElementEntity(this.createDataElement(counter++, "Primary"));
		return proposition;
	}

	private ExtendedDataElement createLhsProposition() {
		ExtendedDataElement lhs = new ExtendedDataElement();
		lhs.setId(Long.valueOf(counter++));
		lhs.setDataElementEntity(this.createDataElement(counter++, "LHS"));
		return lhs;
	}

	private ExtendedDataElement createRhsProposition() {
		ExtendedDataElement rhs = new ExtendedDataElement();
		rhs.setId(Long.valueOf(counter++));
		rhs.setDataElementEntity(this.createDataElement(counter++, "RHS"));
		return rhs;
	}

	private List<Relation> createRelations() {
		List<Relation> relations = new ArrayList<Relation>();
		Relation relation = new Relation();
		relation.setId(Long.valueOf(counter++));
		relation.setMinf1s2(MIN);
		relation.setMinf1s2TimeUnit(this.getTimeUnit());
		relation.setMaxf1s2(MAX);
		relation.setMaxf1s2TimeUnit(this.getTimeUnit());
		relation.setLhsExtendedDataElement(this.createLhsProposition());
		relation.setRhsExtendedDataElement((this.createRhsProposition()));
		relations.add(relation);
		return relations;
	}

	private SequenceEntity createSequenceEntity() {
		SequenceEntity result = new SequenceEntity();
		result.setId(Long.valueOf(counter++));
		result.setKey("test-sequence");
		result.setDescription("test-sequence");
		result.setDisplayName("test-sequence");
		result.setInSystem(false);
		result.setRelations(this.createRelations());
		result.setPrimaryExtendedDataElement(this.createPrimaryExtendedProposition());
		return result;
	}

	@Test
	public void testSequenceConverter() {

		PropositionDefinitionConverterVisitor visitor =
				this.getInstance(PropositionDefinitionConverterVisitor.class);

		SequenceConverter converter = new SequenceConverter();
		converter.setConverterVisitor(visitor);

		List<PropositionDefinition> definitions =
				converter.convert(this.createSequenceEntity());
		HighLevelAbstractionDefinition primary =
				converter.getPrimaryPropositionDefinition();

		Assert.assertEquals("Proposition list size", 1, definitions.size());

		Assert.assertEquals("Primary proposition id", primary.getId(),
				"test-sequence");

		Set<String> abstractedFrom = primary.getAbstractedFrom();
		String[] expectedAbstractedFrom = {"EncounterLHS", "EncounterRHS"};
		Assert.assertEquals("Abstracted from", 2, abstractedFrom.size());
		for (String key : expectedAbstractedFrom) {
			Assert.assertTrue("Absracted from", abstractedFrom.contains(key));
		}

		Set<List<TemporalExtendedPropositionDefinition>> pairs =
				primary.getTemporalExtendedPropositionDefinitionPairs();
		for (List<TemporalExtendedPropositionDefinition> pair : pairs) {
			TemporalExtendedPropositionDefinition lhs = pair.get(0);
			TemporalExtendedPropositionDefinition rhs = pair.get(1);
			Assert.assertEquals("LHS = EncounterLHS", "EncounterLHS",
					lhs.getPropositionId());
			Assert.assertEquals("RHS = EncounterRHS", "EncounterRHS",
					rhs.getPropositionId());

			org.protempa.proposition.interval.Relation relation =
					primary.getRelation(pair);
			Assert.assertEquals("Relation time units", AbsoluteTimeUnit.DAY,
					relation.getMaxDistanceBetweenUnits());
		}
	}
}
