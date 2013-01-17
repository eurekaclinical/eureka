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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

public class ValueThresholdsCompoundLowLevelAbstractionConverterTest extends
        AbstractServiceTest {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private ValueThresholdsCompoundLowLevelAbstractionConverter converter;

	@Before
	public void setUp() {
		converterVisitor = this
		        .getInstance(PropositionDefinitionConverterVisitor.class);
		converter = new ValueThresholdsCompoundLowLevelAbstractionConverter();
		converter.setConverterVisitor(converterVisitor);
	}

	@Test
	public void testCompoundLowLevelAbstractionConverter() {
		ThresholdsOperator op = new ThresholdsOperator();
		op.setName("all");

		ValueComparator eq = new ValueComparator();
		eq.setName("=");
		ValueComparator ne = new ValueComparator();
		ne.setName("!=");
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

		ValueThresholdGroupEntity thresholdGroup = new ValueThresholdGroupEntity();
		thresholdGroup.setId(3L);
		thresholdGroup.setKey("test-valuethreshold");

		ValueThresholdEntity threshold1 = new ValueThresholdEntity();
		threshold1.setAbstractedFrom(primParam1);
		threshold1.setMinValueThreshold(BigDecimal.valueOf(100));
		threshold1.setMinValueComp(gt);
		threshold1.setMaxValueThreshold(BigDecimal.valueOf(200));
		threshold1.setMaxValueComp(lt);

		ValueThresholdEntity threshold2 = new ValueThresholdEntity();
		threshold2.setAbstractedFrom(primParam2);
		threshold2.setMinValueThreshold(BigDecimal.valueOf(5));
		threshold2.setMinValueComp(eq);

		List<ValueThresholdEntity> thresholds = new ArrayList<ValueThresholdEntity>();
		thresholds.add(threshold1);
		thresholds.add(threshold2);
		thresholdGroup.setValueThresholds(thresholds);
		thresholdGroup.setThresholdsOperator(op);

		List<PropositionDefinition> propDefs = this.converter
		        .convert(thresholdGroup);
		assertEquals("wrong number of proposition definitions created", 5,
		        propDefs.size());
		List<LowLevelAbstractionDefinition> llaDefs = new ArrayList<LowLevelAbstractionDefinition>();
		for (PropositionDefinition pd : propDefs) {
			if (pd instanceof LowLevelAbstractionDefinition) {
				llaDefs.add((LowLevelAbstractionDefinition) pd);
			}
		}
		assertEquals(
		        "wrong number of low-level abstraction definitions created", 2,
		        llaDefs.size());
		LowLevelAbstractionDefinition llaDef = llaDefs.get(0);
		assertEquals("wrong id for first low-level abstraction definition",
		        "test-primparam1_CLASSIFICATION", llaDef.getId());
		assertEquals(
		        "wrong number of abstracted from for first low-level abstraction definition",
		        1, llaDef.getAbstractedFrom().size());
		assertEquals(
		        "wrong abstracted from for first low-level abstraction definition",
		        "test-primparam1", llaDef.getAbstractedFrom().iterator().next());
		String userConstraintName = "test-primparam1_CLASSIFICATION_VALUE";
		String compConstraintName = userConstraintName + "_COMP";
		assertTrue("value def '" + userConstraintName + "' does not exist",
		        llaDef.getValueDefinition(userConstraintName) != null);
		assertEquals("wrong min value for value def: '" + userConstraintName
		        + "'", NumberValue.getInstance(100L),
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterValue("minThreshold"));
		assertEquals(
		        "wrong min value comp for value def: '" + userConstraintName
		                + "'",
		        ">",
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterComp("minThreshold").getComparatorString());
		assertEquals("wrong max value for value def: '" + userConstraintName
		        + "'", NumberValue.getInstance(200L),
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterValue("maxThreshold"));
		assertEquals(
		        "wrong max value comp for value def: '" + userConstraintName
		                + "'",
		        "<",
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterComp("maxThreshold").getComparatorString());
		assertTrue("value def '" + compConstraintName + "' does not exist",
		        llaDef.getValueDefinition(compConstraintName) != null);
		assertEquals("wrong min value for value def: '" + compConstraintName
		        + "'", NumberValue.getInstance(200L),
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterValue("minThreshold"));
		assertEquals(
		        "wrong min value comp for value def: '" + compConstraintName
		                + "'",
		        ">=",
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterComp("minThreshold").getComparatorString());
		assertEquals("wrong max value for value def: '" + compConstraintName
		        + "'", NumberValue.getInstance(100L),
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterValue("maxThreshold"));
		assertEquals(
		        "wrong max value comp for value def: '" + compConstraintName
		                + "'",
		        "<=",
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterComp("maxThreshold").getComparatorString());

		llaDef = llaDefs.get(1);
		assertEquals("wrong id for first low-level abstraction definition",
		        "test-primparam2_CLASSIFICATION", llaDef.getId());
		assertEquals(
		        "wrong number of abstracted from for first low-level abstraction definition",
		        1, llaDef.getAbstractedFrom().size());
		assertEquals(
		        "wrong abstracted from for first low-level abstraction definition",
		        "test-primparam2", llaDef.getAbstractedFrom().iterator().next());
		userConstraintName = "test-primparam2_CLASSIFICATION_VALUE";
		compConstraintName = userConstraintName + "_COMP";
		assertTrue("value def '" + userConstraintName + "' does not exist",
		        llaDef.getValueDefinition(userConstraintName) != null);
		assertEquals("wrong min value for value def: '" + userConstraintName
		        + "'", NumberValue.getInstance(5L),
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterValue("minThreshold"));
		assertEquals(
		        "wrong min value comp for value def: '" + userConstraintName
		                + "'",
		        "=",
		        llaDef.getValueDefinition(userConstraintName)
		                .getParameterComp("minThreshold").getComparatorString());
		assertTrue("max value for value def: '" + userConstraintName
		        + "' exists",
		        false == llaDef.getValueDefinition(userConstraintName)
		                .getParameters().contains("maxThreshold"));
		assertTrue("max value comp for value def: '" + userConstraintName
		        + "' exists",
		        false == llaDef.getValueDefinition(userConstraintName)
		                .getParameterComps().contains("maxThreshold"));
		assertTrue("value def '" + compConstraintName + "' does not exist",
		        llaDef.getValueDefinition(compConstraintName) != null);
		assertTrue("min value for value def: '" + compConstraintName
		        + "' exists",
		        false == llaDef.getValueDefinition(compConstraintName)
		                .getParameters().contains("minThreshold"));
		assertTrue("min value comp for value def: '" + compConstraintName
		        + "' exists",
		        false == llaDef.getValueDefinition(compConstraintName)
		                .getParameterComps().contains("minThreshold"));
		assertEquals("wrong max value for value def: '" + compConstraintName
		        + "'", NumberValue.getInstance(5L),
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterValue("maxThreshold"));
		assertEquals(
		        "wrong max value comp for value def: '" + compConstraintName
		                + "'",
		        "!=",
		        llaDef.getValueDefinition(compConstraintName)
		                .getParameterComp("maxThreshold").getComparatorString());

		CompoundLowLevelAbstractionDefinition cllaDef = this.converter
		        .getPrimaryPropositionDefinition();
		assertEquals("wrong id", "test-valuethreshold", cllaDef.getId());
		assertEquals("wrong minimum number of values", 1,
		        cllaDef.getMinimumNumberOfValues());
		assertEquals("wrong value def match operator", "ALL", cllaDef
		        .getValueDefinitionMatchOperator().name());
		assertEquals("wrong number of abstracted from", 2,
		        cllaDef.getAbstractedFrom().size());
		String[] af = new String[cllaDef.getAbstractedFrom().size()];
		cllaDef.getAbstractedFrom().toArray(af);
		assertTrue(
		        "wrong abstracted from",
		        (af[0].equals("test-primparam1_CLASSIFICATION")
		                && af[1].equals("test-primparam2_CLASSIFICATION") || (af[0]
		                .equals("test-primparam2_CLASSIFICATION") && af[1]
		                .equals("test-primparam1_CLASSIFICATION"))));

	}
}
