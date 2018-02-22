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


import edu.emory.cci.aiw.cvrg.eureka.services.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.arp.javautil.arrays.Arrays;
import org.eurekaclinical.eureka.client.comm.SystemType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

public class CategorizationConverterTest extends AbstractServiceTest {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private CategorizationConverter converter;

	@Before
	public void setUp() {
		converterVisitor = this.getInstance
				(PropositionDefinitionConverterVisitor.class);
		converter = new CategorizationConverter();
		converter.setConverterVisitor(converterVisitor);
	}
	

	@Test
	public void testEventCategorization() {
		SystemProposition event1 = new SystemProposition();
		event1.setId(1L);
		event1.setKey("test-event1");
		event1.setInSystem(true);
		event1.setSystemType(SystemType.EVENT);

		SystemProposition event2 = new SystemProposition();
		event2.setId(2L);
		event2.setKey("test-event2");
		event2.setInSystem(true);
		event2.setSystemType(SystemType.EVENT);

		CategoryEntity eventCat1 = new CategoryEntity();
		eventCat1.setId(3L);
		eventCat1.setKey("test-event-cat1");
		eventCat1.setDisplayName("test-event-cat1-display");
		eventCat1.setDescription("test-event-cat1-abbrev");
		eventCat1.setCategoryType(CategoryType.EVENT);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(event1);
		iia1.add(event2);
		eventCat1.setMembers(iia1);

		List<PropositionDefinition> eventDefs1 = 
				this.converter.convert(eventCat1);
		assertEquals("wrong number of proposition definitions created", 1,
				eventDefs1.size());
		PropositionDefinition eventDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not an event", eventDef1 instanceof EventDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-event-cat1"), 
				eventDef1.getId());
		assertEquals("wrong display name", "test-event-cat1-display",
				eventDef1.getDisplayName());
		assertEquals("wrong description", "test-event-cat1-abbrev",
				eventDef1.getDescription());
		assertEquals("wrong size of inverse-is-a", 2,
				eventDef1.getInverseIsA().length);
		String[] inverseIsA = eventDef1.getInverseIsA();
		assertTrue("inverse-is-a-missing test-event1", 
				Arrays.contains(inverseIsA, "test-event1"));
		assertTrue("inverse-is-a-missing test-event2",
				Arrays.contains(inverseIsA, "test-event2"));

		CategoryEntity eventCat2 = new CategoryEntity();
		eventCat2.setId(4L);
		eventCat2.setKey("test-event-cat2");
		eventCat2.setDisplayName("test-event-cat2-display");
		eventCat2.setDescription("test-event-cat2-abbrev");
		eventCat2.setCategoryType(CategoryType.EVENT);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(eventCat1);
		eventCat2.setMembers(iia2);

		List<PropositionDefinition> eventDefs2 = this.converter.convert
				(eventCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				eventDefs2.size());
		PropositionDefinition eventDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not an event", eventDef2 instanceof EventDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-event-cat2"), 
				eventDef2.getId());
		assertEquals("wrong display name", "test-event-cat2-display",
				eventDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-event-cat2-abbrev",
				eventDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				eventDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-event-cat1"),
				eventDef2.getInverseIsA()[0]);
	}

	@Test
	public void testConstantCategorization() {
		SystemProposition constant1 = new SystemProposition();
		constant1.setId(1L);
		constant1.setKey("test-constant1");
		constant1.setInSystem(true);
		constant1.setSystemType(SystemType.CONSTANT);

		SystemProposition constant2 = new SystemProposition();
		constant2.setId(2L);
		constant2.setKey("test-constant2");
		constant2.setInSystem(true);
		constant2.setSystemType(SystemType.CONSTANT);

		CategoryEntity constantCat1 = new CategoryEntity();
		constantCat1.setId(3L);
		constantCat1.setKey("test-constant-cat1");
		constantCat1.setDisplayName("test-constant-cat1-display");
		constantCat1.setDescription("test-constant-cat1-abbrev");
		constantCat1.setCategoryType(CategoryType.CONSTANT);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(constant1);
		iia1.add(constant2);
		constantCat1.setMembers(iia1);

		List<PropositionDefinition> constantDefs1 = this.converter.convert
				(constantCat1);
		assertEquals("wrong number of proposition definitions created", 1,
				constantDefs1.size());
		PropositionDefinition constantDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a constant", constantDef1 instanceof ConstantDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-constant-cat1"), 
				constantDef1.getId());
		assertEquals("wrong display name", "test-constant-cat1-display",
				constantDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-constant-cat1-abbrev",
				constantDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				constantDef1.getInverseIsA().length);
		Set<String> inverseIsASet = Arrays.asSet(constantDef1.getInverseIsA());
		Set<String> expected = Arrays.asSet(
				new String[]{"test-constant1", "test-constant2"});
		assertEquals("inverse-is-a missing", expected, inverseIsASet);

		CategoryEntity constantCat2 = new CategoryEntity();
		constantCat2.setId(4L);
		constantCat2.setKey("test-constant-cat2");
		constantCat2.setDisplayName("test-constant-cat2-display");
		constantCat2.setDescription("test-constant-cat2-abbrev");
		constantCat2.setCategoryType(CategoryType.CONSTANT);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(constantCat1);
		constantCat2.setMembers(iia2);

		List<PropositionDefinition> constantDefs2 = this.converter.convert
				(constantCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				constantDefs2.size());
		PropositionDefinition constantDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a constant", constantDef2 instanceof ConstantDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-constant-cat2"), 
				constantDef2.getId());
		assertEquals("wrong display name", "test-constant-cat2-display",
				constantDef2.getDisplayName());
		assertEquals("wrong description", "test-constant-cat2-abbrev",
				constantDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				constantDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-constant-cat1"),
				constantDef2.getInverseIsA()[0]);

	}

	@Test
	public void testPrimitiveParameterCategorization() {
		SystemProposition primParam1 = new SystemProposition();
		primParam1.setId(1L);
		primParam1.setKey("test-primparam1");
		primParam1.setInSystem(true);
		primParam1.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		SystemProposition primParam2 = new SystemProposition();
		primParam2.setId(2L);
		primParam2.setKey("test-primparam2");
		primParam2.setInSystem(true);
		primParam2.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		CategoryEntity primParamCat1 = new CategoryEntity();
		primParamCat1.setId(3L);
		primParamCat1.setKey("test-primparam-cat1");
		primParamCat1.setDisplayName("test-primparam-cat1-display");
		primParamCat1.setDescription("test-primparam-cat1-abbrev");
		primParamCat1
				.setCategoryType(CategoryType.PRIMITIVE_PARAMETER);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(primParam1);
		iia1.add(primParam2);
		primParamCat1.setMembers(iia1);

		List<PropositionDefinition> primParamDefs1 = this.converter.convert
				(primParamCat1);
		assertEquals("wrong number of proposition definitions created", 1,
				primParamDefs1.size());
		PropositionDefinition primParamDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a primitive parameter",
				primParamDef1 instanceof PrimitiveParameterDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-primparam-cat1"), 
				primParamDef1.getId());
		assertEquals("wrong display name", "test-primparam-cat1-display",
				primParamDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-primparam-cat1-abbrev",
				primParamDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				primParamDef1.getInverseIsA().length);
		Set<String> inverseIsASet = 
				Arrays.asSet(primParamDef1.getInverseIsA());
		Set<String> expected = Arrays.asSet(
				new String[]{"test-primparam1", "test-primparam2"});
		assertEquals("inverse-is-a missing", expected, inverseIsASet);

		CategoryEntity primParamCat2 = new CategoryEntity();
		primParamCat2.setId(4L);
		primParamCat2.setKey("test-primparam-cat2");
		primParamCat2.setDisplayName("test-primparam-cat2-display");
		primParamCat2.setDescription("test-primparam-cat2-abbrev");
		primParamCat2
				.setCategoryType(CategoryType.PRIMITIVE_PARAMETER);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(primParamCat1);
		primParamCat2.setMembers(iia2);

		List<PropositionDefinition> primParamDefs2 = this.converter.convert
				(primParamCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				primParamDefs2.size());
		PropositionDefinition primParamDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a primitive parameter",
				primParamDef2 instanceof PrimitiveParameterDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-primparam-cat2"), 
				primParamDef2.getId());
		assertEquals("wrong display name", "test-primparam-cat2-display",
				primParamDef2.getDisplayName());
		assertEquals("wrong description", "test-primparam-cat2-abbrev",
				primParamDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				primParamDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-primparam-cat1"),
				primParamDef2.getInverseIsA()[0]);
	}

	@Test
	public void testHighLevelAbstractionCategorization() {
		SystemProposition primParam1 = new SystemProposition();
		primParam1.setId(1L);
		primParam1.setKey("test-primparam1");
		primParam1.setInSystem(true);
		primParam1.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		SystemProposition primParam2 = new SystemProposition();
		primParam2.setId(2L);
		primParam2.setKey("test-primparam2");
		primParam2.setInSystem(true);
		primParam2.setSystemType(SystemType.PRIMITIVE_PARAMETER);
		
		SequenceEntity hla1 = new SequenceEntity();
		hla1.setId(1L);
		hla1.setKey("test-hla1");
		hla1.setInSystem(false);
		ExtendedPhenotype ep1 = new ExtendedPhenotype();
		ep1.setPhenotypeEntity(primParam1);
		ExtendedPhenotype ep2 = new ExtendedPhenotype();
		ep2.setPhenotypeEntity(primParam2);
		hla1.setPrimaryExtendedPhenotype(ep1);
		Relation relation = new Relation();
		relation.setLhsExtendedPhenotype(ep1);
		relation.setRhsExtendedPhenotype(ep2);
		hla1.setRelations(Arrays.<Relation>asList(new Relation[]{relation}));

		SequenceEntity hla2 = new SequenceEntity();
		hla2.setId(2L);
		hla2.setKey("test-hla2");
		hla2.setInSystem(false);
		hla2.setPrimaryExtendedPhenotype(ep1);
		hla2.setRelations(Arrays.<Relation>asList(new Relation[]{relation}));

		CategoryEntity hlaCat1 = new CategoryEntity();
		hlaCat1.setId(3L);
		hlaCat1.setKey("test-hla-cat1");
		hlaCat1.setDisplayName("test-hla-cat1-display");
		hlaCat1.setDescription("test-hla-cat1-abbrev");
		hlaCat1.setCategoryType(CategoryType.HIGH_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(hla1);
		iia1.add(hla2);
		hlaCat1.setMembers(iia1);

		List<PropositionDefinition> hlaDefs1 = this.converter.convert
				(hlaCat1);
		assertEquals("wrong number of proposition definitions created", 3,
				hlaDefs1.size());
		PropositionDefinition hlaDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				hlaDef1 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-hla-cat1"), 
				hlaDef1.getId());
		assertEquals("wrong display name", "test-hla-cat1-display",
				hlaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-hla-cat1-abbrev",
				hlaDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				hlaDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(hlaDef1.getInverseIsA()[0].equals(toPropositionId("test-hla1")) && hlaDef1
						.getInverseIsA()[1].equals(toPropositionId("test-hla2")))
						|| (hlaDef1.getInverseIsA()[0].equals(toPropositionId("test-hla2")) &&
						hlaDef1
								.getInverseIsA()[1].equals(toPropositionId("test-hla1"))));

		CategoryEntity hlaCat2 = new CategoryEntity();
		hlaCat2.setId(4L);
		hlaCat2.setKey("test-hla-cat2");
		hlaCat2.setDisplayName("test-hla-cat2-display");
		hlaCat2.setDescription("test-hla-cat2-abbrev");
		hlaCat2.setCategoryType(CategoryType.HIGH_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(hlaCat1);
		hlaCat2.setMembers(iia2);

		List<PropositionDefinition> hlaDefs2 = this.converter.convert(hlaCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				hlaDefs2.size());
		PropositionDefinition hlaDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				hlaDef2 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-hla-cat2"), 
				hlaDef2.getId());
		assertEquals("wrong display name", "test-hla-cat2-display",
				hlaDef2.getDisplayName());
		assertEquals("wrong description", "test-hla-cat2-abbrev",
				hlaDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				hlaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-hla-cat1"),
				hlaDef2.getInverseIsA()[0]);
	}

	@Test
	public void testSliceAbstractionCategorization() {
		SystemProposition sp = new SystemProposition();
		sp.setInSystem(true);
		sp.setKey("foo");
		sp.setSystemType(SystemType.EVENT);
		
		FrequencyType ft = new FrequencyType();
		ft.setName("at least");
		
		FrequencyEntity sa1 = new FrequencyEntity();
		sa1.setId(1L);
		sa1.setKey("test-slice1");
		sa1.setInSystem(false);
		ExtendedPhenotype ede1 = new ExtendedPhenotype();
		ede1.setPhenotypeEntity(sp);
		sa1.setExtendedProposition(ede1);
		sa1.setCount(1);
		sa1.setFrequencyType(ft);

		FrequencyEntity sa2 = new FrequencyEntity();
		sa2.setId(2L);
		sa2.setKey("test-slice2");
		sa2.setInSystem(false);
		ExtendedPhenotype ede2 = new ExtendedPhenotype();
		ede2.setPhenotypeEntity(sp);
		sa2.setExtendedProposition(ede2);
		sa2.setCount(1);
		sa2.setFrequencyType(ft);

		CategoryEntity saCat1 = new CategoryEntity();
		saCat1.setId(3L);
		saCat1.setKey("test-slice-cat1");
		saCat1.setDisplayName("test-slice-cat1-display");
		saCat1.setDescription("test-slice-cat1-abbrev");
		saCat1.setCategoryType(CategoryType.SLICE_ABSTRACTION);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(sa1);
		iia1.add(sa2);
		saCat1.setMembers(iia1);

		List<PropositionDefinition> saDefs1 = this.converter.convert(saCat1);
		assertEquals("wrong number of proposition definitions created", 3,
				saDefs1.size());
		PropositionDefinition saDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a slice abstraction",
				saDef1 instanceof SliceDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-slice-cat1"), 
				saDef1.getId());
		assertEquals("wrong display name", "test-slice-cat1-display",
				saDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-slice-cat1-abbrev",
				saDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				saDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(saDef1.getInverseIsA()[0].equals(toPropositionId("test-slice1")) && saDef1
						.getInverseIsA()[1].equals(toPropositionId("test-slice2")))
						|| (saDef1.getInverseIsA()[0].equals(toPropositionId("test-slice2")) &&
						saDef1
						.getInverseIsA()[1].equals(toPropositionId("test-slice1"))));

		CategoryEntity saCat2 = new CategoryEntity();
		saCat2.setId(4L);
		saCat2.setKey("test-slice-cat2");
		saCat2.setDisplayName("test-slice-cat2-display");
		saCat2.setDescription("test-slice-cat2-abbrev");
		saCat2.setCategoryType(CategoryType.SLICE_ABSTRACTION);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(saCat1);
		saCat2.setMembers(iia2);

		List<PropositionDefinition> saDefs2 = this.converter.convert(saCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				saDefs2.size());
		PropositionDefinition saDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a slice abstraction",
				saDef2 instanceof SliceDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-slice-cat2"), 
				saDef2.getId());
		assertEquals("wrong display name", "test-slice-cat2-display",
				saDef2.getDisplayName());
		assertEquals("wrong description", "test-slice-cat2-abbrev",
				saDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				saDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-slice-cat1"),
				saDef2.getInverseIsA()[0]);
	}

	@Test
	public void testLowLevelAbstractionCategorization() {
		SystemProposition primParam1 = new SystemProposition();
		primParam1.setId(1L);
		primParam1.setKey("test-primparam1");
		primParam1.setInSystem(true);
		primParam1.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		SystemProposition primParam2 = new SystemProposition();
		primParam2.setId(2L);
		primParam2.setKey("test-primparam2");
		primParam2.setInSystem(true);
		primParam2.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		ValueThresholdGroupEntity lla1 = new ValueThresholdGroupEntity();
		lla1.setId(1L);
		lla1.setKey("test-lla1");
		lla1.setInSystem(false);
		lla1.setValueThresholds(new ArrayList<ValueThresholdEntity>(1));
		ValueThresholdEntity vte1 = new ValueThresholdEntity();
		vte1.setAbstractedFrom(primParam1);
		lla1.getValueThresholds().add(vte1);

		ValueThresholdGroupEntity lla2 = new ValueThresholdGroupEntity();
		lla2.setId(2L);
		lla2.setKey("test-lla2");
		lla2.setInSystem(false);
		lla2.setValueThresholds(new ArrayList<ValueThresholdEntity>(1));
		ValueThresholdEntity vte2 = new ValueThresholdEntity();
		vte2.setAbstractedFrom(primParam2);
		lla2.getValueThresholds().add(vte2);

		CategoryEntity llaCat1 = new CategoryEntity();
		llaCat1.setId(3L);
		llaCat1.setKey("test-lla-cat1");
		llaCat1.setDisplayName("test-lla-cat1-display");
		llaCat1.setDescription("test-lla-cat1-abbrev");
		llaCat1.setCategoryType(CategoryType.LOW_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(lla1);
		iia1.add(lla2);
		llaCat1.setMembers(iia1);

		List<PropositionDefinition> llaDefs1 = this.converter.convert(llaCat1);
		assertEquals("wrong number of proposition definitions created", 3,
				llaDefs1.size());
		PropositionDefinition llaDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				llaDef1 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-lla-cat1"), 
				llaDef1.getId());
		assertEquals("wrong display name", "test-lla-cat1-display",
				llaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-lla-cat1-abbrev",
				llaDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				llaDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(llaDef1.getInverseIsA()[0].equals(toPropositionId("test-lla1")) &&
						llaDef1
						.getInverseIsA()[1].equals(toPropositionId("test-lla2")))
						|| (llaDef1.getInverseIsA()[0].equals(toPropositionId("test-lla2")) &&
						llaDef1
						.getInverseIsA()[1].equals(toPropositionId("test-lla1"))));

		CategoryEntity llaCat2 = new CategoryEntity();
		llaCat2.setId(4L);
		llaCat2.setKey("test-lla-cat2");
		llaCat2.setDisplayName("test-lla-cat2-display");
		llaCat2.setDescription("test-lla-cat2-abbrev");
		llaCat2.setCategoryType(CategoryType.LOW_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(llaCat1);
		llaCat2.setMembers(iia2);

		List<PropositionDefinition> llaDefs2 = this.converter.convert(llaCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				llaDefs2.size());
		PropositionDefinition llaDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				llaDef2 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-lla-cat2"), 
				llaDef2.getId());
		assertEquals("wrong display name", "test-lla-cat2-display",
				llaDef2.getDisplayName());
		assertEquals("wrong description", "test-lla-cat2-abbrev",
				llaDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				llaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", toPropositionId("test-lla-cat1"),
				llaDef2.getInverseIsA()[0]);
	}

	@Test
	public void testCompoundLowLevelAbstractionCategorization() {
		ThresholdsOperator op = new ThresholdsOperator();
		op.setName("any");

		SystemProposition primParam1 = new SystemProposition();
		primParam1.setId(1L);
		primParam1.setKey("test-primparam1");
		primParam1.setInSystem(true);
		primParam1.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		SystemProposition primParam2 = new SystemProposition();
		primParam2.setId(2L);
		primParam2.setKey("test-primparam2");
		primParam2.setInSystem(true);
		primParam2.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		ValueThresholdGroupEntity clla1 = new ValueThresholdGroupEntity();
		clla1.setId(1L);
		clla1.setKey("test-clla1");
		clla1.setInSystem(false);
		clla1.setThresholdsOperator(op);
		clla1.setValueThresholds(new ArrayList<ValueThresholdEntity>(2));

		ValueThresholdEntity vte1 = new ValueThresholdEntity();
		vte1.setAbstractedFrom(primParam1);
		ValueThresholdEntity vte2 = new ValueThresholdEntity();
		vte2.setAbstractedFrom(primParam2);
		clla1.getValueThresholds().add(vte1);
		clla1.getValueThresholds().add(vte2);

		ValueThresholdGroupEntity clla2 = new ValueThresholdGroupEntity();
		clla2.setId(2L);
		clla2.setKey("test-clla2");
		clla2.setInSystem(false);
		clla2.setThresholdsOperator(op);
		clla2.setValueThresholds(new ArrayList<ValueThresholdEntity>(2));
		ValueThresholdEntity vte3 = new ValueThresholdEntity();
		vte3.setAbstractedFrom(primParam1);
		ValueThresholdEntity vte4 = new ValueThresholdEntity();
		vte4.setAbstractedFrom(primParam2);
		clla1.getValueThresholds().add(vte3);
		clla1.getValueThresholds().add(vte4);

		CategoryEntity cllaCat1 = new CategoryEntity();
		cllaCat1.setId(3L);
		cllaCat1.setKey("test-clla-cat1");
		cllaCat1.setDisplayName("test-clla-cat1-display");
		cllaCat1.setDescription("test-clla-cat1-abbrev");
		cllaCat1.setCategoryType(CategoryType.COMPOUND_LOW_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(clla1);
		iia1.add(clla2);
		cllaCat1.setMembers(iia1);

		List<PropositionDefinition> cllaDefs1 = this.converter.convert(cllaCat1);
		assertEquals("wrong number of proposition definitions created", 7,
				cllaDefs1.size());
		PropositionDefinition cllaDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				cllaDef1 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-clla-cat1"), 
				cllaDef1.getId());
		assertEquals("wrong display name", "test-clla-cat1-display",
				cllaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-clla-cat1-abbrev",
				cllaDef1.getDescription());
		assertEquals("wrong inverse-is-a size", 2,
				cllaDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(cllaDef1.getInverseIsA()[0].equals(toPropositionId("test-clla1")) &&
						cllaDef1
						.getInverseIsA()[1].equals(toPropositionId("test-clla2")))
						|| (cllaDef1.getInverseIsA()[0].equals(toPropositionId("test-clla2"))
						&& cllaDef1
						.getInverseIsA()[1].equals(toPropositionId("test-clla1"))));

		CategoryEntity cllaCat2 = new CategoryEntity();
		cllaCat2.setId(4L);
		cllaCat2.setKey("test-clla-cat2");
		cllaCat2.setDisplayName("test-clla-cat2-display");
		cllaCat2.setDescription("test-clla-cat2-abbrev");
		cllaCat2.setCategoryType(CategoryType.COMPOUND_LOW_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(cllaCat1);
		cllaCat2.setMembers(iia2);

		List<PropositionDefinition> cllaDefs2 = this.converter.convert(cllaCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				cllaDefs2.size());
		PropositionDefinition cllaDef2 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				cllaDef2 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-clla-cat2"), 
				cllaDef2.getId());
		assertEquals("wrong display name", "test-clla-cat2-display",
				cllaDef2.getDisplayName());
		assertEquals("wrong description", "test-clla-cat2-abbrev",
				cllaDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				cllaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", toPropositionId("test-clla-cat1"),
				cllaDef2.getInverseIsA()[0]);
	}

	@Test
	public void testMixedCategorization() {
		SystemProposition event = new SystemProposition();
		event.setId(1L);
		event.setKey("test-event1");
		event.setInSystem(true);
		event.setSystemType(SystemType.EVENT);

		SystemProposition primParam = new SystemProposition();
		primParam.setId(2L);
		primParam.setKey("test-primparam1");
		primParam.setInSystem(true);
		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);

		CategoryEntity mixedCat1 = new CategoryEntity();
		mixedCat1.setId(3L);
		mixedCat1.setKey("test-mixed-cat1");
		mixedCat1.setDisplayName("test-mixed-cat1-display");
		mixedCat1.setDescription("test-mixed-cat1-abbrev");
		mixedCat1.setCategoryType(CategoryType.HIGH_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia1 = new ArrayList<>();
		iia1.add(event);
		iia1.add(primParam);
		mixedCat1.setMembers(iia1);

		List<PropositionDefinition> hlaDefs1 = this.converter.convert
				(mixedCat1);
		assertEquals("wrong number of proposition definitions created", 1,
				hlaDefs1.size());
		PropositionDefinition hlaDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not a high-level abstraction",
				hlaDef1 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-mixed-cat1"), 
				hlaDef1.getId());
		assertEquals("wrong display name", "test-mixed-cat1-display",
				hlaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-mixed-cat1-abbrev",
				hlaDef1.getDescription());
		assertEquals("wrong size of inverse-is-a", 2,
				hlaDef1.getInverseIsA().length);
		Set<String> inverseIsASet = Arrays.asSet(hlaDef1.getInverseIsA());
		Set<String> expected = Arrays.asSet(new String[]{"test-event1", "test-primparam1"});
		assertEquals("inverse-is-a missing", expected, inverseIsASet);

		CategoryEntity mixedCat2 = new CategoryEntity();
		mixedCat2.setId(4L);
		mixedCat2.setKey("test-mixed-cat2");
		mixedCat2.setDisplayName("test-mixed-cat2-display");
		mixedCat2.setDescription("test-mixed-cat2-abbrev");
		mixedCat2.setCategoryType(CategoryType.HIGH_LEVEL_ABSTRACTION);
		List<PhenotypeEntity> iia2 = new ArrayList<>();
		iia2.add(mixedCat1);
		mixedCat2.setMembers(iia2);

		List<PropositionDefinition> hlaDefs2 = this.converter.convert
				(mixedCat2);
		assertEquals("wrong number of proposition definitions created", 1,
				hlaDefs2.size());
		PropositionDefinition hlaDef2 = this.converter.getPrimaryPropositionDefinition();
		assertTrue("not an event", hlaDef2 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", 
				toPropositionId("test-mixed-cat2"), hlaDef2.getId());
		assertEquals("wrong display name", "test-mixed-cat2-display",
				hlaDef2.getDisplayName());
		assertEquals("wrong description", "test-mixed-cat2-abbrev",
				hlaDef2.getDescription());
		assertEquals("wrong inverse-is-a size", 1,
				hlaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", 
				toPropositionId("test-mixed-cat1"),
				hlaDef2.getInverseIsA()[0]);
	}
}
