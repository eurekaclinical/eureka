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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import org.eurekaclinical.eureka.client.comm.SystemType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.value.Value;

/**
 *
 */
public class FrequencyAtLeastNotConsecutiveValueThresholdConverterTest extends AbstractServiceTest {
	private List<PropositionDefinition> propDefs;
	private List<LowLevelAbstractionDefinition> llas;
	private LowLevelAbstractionDefinition llaDef;
	private String userConstraintName;
	private String compConstraintName;
	private HighLevelAbstractionDefinition hlad;
	private FrequencyEntity frequency;
	private TemporalExtendedParameterDefinition tepd;
	private SimpleGapFunction gf;
	private String thresholdGroupKey;
	
	@Before
	public void setUp() {
		PropositionDefinitionConverterVisitor converterVisitor = 
				this.getInstance(PropositionDefinitionConverterVisitor.class);
		FrequencyValueThresholdConverter converter = 
				new FrequencyValueThresholdConverter();
		converter.setConverterVisitor(converterVisitor);
		SystemProposition primParam = new SystemProposition();
		primParam.setId(1L);
		primParam.setKey("test-primparam1");
		primParam.setInSystem(true);
		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);
		
		ValueThresholdGroupEntity thresholdGroup = 
				new ValueThresholdGroupEntity();
		thresholdGroup.setId(2L);
		thresholdGroup.setKey("test-valuethreshold");
		thresholdGroupKey = thresholdGroup.getKey();
		
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
		threshold.setId(Long.valueOf(1));
		
		List<ValueThresholdEntity> thresholds = new
				ArrayList<>();
		thresholds.add(threshold);
		thresholdGroup.setValueThresholds(thresholds);
		
		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");
		
		FrequencyType ft = new FrequencyType();
		ft.setName("at least");
		
		frequency = new FrequencyEntity();
		frequency.setId(3L);
		frequency.setKey("test-freqhla-key");
		frequency.setCount(2);
		frequency.setWithinAtLeast(1);
		frequency.setWithinAtLeastUnits(dayUnit);
		frequency.setWithinAtMost(90);
		frequency.setWithinAtMostUnits(dayUnit);
		frequency.setFrequencyType(ft);
		
		
		ExtendedPhenotype af = new ExtendedPhenotype();
		af.setPhenotypeEntity(thresholdGroup);
		frequency.setExtendedProposition(af);
		
		propDefs = converter.convert(frequency);
		
		llas = new ArrayList<>();
		for (PropositionDefinition propDef : propDefs) {
			if (propDef instanceof LowLevelAbstractionDefinition) {
				llas.add((LowLevelAbstractionDefinition) propDef);
			}
		}
		llaDef = llas.get(0);
		hlad = converter.getPrimaryPropositionDefinition();
		
		userConstraintName = asValueString(thresholdGroupKey);
		compConstraintName = asValueCompString(thresholdGroupKey);
		
		tepd = (TemporalExtendedParameterDefinition) 
				hlad.getExtendedPropositionDefinitions().iterator().next();
		
		gf = (SimpleGapFunction) hlad.getGapFunction();
	}

	@Test
	public void testPrimaryPropositionId() {
		assertEquals("wrong primary proposition id", 
				toPropositionId(frequency), 
				hlad.getId());
	}
	
	@Test
	public void testNumberOfPropositionDefinitionsCreated() {
		assertEquals("wrong number of proposition definitions created", 
				2, propDefs.size());
	}
	
	@Test
	public void testNumberOfLLAsCreated() {
		assertEquals("wrong number of low-level abstractions created", 
				1, llas.size());
	}
	
	@Test
	public void testLLAId() {
		assertEquals("wrong id", 
				toPropositionId("test-valuethreshold"), 
				llaDef.getId());
	}
	
	@Test
	public void testAbstractedFromSize() {
		assertEquals("wrong abstracted from size", 
				1, llaDef.getAbstractedFrom().size());
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
		assertEquals("wrong algorithm", 
				"stateDetector", llaDef.getAlgorithmId());
	}
	
	@Test
	public void testNumberOfValueDefs() {
		assertEquals("wrong number of value definitions", 
				3, llaDef.getValueDefinitions().size());
	}
	
	@Test
	public void testValueDefDoesNotExist() {
		List<String> vds = new ArrayList<>();
		for (LowLevelAbstractionValueDefinition vd : 
				llaDef.getValueDefinitions()) {
			vds.add(vd.getId());
		}
		assertTrue("value def '" + userConstraintName +
				"' does not exist; what does exist? " + 
				StringUtils.join(vds, ", "), 
				llaDef.getValueDefinitions(userConstraintName) != null);
	}
	
	@Test
	public void testMinValueThresholdForValueDef() {
		assertEquals("wrong min value threshold for value def: '" 
				+ userConstraintName + "'", 
				NumberValue.getInstance(100L), 
				llaDef.getValueDefinitions(userConstraintName)[0]
				.getParameterValue("minThreshold"));
	}
	
	@Test
	public void testMinValueGTForValueDef() {
		assertEquals("wrong min value comp for value def: '" + 
				userConstraintName + "'", 
				">", 
				userCompStringFor("minThreshold"));
	}
	
	@Test
	public void testMaxValueThresholdForValueDef() {
		assertEquals("wrong max value for value def: '" + 
				userConstraintName + "'", 
				NumberValue.getInstance(200L), 
				llaDef.getValueDefinitions(userConstraintName)[0]
				.getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testMaxValueCompLTForValueDef() {
		assertEquals("wrong max value comp for value def: '" + 
				userConstraintName + "'", 
				"<", 
				userCompStringFor("maxThreshold"));
	}
	
	@Test
	public void testValueDefDoesNotExistComp() {
		assertTrue("value def '" + compConstraintName +"' does not exist", 
				llaDef.getValueDefinitions(compConstraintName) != null);
	}
	
	@Test
	public void testMinValueCompThresholdForValueDef() {
		assertEquals("wrong min value comp for value def: '" + 
				compConstraintName + "'", 
				NumberValue.getInstance(200L), 
				valStringFor("minThreshold"));
	}
	
	@Test
	public void minValueCompGEForValueDef() {
		assertEquals("wrong min value comp for value def: '" +
				compConstraintName + "'", 
				">=", 
				compStringFor("minThreshold"));
	}
	
	@Test
	public void testMaxValueCompThresholdForValueDef() {
		assertEquals("wrong max value for value def: '" + 
				compConstraintName + "'", 
				NumberValue.getInstance(100L), 
				valStringFor("maxThreshold"));
	}
	
	@Test
	public void testMaxValueCompLEForValueDef() {
		assertEquals("wrong max value comp for value def: '" +
				compConstraintName + "'", 
				"<=", 
				compStringFor("maxThreshold"));
	}
	
	private Value valStringFor(String threshold) {
		for (LowLevelAbstractionValueDefinition valueDefinitions : llaDef.getValueDefinitions(compConstraintName)) {
			Value val = valueDefinitions.getParameterValue(threshold);
			if (val != null) {
				return val;
			}
		}
		return null;
	}
	
	private String compStringFor(String threshold) {
		for (LowLevelAbstractionValueDefinition valueDefinitions : llaDef.getValueDefinitions(compConstraintName)) {
			org.protempa.proposition.value.ValueComparator parameterComp = valueDefinitions.getParameterComp(threshold);
			if (parameterComp != null) {
				return parameterComp.getComparatorString();
			}
		}
		return null;
	}
	
	private String userCompStringFor(String threshold) {
		for (LowLevelAbstractionValueDefinition valueDefinitions : llaDef.getValueDefinitions(userConstraintName)) {
			org.protempa.proposition.value.ValueComparator parameterComp = valueDefinitions.getParameterComp(threshold);
			if (parameterComp != null) {
				return parameterComp.getComparatorString();
			}
		}
		return null;
	}
	
	@Test
	public void testHLAId() {
		String hladExpectedId = toPropositionId(frequency);
		assertEquals("wrong id", hladExpectedId, hlad.getId());
	}
	
	@Test
	public void testNumberOfExtendedPropositionDefinitions() {
		assertEquals("wrong number of extended proposition definitions", 
				2, hlad.getExtendedPropositionDefinitions().size());
	}
	
	@Test
	public void testExtendedPropositionDefinitionPropositionId() {
		assertEquals("wrong extended proposition definition", 
				toPropositionId(thresholdGroupKey), 
				tepd.getPropositionId());
	}
	
	@Test
	public void testValueForExtendedPropositionDefinition() {
		assertEquals("wrong value for extended proposition definition", 
				asValue(thresholdGroupKey), 
				tepd.getValue());
	}
	
	@Test
	public void testNumberOfRelations() {
		assertEquals("wrong number of relations defined", 
				1, 
				hlad.getTemporalExtendedPropositionDefinitionPairs().size());
	}
	
	@Test
	public void testGapFunctionExists() {
		Assert.assertNotNull("gap function is null", gf);
	}
	
	@Test
	public void testMaxGap() {
		assertEquals("wrong max gap", Integer.valueOf(0), gf.getMaximumGap());
	}
	
	@Test
	public void testMaxGapUnit() {
		assertEquals("wrong max gap unit", null, gf.getMaximumGapUnit());
	}
}
