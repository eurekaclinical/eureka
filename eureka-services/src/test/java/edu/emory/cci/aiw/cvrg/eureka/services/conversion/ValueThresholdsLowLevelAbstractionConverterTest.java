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
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValueThresholdsLowLevelAbstractionConverterTest extends
		AbstractServiceTest {

	private String userConstraintName;
	private String compConstraintName;
	private List<PropositionDefinition> llaDefs;
	private LowLevelAbstractionDefinition llaDef;

	@Before
	public void setUp() {
		PropositionDefinitionConverterVisitor converterVisitor = 
				this.getInstance(PropositionDefinitionConverterVisitor.class);
		ValueThresholdsLowLevelAbstractionConverter converter = 
				new ValueThresholdsLowLevelAbstractionConverter();
		converter.setConverterVisitor(converterVisitor);
		
		SystemProposition primParam = new SystemProposition();
		primParam.setId(1L);
		primParam.setKey("test-primparam1");
		primParam.setInSystem(true);
		primParam.setSystemType(
				SystemProposition.SystemType.PRIMITIVE_PARAMETER);

		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");

		ValueThresholdGroupEntity thresholdGroup = 
				new ValueThresholdGroupEntity();
		thresholdGroup.setId(2L);
		thresholdGroup.setKey("test-valuethreshold");
		userConstraintName = thresholdGroup.getKey() + "_VALUE";
		compConstraintName = thresholdGroup.getKey() + "_VALUE_COMP";

		ValueComparator lt = new ValueComparator();
		lt.setName("<");
		ValueComparator gt = new ValueComparator();
		gt.setName(">");
		ValueComparator lte = new ValueComparator();
		lte.setName("<=");
		ValueComparator gte = new ValueComparator();
		gte.setName(">=");
		lt.setComplement(gte);
		gte.setComplement(lt);
		gt.setComplement(lte);
		lte.setComplement(gt);

		ValueThresholdEntity threshold = new ValueThresholdEntity();
		threshold.setAbstractedFrom(primParam);
		threshold.setMinValueThreshold(BigDecimal.valueOf(100));
		threshold.setMinValueComp(gt);
		threshold.setMaxValueThreshold(BigDecimal.valueOf(200));
		threshold.setMaxValueComp(lt);

		List<ValueThresholdEntity> thresholds = new
				ArrayList<>();
		thresholds.add(threshold);
		thresholdGroup.setValueThresholds(thresholds);
		llaDefs = 
				converter.convert(thresholdGroup);
		llaDef = converter.getPrimaryPropositionDefinition();
	}

	@Test
	public void testNumberOfPropositionDefinitionsCreated() {
		assertEquals("wrong number of proposition definitions created", 1,
				llaDefs.size());
	}
	
	@Test
	public void testId() {
		assertEquals("wrong id", "test-valuethreshold" + 
				ConversionUtil.PRIMARY_PROP_ID_SUFFIX, llaDef.getId());
	}
	
	@Test
	public void testAbstractedFromSize() {
		assertEquals("wrong abstracted from size", 1, 
				llaDef.getAbstractedFrom().size());
	}
	
	@Test
	public void testAbstractedFrom() {
		assertEquals("wrong abstracted from", "test-primparam1",
				llaDef.getAbstractedFrom().iterator().next());
	}
	
	@Test
	public void testMinimumNumberOfValues() {
		assertEquals("wrong minimum number of values", 1, 
				llaDef.getMinimumNumberOfValues());
	}
	
	@Test
	public void testAlgorithm() {
		assertEquals("wrong algorithm", "stateDetector", 
				llaDef.getAlgorithmId());
	}
	
	@Test
	public void tesetNumberOfValueDefinitions() {
		assertEquals("wrong number of value definitions", 2, 
				llaDef.getValueDefinitions().size());
	}
	
	@Test
	public void testValueDefExists() {
		List<String> values = new ArrayList<>();
		for (LowLevelAbstractionValueDefinition llvd : 
				llaDef.getValueDefinitions()) {
			values.add(llvd.getId());
		}
		assertTrue("value def '" + 
				userConstraintName +
				"' does not exist; value defs that do exist are " + 
				StringUtils.join(values, ", "), 
				llaDef.getValueDefinition(userConstraintName) != null);
	}
	
	@Test
	public void testValueDefDoesNotExistComp() {
		List<String> values = new ArrayList<>();
		for (LowLevelAbstractionValueDefinition llvd : 
				llaDef.getValueDefinitions()) {
			values.add(llvd.getId());
		}
		assertTrue("value def '" + compConstraintName +
				"' does not exist; value defs that exist are " + 
				StringUtils.join(values, ", "), 
				llaDef.getValueDefinition(compConstraintName) != null);
	}
	
	@Test
	public void testMinValueThresholdForValueDef() {
		assertEquals("wrong min value threshold for value def: '" 
				+ userConstraintName + "'", 
				NumberValue.getInstance(100L), 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterValue("minThreshold"));
	}
	
	@Test
	public void testMinValueCompGTForValueDef() {
		assertEquals("wrong min value comp comparator for value def: '" + 
				userConstraintName + "'", 
				">", 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testMaxValueForValueDef() {
		assertEquals("wrong max value for value def: '" + 
				userConstraintName + "'", 
				NumberValue.getInstance(200L), 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testMaxValueLTForValueDef() {
		assertEquals("wrong max value comp for value def: '" + 
				userConstraintName + "'", 
				"<", 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterComp("maxThreshold").getComparatorString());
	}
	
	@Test
	public void testValueDef() {
		assertTrue("value def '" + compConstraintName +"' does not exist", 
				llaDef.getValueDefinition(compConstraintName) != null);
	}
	
	@Test
	public void testMinValueCompThresholdForValueDef() {
		assertEquals("wrong min value comp for value def: '" + 
				compConstraintName + "'", 
				NumberValue.getInstance(200L), 
				llaDef.getValueDefinition(compConstraintName)
				.getParameterValue("minThreshold"));
	}
	
	@Test
	public void testMinValueCompGEForValueDef() {
		assertEquals("wrong min value comp for value def: '" +
				compConstraintName + "'", 
				">=", 
				llaDef.getValueDefinition(compConstraintName)
				.getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testMaxValueThresholdForValueDef() {
		assertEquals("wrong max value for value def: '" 
				+ compConstraintName + "'", 
				NumberValue.getInstance(100L), 
				llaDef.getValueDefinition(compConstraintName)
				.getParameterValue("maxThreshold"));
	}

	@Test
	public void testMaxValueCompLEForValueDef() {
		assertEquals("wrong max value comp for value def: '" +
				compConstraintName + "'", 
				"<=", 
				llaDef.getValueDefinition(compConstraintName)
				.getParameterComp("maxThreshold").getComparatorString());
	}
}
