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

import edu.emory.cci.aiw.cvrg.eureka.services.entity.ExtendedPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SystemProposition;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.protempa.AbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SimpleGapFunction;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import org.eurekaclinical.eureka.client.comm.SystemType;

import static org.junit.Assert.assertEquals;

public class FrequencyFirstNotValueThresholdConverterTest extends
		AbstractServiceTest {
	private SystemProposition event;
	private FrequencyEntity frequency;
	private AbstractionDefinition hlad;
	private List<PropositionDefinition> propDefs;
	private SimpleGapFunction gf;

	@Before
	public void setUp() {
		PropositionDefinitionConverterVisitor converterVisitor = 
				this.getInstance
				(PropositionDefinitionConverterVisitor.class);
		FrequencyNotValueThresholdConverter converter = 
				new FrequencyNotValueThresholdConverter();
		converter.setVisitor(converterVisitor);
		
		event = new SystemProposition();
		event.setId(1L);
		event.setKey("test-event1");
		event.setDisplayName("test-event1-display");
		event.setDescription("test-event1-abbrev");
		event.setInSystem(true);
		event.setSystemType(SystemType.EVENT);
		
		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");
		
		FrequencyType ft = new FrequencyType();
		ft.setName("first");
		
		frequency = new FrequencyEntity();
		frequency.setId(2L);
		frequency.setKey("test-slice-key");
		frequency.setCount(3);
		frequency.setWithinAtLeast(1);
		frequency.setWithinAtLeastUnits(dayUnit);
		frequency.setWithinAtMost(90);
		frequency.setWithinAtMostUnits(dayUnit);
		frequency.setFrequencyType(ft);
		
		ExtendedPhenotype af = new ExtendedPhenotype();
		af.setPhenotypeEntity(event);
		
		frequency.setExtendedProposition(af);
		propDefs = converter.convert(frequency);
		hlad = converter.getPrimaryPropositionDefinition();
		gf = (SimpleGapFunction) hlad.getGapFunction();
	}

	@Test
	public void testNumberOfPropositionDefinitionsCreated() {
		assertEquals("wrong number of proposition definitions created", 2,
				propDefs.size());
	}
	
	@Test
	public void testId() {
		assertEquals("wrong ID", toPropositionId(frequency), hlad.getId());
	}

	@Test
	public void testAbstractedFromSize() {
		assertEquals("wrong abstracted from size", 
				1, hlad.getAbstractedFrom().size());
	}
	
	@Test
	public void testAbstractedFrom() {
		assertEquals("wrong abstracted from", frequency.getKey() + "_SUB",
				hlad.getAbstractedFrom().iterator().next());
	}
	
	@Test
	public void testGapMax() {
		assertEquals("wrong gap max", Integer.valueOf(0), gf.getMaximumGap());
	}
	
	@Test
	public void testGapMaxUnit() {
		assertEquals("wrong gap max unit", null, gf.getMaximumGapUnit());
	}
	
//	@Test
//	public void minDistanceBetween() {
//		Set<List<TemporalExtendedPropositionDefinition>> tepdPairs = 
//				sliceDef.getTemporalExtendedPropositionDefinitionPairs();
//		for (List<TemporalExtendedPropositionDefinition> tepdPair : tepdPairs) {
//			Relation rel = sliceDef.getRelation(tepdPair);
//			assertEquals("wrong min distance between", Integer.valueOf(1), rel.getMinDistanceBetween());
//		}
//	}
//	
//	@Test
//	public void minDistanceBetweenUnits() {
//		Set<List<TemporalExtendedPropositionDefinition>> tepdPairs = 
//				sliceDef.getTemporalExtendedPropositionDefinitionPairs();
//		for (List<TemporalExtendedPropositionDefinition> tepdPair : tepdPairs) {
//			Relation rel = sliceDef.getRelation(tepdPair);
//			assertEquals("wrong min distance between units", AbsoluteTimeUnit.DAY, rel.getMinDistanceBetweenUnits());
//		}
//	}
}
