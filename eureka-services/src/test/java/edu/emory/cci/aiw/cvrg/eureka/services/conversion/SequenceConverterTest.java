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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.AbsoluteTimeUnit;

import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

import static org.junit.Assert.assertEquals;

public class SequenceConverterTest extends AbstractServiceTest {

	private static final Integer MIN = 1;
	private static final Integer MAX = 2;
	private TimeUnit timeUnit;
	private long counter = 0;
	
	@Before
	public void setUp() {
		this.timeUnit = new TimeUnit();
		this.timeUnit.setId(1L);
		this.timeUnit.setName(AbsoluteTimeUnit.DAY.getName());
		this.timeUnit.setDescription(AbsoluteTimeUnit.DAY.getPluralName());
		this.timeUnit.setRank(1);
	}

	private DataElementEntity createDataElement(long id, String suffix) {
		SystemProposition entity = new SystemProposition();
		entity.setId(id);
		entity.setSystemType(SystemProposition.SystemType.EVENT);
		entity.setKey("Encounter" + suffix);
		entity.setDescription("Encounter" + suffix);
		entity.setDisplayName("Encounter" + suffix);
		return entity;
	}

	private ExtendedDataElement createPrimaryExtendedProposition() {
		ExtendedDataElement proposition = new ExtendedDataElement();
		proposition.setId(counter++);
		proposition
				.setDataElementEntity(this.createDataElement(counter++, "Primary"));
		return proposition;
	}

	private ExtendedDataElement createLhsProposition() {
		ExtendedDataElement lhs = new ExtendedDataElement();
		lhs.setId(counter++);
		lhs.setDataElementEntity(this.createDataElement(counter++, "LHS"));
		return lhs;
	}

	private ExtendedDataElement createRhsProposition() {
		ExtendedDataElement rhs = new ExtendedDataElement();
		rhs.setId(counter++);
		rhs.setDataElementEntity(this.createDataElement(counter++, "RHS"));
		return rhs;
	}

	private List<Relation> createRelations() {
		List<Relation> relations = new ArrayList<>();
		Relation relation = new Relation();
		relation.setId(counter++);
		relation.setMinf1s2(MIN);
		relation.setMinf1s2TimeUnit(this.timeUnit);
		relation.setMaxf1s2(MAX);
		relation.setMaxf1s2TimeUnit(this.timeUnit);
		relation.setLhsExtendedDataElement(this.createLhsProposition());
		relation.setRhsExtendedDataElement((this.createRhsProposition()));
		relations.add(relation);
		return relations;
	}

	private SequenceEntity createSequenceEntity() {
		SequenceEntity result = new SequenceEntity();
		result.setId(counter++);
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

		assertEquals("Proposition list size", 1, definitions.size());

		assertEquals("Primary proposition id",
				toPropositionId("test-sequence"),
				primary.getId());

		Set<String> abstractedFrom = primary.getAbstractedFrom();
		String[] expectedAbstractedFrom = {"EncounterLHS", "EncounterRHS"};
		assertEquals("Abstracted from", 2, abstractedFrom.size());
		for (String key : expectedAbstractedFrom) {
			Assert.assertTrue("Absracted from", abstractedFrom.contains(key));
		}

		Set<List<TemporalExtendedPropositionDefinition>> pairs =
				primary.getTemporalExtendedPropositionDefinitionPairs();
		for (List<TemporalExtendedPropositionDefinition> pair : pairs) {
			TemporalExtendedPropositionDefinition lhs = pair.get(0);
			TemporalExtendedPropositionDefinition rhs = pair.get(1);
			assertEquals("LHS = EncounterLHS", "EncounterLHS",
					lhs.getPropositionId());
			assertEquals("RHS = EncounterRHS", "EncounterRHS",
					rhs.getPropositionId());

			org.protempa.proposition.interval.Relation relation =
					primary.getRelation(pair);
			assertEquals("Relation time units", AbsoluteTimeUnit.DAY,
					relation.getMaxDistanceBetweenUnits());
		}
	}
}
