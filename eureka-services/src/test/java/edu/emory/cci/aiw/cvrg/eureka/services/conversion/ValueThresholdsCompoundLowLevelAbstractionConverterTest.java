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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValueThresholdsCompoundLowLevelAbstractionConverterTest extends
        AbstractServiceTest {
	
	private List<PropositionDefinition> propDefs;
	private List<LowLevelAbstractionDefinition> llaDefs;
	private LowLevelAbstractionDefinition firstLlaDef;
	private LowLevelAbstractionDefinition secondLlaDef;
	private CompoundLowLevelAbstractionDefinition cllaDef;
	private String userConstraintName;
	private String compConstraintName;
	private String secondUserConstraintName;
	private String secondCompConstraintName;
	private ValueThresholdGroupEntity thresholdGroup;

	@Before
	public void setUp() {
		PropositionDefinitionConverterVisitor converterVisitor = this
		        .getInstance(PropositionDefinitionConverterVisitor.class);
		ValueThresholdsCompoundLowLevelAbstractionConverter converter = 
				new ValueThresholdsCompoundLowLevelAbstractionConverter();
		converter.setConverterVisitor(converterVisitor);
		
		ThresholdsOperator op = new ThresholdsOperator();
		op.setName("all");

		ValueComparator eq = new ValueComparator();
		eq.setName("=");
		ValueComparator ne = new ValueComparator();
		ne.setName("not=");
		ValueComparator lt = new ValueComparator();
		lt.setName("<");
		ValueComparator gte = new ValueComparator();
		gte.setName(">=");
		ValueComparator gt = new ValueComparator();
		gt.setName(">");
		ValueComparator lte = new ValueComparator();
		lte.setName("<=");
		eq.setComplement(ne);
		ne.setComplement(eq);
		lt.setComplement(gte);
		gte.setComplement(lt);
		gt.setComplement(lte);
		lte.setComplement(gt);

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

		thresholdGroup = new ValueThresholdGroupEntity();
		thresholdGroup.setId(3L);
		thresholdGroup.setKey("test-valuethreshold");

		ValueThresholdEntity threshold1 = new ValueThresholdEntity();
		threshold1.setAbstractedFrom(primParam1);
		threshold1.setMinValueThreshold(BigDecimal.valueOf(100));
		threshold1.setMinValueComp(gt);
		threshold1.setMaxValueThreshold(BigDecimal.valueOf(200));
		threshold1.setMaxValueComp(lt);
		threshold1.setId(1L);

		ValueThresholdEntity threshold2 = new ValueThresholdEntity();
		threshold2.setAbstractedFrom(primParam2);
		threshold2.setMinValueThreshold(BigDecimal.valueOf(5));
		threshold2.setMinValueComp(eq);
		threshold2.setId(2L);

		List<ValueThresholdEntity> thresholds = new ArrayList<>();
		thresholds.add(threshold1);
		thresholds.add(threshold2);
		thresholdGroup.setValueThresholds(thresholds);
		thresholdGroup.setThresholdsOperator(op);

		propDefs = converter
		        .convert(thresholdGroup);
		llaDefs = new ArrayList<>();
		for (PropositionDefinition pd : propDefs) {
			if (pd instanceof LowLevelAbstractionDefinition) {
				llaDefs.add((LowLevelAbstractionDefinition) pd);
			}
		}
		firstLlaDef = llaDefs.get(0);
		secondLlaDef = llaDefs.get(1);
		cllaDef = converter.getPrimaryPropositionDefinition();
		userConstraintName = thresholdGroup.getKey() + "_VALUE";
		compConstraintName = userConstraintName + "_COMP";
		secondUserConstraintName = thresholdGroup.getKey() + "_VALUE";
		secondCompConstraintName = secondUserConstraintName + "_COMP";
	}

	@Test
	public void testNumberOfPropositionDefinitionsCreated() {
		assertEquals("wrong number of proposition definitions created", 3,
		        propDefs.size());
	}
	
	@Test
	public void testNumberOfLowLevelAbstractionDefinitionsCreated() {
		assertEquals(
		        "wrong number of low-level abstraction definitions created", 2,
		        llaDefs.size());
	}
	
	@Test
	public void testIdForFirstLLAD() {
		assertEquals("wrong id for first low-level abstraction definition",
		        thresholdGroup.getKey() + "_SUB1", firstLlaDef.getId());
	}
	
	@Test
	public void testNumberOfAbstractedFromForFirstLLAD() {
		assertEquals(
		        "wrong number of abstracted from for first low-level abstraction definition",
		        1, firstLlaDef.getAbstractedFrom().size());
	}
	
	@Test
	public void testAbstractedFromForFirstLLAD() {
		assertEquals(
		        "wrong abstracted from for first low-level abstraction definition",
		        "test-primparam1", firstLlaDef.getAbstractedFrom().iterator().next());
	}
	
	@Test
	public void testValueDefExists() {
		List<String> ids = new ArrayList<>();
		for (LowLevelAbstractionValueDefinition vd : firstLlaDef.getValueDefinitions()) {
			ids.add(vd.getId());
		}
		assertTrue("value def '" + userConstraintName + 
				"' does not exist; value definition ids: " +
				StringUtils.join(ids, ", "),
		        firstLlaDef.getValueDefinition(userConstraintName) != null);
	}
	
	@Test
	public void testValueDefMinValue() {
		assertEquals("wrong min value for value def: '" + userConstraintName
		        + "'", NumberValue.getInstance(100L),
		        firstLlaDef.getValueDefinition(userConstraintName)
		                .getParameterValue("minThreshold"));
	}
	
	@Test
	public void testValueDefMinGT() {
		assertEquals(
		        "wrong min value comp for value def: '" + userConstraintName
		                + "'",
		        ">",
		        firstLlaDef.getValueDefinition(userConstraintName)
				.getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testValueDefMaxValue() {
		assertEquals("wrong max value for value def: '" + userConstraintName
		        + "'", NumberValue.getInstance(200L),
		        firstLlaDef.getValueDefinition(userConstraintName)
		                .getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testValueDefMaxLT() {
		assertEquals(
		        "wrong max value comp for value def: '" + userConstraintName
		                + "'",
		        "<",
		        firstLlaDef.getValueDefinition(userConstraintName)
		        .getParameterComp("maxThreshold").getComparatorString());
	}
	
	@Test
	public void testValueDefCompExists() {
		assertTrue("value def '" + compConstraintName + "' does not exist",
		        firstLlaDef.getValueDefinition(compConstraintName) != null);
	}
	
	@Test
	public void testValueDefCompMinValue() {
		assertEquals("wrong min value for value def: '" + compConstraintName
		        + "'", NumberValue.getInstance(200L),
		        firstLlaDef.getValueDefinition(compConstraintName)
		                .getParameterValue("minThreshold"));
	}
	
	@Test
	public void testValueDefCompGE() {
		assertEquals(
		        "wrong min value comp for value def: '" + compConstraintName
		                + "'",
		        ">=",
		        firstLlaDef.getValueDefinition(compConstraintName)
		        .getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testValueDefCompMaxValue() {
		assertEquals("wrong max value for value def: '" + compConstraintName
		        + "'", NumberValue.getInstance(100L),
		        firstLlaDef.getValueDefinition(compConstraintName)
		                .getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testValueDefCompLE() {
		assertEquals(
		        "wrong max value comp for value def: '" + compConstraintName
		                + "'",
		        "<=",
		        firstLlaDef.getValueDefinition(compConstraintName)
		        .getParameterComp("maxThreshold").getComparatorString());
	}
	
	@Test
	public void testSecondLLADId() {
		assertEquals("wrong id for first low-level abstraction definition",
		        thresholdGroup.getKey() + "_SUB2", secondLlaDef.getId());
	}
	
	@Test
	public void testSecondLLADNumberOfAbstractedFrom() {
		assertEquals(
		        "wrong number of abstracted from for first low-level abstraction definition",
		        1, secondLlaDef.getAbstractedFrom().size());
	}
	
	@Test
	public void testSecondLLADAbstractedFrom() {
		assertEquals(
		        "wrong abstracted from for first low-level abstraction definition",
		        "test-primparam2", secondLlaDef.getAbstractedFrom().iterator().next());
	}
	
	@Test
	public void testSecondLLADValueDefExists() {
		assertTrue("value def '" + secondUserConstraintName + "' does not exist",
		        secondLlaDef
				.getValueDefinition(secondUserConstraintName) != null);
	}
	
	@Test
	public void testSecondLLADMinValue() {
		assertEquals("wrong min value for value def: '" + secondUserConstraintName
		        + "'", NumberValue.getInstance(5L),
		        secondLlaDef.getValueDefinition(secondUserConstraintName)
		                .getParameterValue("minThreshold"));
	}
	
	@Test
	public void testSecondLLADMinEQ() {
		assertEquals(
		        "wrong min value comp for value def: '" + secondUserConstraintName
		                + "'",
		        "=",
		        secondLlaDef.getValueDefinition(secondUserConstraintName)
		        .getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testSecondLLADMaxThresholdParamExists() {
		assertTrue("max value for value def: '" + secondUserConstraintName
		        + "' exists",
		        false == secondLlaDef.getValueDefinition(secondUserConstraintName)
		                .getParameters().contains("maxThreshold"));
	}
	
	@Test
	public void testSecondLLADMaxThresholdCompParamExists() {
		assertTrue("max value comp for value def: '" + secondUserConstraintName
		        + "' exists",
		        false == secondLlaDef.getValueDefinition(secondUserConstraintName)
		                .getParameterComps().contains("maxThreshold"));
	}
	
	@Test
	public void testSecondLLADCompValueDefExists() {
		assertTrue("value def '" + secondCompConstraintName + "' does not exist",
		        secondLlaDef.getValueDefinition(secondCompConstraintName) != null);
	}
	
	@Test
	public void testSecondLLADCompMinThresholdExists() {
		assertTrue("min value for value def: '" + secondCompConstraintName
		        + "' exists",
		        false == secondLlaDef.getValueDefinition(secondCompConstraintName)
		                .getParameters().contains("minThreshold"));
	}
	
	@Test
	public void testSecondLLADCompMinThresholdCompParamExists() {
		assertTrue("min value comp for value def: '" + secondCompConstraintName
		        + "' exists",
		        false == secondLlaDef.getValueDefinition(secondCompConstraintName)
		                .getParameterComps().contains("minThreshold"));
	}
	
	@Test
	public void testSecondLLADCompMaxValue() {
		assertEquals("wrong max value for value def: '" + secondCompConstraintName
		        + "'", NumberValue.getInstance(5L),
		        secondLlaDef.getValueDefinition(secondCompConstraintName)
		                .getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testSecondLLADCompNE() {
		assertEquals(
		        "wrong max value comp for value def: '" + secondCompConstraintName
		                + "'",
		        "!=",
		        secondLlaDef.getValueDefinition(secondCompConstraintName)
		                .getParameterComp("maxThreshold").getComparatorString());	
	}
	
	@Test
	public void testThresholdGroupId() {
		assertEquals("wrong id", thresholdGroup.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX, cllaDef.getId());
	}
	
	@Test
	public void testMinimumNumberOfValues() {
		assertEquals("wrong minimum number of values", 1,
		        cllaDef.getMinimumNumberOfValues());
	}
	
	@Test
	public void testThresholdsOperator() {
		assertEquals("wrong value def match operator", "ALL", cllaDef
		        .getValueDefinitionMatchOperator().name());
	}
	
	@Test
	public void testNumberOfAbstractedFrom() {
		assertEquals("wrong number of abstracted from", 2,
		        cllaDef.getAbstractedFrom().size());
	}
	
	@Test
	public void testAbstractedFrom() {
		String[] af = new String[cllaDef.getAbstractedFrom().size()];
		cllaDef.getAbstractedFrom().toArray(af);
		assertTrue(
		        "wrong abstracted from",
		        (af[0].equals("test-valuethreshold_SUB1")
		                && af[1].equals("test-valuethreshold_SUB2") || (af[0]
		                .equals("test-valuethreshold_SUB2") && af[1]
		                .equals("test-valuethreshold_SUB1"))));
	}
}
