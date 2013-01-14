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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategorizationType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.junit.Before;
import org.junit.Test;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategorizationConverterTest {

	private CategorizationConverter converter;

	@Before
	public void setup() {
		this.converter = new CategorizationConverter();

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
		eventCat1.setAbbrevDisplayName("test-event-cat1-abbrev");
		eventCat1.setCategorizationType(CategorizationType.EVENT);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(event1);
		iia1.add(event2);
		eventCat1.setInverseIsA(iia1);

		List<PropositionDefinition> eventDefs1 = this.converter.convert
				(eventCat1);
		assertEquals("wrong number of proposition definitions created", 1,
				eventDefs1.size());
		PropositionDefinition eventDef1 = this.converter
				.getPrimaryPropositionDefinition();
		assertTrue("not an event", eventDef1 instanceof EventDefinition);
		assertEquals("wrong ID", "test-event-cat1", eventDef1.getId());
		assertEquals("wrong display name", "test-event-cat1-display",
		        eventDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-event-cat1-abbrev",
		        eventDef1.getAbbreviatedDisplayName());
		assertEquals("wrong size of inverse-is-a", 2,
		        eventDef1.getInverseIsA().length);
		assertTrue(
				"inverse-is-a missing",
				(eventDef1.getInverseIsA()[0].equals("test-event1") && eventDef1
						.getInverseIsA()[1].equals("test-event2"))
						|| (eventDef1.getInverseIsA()[0].equals("test-event2") && eventDef1
						.getInverseIsA()[1].equals("test-event1")));

		CategoryEntity eventCat2 = new CategoryEntity();
		eventCat2.setId(4L);
		eventCat2.setKey("test-event-cat2");
		eventCat2.setDisplayName("test-event-cat2-display");
		eventCat2.setAbbrevDisplayName("test-event-cat2-abbrev");
		eventCat2.setCategorizationType(CategorizationType.EVENT);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(eventCat1);
		eventCat2.setInverseIsA(iia2);

		PropositionDefinition eventDef2 = this.converter.convert(eventCat2)
				.get(0);
		assertTrue("not an event", eventDef2 instanceof EventDefinition);
		assertEquals("wrong ID", "test-event-cat2", eventDef2.getId());
		assertEquals("wrong display name", "test-event-cat2-display",
		        eventDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-event-cat2-abbrev",
		        eventDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        eventDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3", eventDef2.getInverseIsA()[0]);
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
		constantCat1.setAbbrevDisplayName("test-constant-cat1-abbrev");
		constantCat1.setCategorizationType(CategorizationType.CONSTANT);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(constant1);
		iia1.add(constant2);
		constantCat1.setInverseIsA(iia1);

		PropositionDefinition constantDef1 = this.converter.convert
				(constantCat1).get(0);
		assertTrue("not a constant", constantDef1 instanceof ConstantDefinition);
		assertEquals("wrong ID", "test-constant-cat1", constantDef1.getId());
		assertEquals("wrong display name", "test-constant-cat1-display",
		        constantDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-constant-cat1-abbrev",
		        constantDef1.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 2,
		        constantDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(constantDef1.getInverseIsA()[0].equals("test-constant1") && constantDef1
						.getInverseIsA()[1].equals("test-constant2"))
						|| (constantDef1.getInverseIsA()[0]
						.equals("test-constant2") && constantDef1
						.getInverseIsA()[1].equals("test-constant1")));

		CategoryEntity constantCat2 = new CategoryEntity();
		constantCat2.setId(4L);
		constantCat2.setKey("test-constant-cat2");
		constantCat2.setDisplayName("test-constant-cat2-display");
		constantCat2.setAbbrevDisplayName("test-constant-cat2-abbrev");
		constantCat2.setCategorizationType(CategorizationType.CONSTANT);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(constantCat1);
		constantCat2.setInverseIsA(iia2);

		PropositionDefinition constantDef2 = this.converter.convert
				(constantCat2).get(0);
		assertTrue("not a constant", constantDef2 instanceof ConstantDefinition);
		assertEquals("wrong ID", "test-constant-cat2", constantDef2.getId());
		assertEquals("wrong display name", "test-constant-cat2-display",
		        constantDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-constant-cat2-abbrev",
		        constantDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        constantDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3", constantDef2.getInverseIsA()[0]);

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
		primParamCat1.setAbbrevDisplayName("test-primparam-cat1-abbrev");
		primParamCat1
		        .setCategorizationType(CategorizationType.PRIMITIVE_PARAMETER);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(primParam1);
		iia1.add(primParam2);
		primParamCat1.setInverseIsA(iia1);

		PropositionDefinition primParamDef1 = this.converter.convert
				(primParamCat1).get(0);
		assertTrue("not a primitive parameter",
				primParamDef1 instanceof PrimitiveParameterDefinition);
		assertEquals("wrong ID", "test-primparam-cat1", primParamDef1.getId());
		assertEquals("wrong display name", "test-primparam-cat1-display",
		        primParamDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-primparam-cat1-abbrev",
		        primParamDef1.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 2,
		        primParamDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(primParamDef1.getInverseIsA()[0].equals("test-primparam1") && primParamDef1
						.getInverseIsA()[1].equals("test-primparam2"))
						|| (primParamDef1.getInverseIsA()[0]
						.equals("test-primparam2") && primParamDef1
						.getInverseIsA()[1].equals("test-primparam1")));

		CategoryEntity primParamCat2 = new CategoryEntity();
		primParamCat2.setId(4L);
		primParamCat2.setKey("test-primparam-cat2");
		primParamCat2.setDisplayName("test-primparam-cat2-display");
		primParamCat2.setAbbrevDisplayName("test-primparam-cat2-abbrev");
		primParamCat2
		        .setCategorizationType(CategorizationType.PRIMITIVE_PARAMETER);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(primParamCat1);
		primParamCat2.setInverseIsA(iia2);

		PropositionDefinition primParamDef2 = this.converter.convert
				(primParamCat2).get(0);
		assertTrue("not a primitve parameter",
		        primParamDef2 instanceof PrimitiveParameterDefinition);
		assertEquals("wrong ID", "test-primparam-cat2", primParamDef2.getId());
		assertEquals("wrong display name", "test-primparam-cat2-display",
		        primParamDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-primparam-cat2-abbrev",
		        primParamDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        primParamDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3",
		        primParamDef2.getInverseIsA()[0]);
	}

	@Test
	public void testHighLevelAbstractionCategorization() {
		SequenceEntity hla1 = new SequenceEntity();
		hla1.setId(1L);
		hla1.setKey("test-hla1");
		hla1.setInSystem(false);

		SequenceEntity hla2 = new SequenceEntity();
		hla2.setId(2L);
		hla2.setKey("test-hla2");
		hla2.setInSystem(false);

		CategoryEntity hlaCat1 = new CategoryEntity();
		hlaCat1.setId(3L);
		hlaCat1.setKey("test-hla-cat1");
		hlaCat1.setDisplayName("test-hla-cat1-display");
		hlaCat1.setAbbrevDisplayName("test-hla-cat1-abbrev");
		hlaCat1.setCategorizationType(CategorizationType.HIGH_LEVEL_ABSTRACTION);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(hla1);
		iia1.add(hla2);
		hlaCat1.setInverseIsA(iia1);

		PropositionDefinition hlaDef1 = this.converter.convert(hlaCat1).get(0);
		assertTrue("not a high-level abstraction",
				hlaDef1 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", "test-hla-cat1", hlaDef1.getId());
		assertEquals("wrong display name", "test-hla-cat1-display",
		        hlaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-hla-cat1-abbrev",
		        hlaDef1.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 2,
		        hlaDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(hlaDef1.getInverseIsA()[0].equals("1") && hlaDef1
						.getInverseIsA()[1].equals("2"))
						|| (hlaDef1.getInverseIsA()[0].equals("2") && hlaDef1
						.getInverseIsA()[1].equals("1")));

		CategoryEntity hlaCat2 = new CategoryEntity();
		hlaCat2.setId(4L);
		hlaCat2.setKey("test-hla-cat2");
		hlaCat2.setDisplayName("test-hla-cat2-display");
		hlaCat2.setAbbrevDisplayName("test-hla-cat2-abbrev");
		hlaCat2.setCategorizationType(CategorizationType.HIGH_LEVEL_ABSTRACTION);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(hlaCat1);
		hlaCat2.setInverseIsA(iia2);

		PropositionDefinition hlaDef2 = this.converter.convert(hlaCat2).get(0);
		assertTrue("not a high-level abstraction",
		        hlaDef2 instanceof HighLevelAbstractionDefinition);
		assertEquals("wrong ID", "test-hla-cat2", hlaDef2.getId());
		assertEquals("wrong display name", "test-hla-cat2-display",
		        hlaDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-hla-cat2-abbrev",
		        hlaDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        hlaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3", hlaDef2.getInverseIsA()[0]);
	}

	@Test
	public void testSliceAbstractionCategorization() {
		FrequencyEntity sa1 = new FrequencyEntity();
		sa1.setId(1L);
		sa1.setKey("test-slice1");
		sa1.setInSystem(false);

		FrequencyEntity sa2 = new FrequencyEntity();
		sa2.setId(2L);
		sa2.setKey("test-slice2");
		sa2.setInSystem(false);

		CategoryEntity saCat1 = new CategoryEntity();
		saCat1.setId(3L);
		saCat1.setKey("test-slice-cat1");
		saCat1.setDisplayName("test-slice-cat1-display");
		saCat1.setAbbrevDisplayName("test-slice-cat1-abbrev");
		saCat1.setCategorizationType(CategorizationType.SLICE_ABSTRACTION);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(sa1);
		iia1.add(sa2);
		saCat1.setInverseIsA(iia1);

		PropositionDefinition saDef1 = this.converter.convert(saCat1).get(0);
		assertTrue("not a slice abstraction",
				saDef1 instanceof SliceDefinition);
		assertEquals("wrong ID", "test-slice-cat1", saDef1.getId());
		assertEquals("wrong display name", "test-slice-cat1-display",
		        saDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-slice-cat1-abbrev",
		        saDef1.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 2,
		        saDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(saDef1.getInverseIsA()[0].equals("1") && saDef1
						.getInverseIsA()[1].equals("2"))
						|| (saDef1.getInverseIsA()[0].equals("2") && saDef1
						.getInverseIsA()[1].equals("1")));

		CategoryEntity saCat2 = new CategoryEntity();
		saCat2.setId(4L);
		saCat2.setKey("test-slice-cat2");
		saCat2.setDisplayName("test-slice-cat2-display");
		saCat2.setAbbrevDisplayName("test-slice-cat2-abbrev");
		saCat2.setCategorizationType(CategorizationType.SLICE_ABSTRACTION);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(saCat1);
		saCat2.setInverseIsA(iia2);

		PropositionDefinition saDef2 = this.converter.convert(saCat2).get(0);
		assertTrue("not a slice abstraction",
		        saDef2 instanceof SliceDefinition);
		assertEquals("wrong ID", "test-slice-cat2", saDef2.getId());
		assertEquals("wrong display name", "test-slice-cat2-display",
		        saDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-slice-cat2-abbrev",
		        saDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        saDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3", saDef2.getInverseIsA()[0]);
	}

	@Test
	public void testLowLevelAbstractionCategorization() {
		ValueThresholdGroupEntity lla1 = new ValueThresholdGroupEntity();
		lla1.setId(1L);
		lla1.setKey("test-lla1");
		lla1.setInSystem(false);

		ValueThresholdGroupEntity lla2 = new ValueThresholdGroupEntity();
		lla2.setId(2L);
		lla2.setKey("test-lla2");
		lla2.setInSystem(false);

		CategoryEntity llaCat1 = new CategoryEntity();
		llaCat1.setId(3L);
		llaCat1.setKey("test-lla-cat1");
		llaCat1.setDisplayName("test-lla-cat1-display");
		llaCat1.setAbbrevDisplayName("test-lla-cat1-abbrev");
		llaCat1.setCategorizationType(CategorizationType.LOW_LEVEL_ABSTRACTION);
		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
		iia1.add(lla1);
		iia1.add(lla2);
		llaCat1.setInverseIsA(iia1);

		PropositionDefinition llaDef1 = this.converter.convert(llaCat1).get(0);
		assertTrue("not a low-level abstraction",
				llaDef1 instanceof LowLevelAbstractionDefinition);
		assertEquals("wrong ID", "test-lla-cat1", llaDef1.getId());
		assertEquals("wrong display name", "test-lla-cat1-display",
		        llaDef1.getDisplayName());
		assertEquals("wrong abbrev display name", "test-lla-cat1-abbrev",
		        llaDef1.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 2,
		        llaDef1.getInverseIsA().length);
		assertTrue(
				"wrong inverse-is-a objects",
				(llaDef1.getInverseIsA()[0].equals("1") && llaDef1
						.getInverseIsA()[1].equals("2"))
						|| (llaDef1.getInverseIsA()[0].equals("2") && llaDef1
						.getInverseIsA()[1].equals("1")));

		CategoryEntity llaCat2 = new CategoryEntity();
		llaCat2.setId(4L);
		llaCat2.setKey("test-lla-cat2");
		llaCat2.setDisplayName("test-lla-cat2-display");
		llaCat2.setAbbrevDisplayName("test-lla-cat2-abbrev");
		llaCat2.setCategorizationType(CategorizationType.LOW_LEVEL_ABSTRACTION);
		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
		iia2.add(llaCat1);
		llaCat2.setInverseIsA(iia2);

		PropositionDefinition llaDef2 = this.converter.convert(llaCat2).get(0);
		assertTrue("not a low-level abstraction",
		        llaDef2 instanceof LowLevelAbstractionDefinition);
		assertEquals("wrong ID", "test-lla-cat2", llaDef2.getId());
		assertEquals("wrong display name", "test-lla-cat2-display",
		        llaDef2.getDisplayName());
		assertEquals("wrong abbrev display name", "test-lla-cat2-abbrev",
		        llaDef2.getAbbreviatedDisplayName());
		assertEquals("wrong inverse-is-a size", 1,
		        llaDef2.getInverseIsA().length);
		assertEquals("wrong inverse-is-a", "3", llaDef2.getInverseIsA()[0]);
	}

	@Test
	public void testCompoundLowLevelAbstractionCategorization() {
//		CompoundValueThreshold clla1 = new CompoundValueThreshold();
//		clla1.setId(1L);
//		clla1.setKey("test-clla1");
//		clla1.setInSystem(false);
//
//		CompoundValueThreshold clla2 = new CompoundValueThreshold();
//		clla2.setId(2L);
//		clla2.setKey("test-clla2");
//		clla2.setInSystem(false);
//
//		CategoryEntity cllaCat1 = new CategoryEntity();
//		cllaCat1.setId(3L);
//		cllaCat1.setKey("test-clla-cat1");
//		cllaCat1.setDisplayName("test-clla-cat1-display");
//		cllaCat1.setAbbrevDisplayName("test-clla-cat1-abbrev");
//		cllaCat1.setCategorizationType(CategorizationType.COMPOUND_LOW_LEVEL_ABSTRACTION);
//		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
//		iia1.add(clla1);
//		iia1.add(clla2);
//		cllaCat1.setInverseIsA(iia1);
//
//		PropositionDefinition cllaDef1 = this.converter.convert(cllaCat1);
//		assertTrue("not a compound low-level abstraction",
//		        cllaDef1 instanceof CompoundLowLevelAbstractionDefinition);
//		assertEquals("wrong ID", "test-clla-cat1", cllaDef1.getId());
//		assertEquals("wrong display name", "test-clla-cat1-display",
//		        cllaDef1.getDisplayName());
//		assertEquals("wrong abbrev display name", "test-clla-cat1-abbrev",
//		        cllaDef1.getAbbreviatedDisplayName());
//		assertEquals("wrong inverse-is-a size", 2,
//		        cllaDef1.getInverseIsA().length);
//		assertTrue(
//		        "wrong inverse-is-a objects",
//		        (cllaDef1.getInverseIsA()[0].equals("1") && cllaDef1
//		                .getInverseIsA()[1].equals("2"))
//		                || (cllaDef1.getInverseIsA()[0].equals("2") && cllaDef1
//		                        .getInverseIsA()[1].equals("1")));
//
//		CategoryEntity cllaCat2 = new CategoryEntity();
//		cllaCat2.setId(4L);
//		cllaCat2.setKey("test-clla-cat2");
//		cllaCat2.setDisplayName("test-clla-cat2-display");
//		cllaCat2.setAbbrevDisplayName("test-clla-cat2-abbrev");
//		cllaCat2.setCategorizationType(CategorizationType.COMPOUND_LOW_LEVEL_ABSTRACTION);
//		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
//		iia2.add(cllaCat1);
//		cllaCat2.setInverseIsA(iia2);
//
//		PropositionDefinition cllaDef2 = this.converter.convert(cllaCat2);
//		assertTrue("not a compound low-level abstraction",
//		        cllaDef2 instanceof CompoundLowLevelAbstractionDefinition);
//		assertEquals("wrong ID", "test-clla-cat2", cllaDef2.getId());
//		assertEquals("wrong display name", "test-clla-cat2-display",
//		        cllaDef2.getDisplayName());
//		assertEquals("wrong abbrev display name", "test-clla-cat2-abbrev",
//		        cllaDef2.getAbbreviatedDisplayName());
//		assertEquals("wrong inverse-is-a size", 1,
//		        cllaDef2.getInverseIsA().length);
//		assertEquals("wrong inverse-is-a", "3", cllaDef2.getInverseIsA()[0]);
//	}
//
//	@Test
//	public void testMixedCategorization() {
//		SystemProposition event = new SystemProposition();
//		event.setId(1L);
//		event.setKey("test-event");
//		event.setInSystem(true);
//		event.setSystemType(SystemType.EVENT);
//
//		SystemProposition primParam = new SystemProposition();
//		primParam.setId(2L);
//		primParam.setKey("test-primparam");
//		primParam.setInSystem(true);
//		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);
//
//		CategoryEntity mixedCat1 = new CategoryEntity();
//		mixedCat1.setId(3L);
//		mixedCat1.setKey("test-mixed-cat1");
//		mixedCat1.setDisplayName("test-mixed-cat1-display");
//		mixedCat1.setAbbrevDisplayName("test-mixed-cat1-abbrev");
//		mixedCat1.setCategorizationType(CategorizationType.MIXED);
//		List<DataElementEntity> iia1 = new ArrayList<DataElementEntity>();
//		iia1.add(event);
//		iia1.add(primParam);
//		mixedCat1.setInverseIsA(iia1);
//
//		PropositionDefinition hlaDef1 = this.converter.convert(mixedCat1);
//		assertTrue("not a high-level abstraction",
//		        hlaDef1 instanceof HighLevelAbstractionDefinition);
//		assertEquals("wrong ID", "test-mixed-cat1", hlaDef1.getId());
//		assertEquals("wrong display name", "test-mixed-cat1-display",
//		        hlaDef1.getDisplayName());
//		assertEquals("wrong abbrev display name", "test-mixed-cat1-abbrev",
//		        hlaDef1.getAbbreviatedDisplayName());
//		assertEquals("wrong size of inverse-is-a", 2,
//		        hlaDef1.getInverseIsA().length);
//		assertTrue(
//		        "inverse-is-a missing",
//		        (hlaDef1.getInverseIsA()[0].equals("test-event") && hlaDef1
//		                .getInverseIsA()[1].equals("test-primparam"))
//		                || (hlaDef1.getInverseIsA()[0].equals("test-primparam") && hlaDef1
//		                        .getInverseIsA()[1].equals("test-event")));
//
//		CategoryEntity mixedCat = new CategoryEntity();
//		mixedCat.setId(4L);
//		mixedCat.setKey("test-mixed-cat2");
//		mixedCat.setDisplayName("test-mixed-cat2-display");
//		mixedCat.setAbbrevDisplayName("test-mixed-cat2-abbrev");
//		mixedCat.setCategorizationType(CategorizationType.MIXED);
//		List<DataElementEntity> iia2 = new ArrayList<DataElementEntity>();
//		iia2.add(mixedCat1);
//		mixedCat.setInverseIsA(iia2);
//
//		PropositionDefinition hlaDef2 = this.converter.convert(mixedCat);
//		assertTrue("not an event", hlaDef2 instanceof HighLevelAbstractionDefinition);
//		assertEquals("wrong ID", "test-mixed-cat2", hlaDef2.getId());
//		assertEquals("wrong display name", "test-mixed-cat2-display",
//		        hlaDef2.getDisplayName());
//		assertEquals("wrong abbrev display name", "test-mixed-cat2-abbrev",
//		        hlaDef2.getAbbreviatedDisplayName());
//		assertEquals("wrong inverse-is-a size", 1,
//		        hlaDef2.getInverseIsA().length);
//		assertEquals("wrong inverse-is-a", "3", hlaDef2.getInverseIsA()[0]);
	}
}
