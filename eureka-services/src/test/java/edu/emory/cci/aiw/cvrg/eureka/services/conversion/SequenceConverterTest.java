/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ExtendedPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.TimeUnit;
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
import org.eurekaclinical.eureka.client.comm.SystemType;

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

	private PhenotypeEntity createPhenotype(long id, String suffix) {
		SystemProposition entity = new SystemProposition();
		entity.setId(id);
		entity.setSystemType(SystemType.EVENT);
		entity.setKey("Encounter" + suffix);
		entity.setDescription("Encounter" + suffix);
		entity.setDisplayName("Encounter" + suffix);
		return entity;
	}

	private ExtendedPhenotype createPrimaryExtendedProposition() {
		ExtendedPhenotype proposition = new ExtendedPhenotype();
		proposition.setId(counter++);
		proposition
				.setPhenotypeEntity(this.createPhenotype(counter++, "Primary"));
		return proposition;
	}

	private ExtendedPhenotype createLhsProposition() {
		ExtendedPhenotype lhs = new ExtendedPhenotype();
		lhs.setId(counter++);
		lhs.setPhenotypeEntity(this.createPhenotype(counter++, "LHS"));
		return lhs;
	}

	private ExtendedPhenotype createRhsProposition() {
		ExtendedPhenotype rhs = new ExtendedPhenotype();
		rhs.setId(counter++);
		rhs.setPhenotypeEntity(this.createPhenotype(counter++, "RHS"));
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
		relation.setLhsExtendedPhenotype(this.createLhsProposition());
		relation.setRhsExtendedPhenotype((this.createRhsProposition()));
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
		result.setPrimaryExtendedPhenotype(this.createPrimaryExtendedProposition());
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
