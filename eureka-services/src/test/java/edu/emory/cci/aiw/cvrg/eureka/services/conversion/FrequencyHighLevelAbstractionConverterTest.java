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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

/**
 *
 */
public class FrequencyHighLevelAbstractionConverterTest extends AbstractServiceTest {
	private PropositionDefinitionConverterVisitor converterVisitor;
	private FrequencyHighLevelAbstractionConverter converter;
	
	@Before
	public void setUp() {
		converterVisitor = this.getInstance(PropositionDefinitionConverterVisitor.class);
		converter = new FrequencyHighLevelAbstractionConverter();
		converter.setConverterVisitor(converterVisitor);
	}
	
	@Test
	public void testFrequencyHighLevelAbstractionConverter() {
		SystemProposition primParam = new SystemProposition();
		primParam.setId(1L);
		primParam.setKey("test-primparam1");
		primParam.setInSystem(true);
		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);
		
		ValueThresholdGroupEntity thresholdGroup = new ValueThresholdGroupEntity();
		thresholdGroup.setId(2L);
		thresholdGroup.setKey("test-valuethreshold");
		
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
				ArrayList<ValueThresholdEntity>();
		thresholds.add(threshold);
		thresholdGroup.setValueThresholds(thresholds);
		
		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");
		
		FrequencyEntity frequency = new FrequencyEntity();
		frequency.setId(3L);
		frequency.setKey("test-freqhla-key");
		frequency.setAtLeastCount(2);
		frequency.setWithinAtLeast(1);
		frequency.setWithinAtLeastUnits(dayUnit);
		frequency.setWithinAtMost(90);
		frequency.setWithinAtMostUnits(dayUnit);
		
		ExtendedProposition af = new ExtendedProposition();
		af.setProposition(thresholdGroup);
		frequency.setExtendedProposition(af);
		
		List<PropositionDefinition> propDefs = this.converter.convert(frequency);
		assertEquals("wrong number of proposition definitions created", 2, propDefs.size());
		List<LowLevelAbstractionDefinition> llas = new ArrayList<LowLevelAbstractionDefinition>();
		for (PropositionDefinition propDef : propDefs) {
			if (propDef instanceof LowLevelAbstractionDefinition) {
				llas.add((LowLevelAbstractionDefinition) propDef);
			}
		}
		assertEquals("wrong number of low-level abstractions created", 1, llas.size());
		LowLevelAbstractionDefinition llaDef = llas.get(0);
		assertEquals("wrong id", "test-valuethreshold", llaDef.getId());
		assertEquals("wrong abstracted from size", 1, llaDef.getAbstractedFrom().size());
		assertEquals("wrong abstracted from", "test-primparam1",
				llaDef.getAbstractedFrom().iterator().next());
		assertEquals("wrong minimum number of values", 2, llaDef.getMinimumNumberOfValues());
		assertEquals("wrong algorithm", "stateDetector", llaDef.getAlgorithmId());
		assertEquals("wrong number of value definitions", 2, llaDef.getValueDefinitions().size());
		final String userConstraintName = "test-valuethreshold_VALUE";
		final String compConstraintName = "test-valuethreshold_VALUE_COMP";
		assertTrue("value def '" + userConstraintName +"' does not exist", llaDef.getValueDefinition(userConstraintName) != null);
		assertEquals("wrong min value for value def: '" + userConstraintName + "'", NumberValue.getInstance(100L), llaDef.getValueDefinition(userConstraintName).getParameterValue("minThreshold"));
		assertEquals("wrong min value comp for value def: '" + userConstraintName + "'", ">", llaDef.getValueDefinition(userConstraintName).getParameterComp("minThreshold").getComparatorString());
		assertEquals("wrong max value for value def: '" + userConstraintName + "'", NumberValue.getInstance(200L), llaDef.getValueDefinition(userConstraintName).getParameterValue("maxThreshold"));
		assertEquals("wrong max value comp for value def: '" + userConstraintName + "'", "<", llaDef.getValueDefinition(userConstraintName).getParameterComp("maxThreshold").getComparatorString());
		assertTrue("value def '" + compConstraintName +"' does not exist", llaDef.getValueDefinition(compConstraintName) != null);
		assertEquals("wrong min value for value def: '" + compConstraintName + "'", NumberValue.getInstance(200L), llaDef.getValueDefinition(compConstraintName).getParameterValue("minThreshold"));
		assertEquals("wrong min value comp for value def: '" +
				compConstraintName + "'", ">=", llaDef.getValueDefinition(compConstraintName).getParameterComp("minThreshold").getComparatorString());
		assertEquals("wrong max value for value def: '" + compConstraintName + "'", NumberValue.getInstance(100L), llaDef.getValueDefinition(compConstraintName).getParameterValue("maxThreshold"));
		assertEquals("wrong max value comp for value def: '" +
				compConstraintName + "'", "<=", llaDef.getValueDefinition(compConstraintName).getParameterComp("maxThreshold").getComparatorString());

		HighLevelAbstractionDefinition hlad = this.converter.getPrimaryPropositionDefinition();
		assertEquals("wrong id", "test-freqhla-key", hlad.getId());
		assertEquals("wrong number of extended propositions", 1, hlad.getExtendedPropositionDefinitions().size());
		TemporalExtendedParameterDefinition tepd = (TemporalExtendedParameterDefinition) hlad.getExtendedPropositionDefinitions().iterator().next();
		assertEquals("wrong extended proposition", "test-valuethreshold", tepd.getPropositionId());
		assertEquals("wrong value definition for extended proposition", NominalValue.getInstance("test-valuethreshold_VALUE"), tepd.getValue());
		assertEquals("wrong number of relations defined", 1, hlad.getTemporalExtendedPropositionDefinitionPairs().size());
		Relation relation = hlad.getRelation(tepd, tepd);
		Assert.assertNotNull("relation is null", relation);
		MinMaxGapFunction gf = (MinMaxGapFunction) hlad.getGapFunction();
		Assert.assertNotNull("gap function is null", gf);
		assertEquals("wrong min gap ", new Integer(1), gf.getMinimumGap());
		assertEquals("wrong min gap unit", "day", gf.getMinimumGapUnit().getName());
		assertEquals("wrong max gap", new Integer(90), gf.getMaximumGap());
		assertEquals("wrong max gap unit", "day", gf.getMaximumGapUnit().getName());
	}
}
