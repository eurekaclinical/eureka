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
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.proposition.value.AbsoluteTimeUnit;

/**
 *
 */
public class FrequencyHighLevelAbstractionConverterTest extends AbstractServiceTest {
	private List<LowLevelAbstractionDefinition> llas;
	private LowLevelAbstractionDefinition llaDef;
	private String userConstraintName;
	private String compConstraintName;
	private HighLevelAbstractionDefinition hlad;
	private FrequencyEntity frequency;
	private TemporalExtendedParameterDefinition tepd;
	private MinMaxGapFunction gf;
	private String thresholdGroupKey;
	
	@Before
	public void setUp() {
		PropositionDefinitionConverterVisitor converterVisitor = this.getInstance(PropositionDefinitionConverterVisitor.class);
		FrequencyHighLevelAbstractionConverter converter = new FrequencyHighLevelAbstractionConverter();
		converter.setConverterVisitor(converterVisitor);
		SystemProposition primParam = new SystemProposition();
		primParam.setId(1L);
		primParam.setKey("test-primparam1");
		primParam.setInSystem(true);
		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);
		
		ValueThresholdGroupEntity thresholdGroup = new ValueThresholdGroupEntity();
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
				ArrayList<ValueThresholdEntity>();
		thresholds.add(threshold);
		thresholdGroup.setValueThresholds(thresholds);
		
		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");
		
		frequency = new FrequencyEntity();
		frequency.setId(3L);
		frequency.setKey("test-freqhla-key");
		frequency.setAtLeastCount(2);
		frequency.setWithinAtLeast(1);
		frequency.setWithinAtLeastUnits(dayUnit);
		frequency.setWithinAtMost(90);
		frequency.setWithinAtMostUnits(dayUnit);
		
		
		ExtendedDataElement af = new ExtendedDataElement();
		af.setDataElementEntity(thresholdGroup);
		frequency.setExtendedProposition(af);
		
		List<PropositionDefinition> propDefs = 
				converter.convert(frequency);
		assertEquals("wrong number of proposition definitions created", 
				3, propDefs.size());
		llas = new ArrayList<LowLevelAbstractionDefinition>();
		for (PropositionDefinition propDef : propDefs) {
			if (propDef instanceof LowLevelAbstractionDefinition) {
				llas.add((LowLevelAbstractionDefinition) propDef);
			}
		}
		llaDef = llas.get(0);
		hlad = converter.getPrimaryPropositionDefinition();
		
		userConstraintName = thresholdGroupKey + "_VALUE";
		compConstraintName = thresholdGroupKey + "_VALUE_COMP";
		
		tepd = (TemporalExtendedParameterDefinition) 
				hlad.getExtendedPropositionDefinitions().iterator().next();
		
		gf = (MinMaxGapFunction) hlad.getGapFunction();
	}
	
	@After
	public void tearDown() {
		llas = null;
		llaDef = null;
		userConstraintName = null;
		compConstraintName = null;
		frequency = null;
		tepd = null;
		hlad = null;
		gf = null;
		thresholdGroupKey = null;
	}
	
	@Test
	public void testNumberOfLLAsCreated() {
		assertEquals("wrong number of low-level abstractions created", 
				1, llas.size());
	}
	
	@Test
	public void testLLAId() {
		assertEquals("wrong id", 
				"test-valuethreshold" + ConversionUtil.PRIMARY_PROP_ID_SUFFIX, 
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
				2, llaDef.getValueDefinitions().size());
	}
	
	@Test
	public void testValueDefDoesNotExist() {
		List<String> vds = new ArrayList<String>();
		for (LowLevelAbstractionValueDefinition vd : 
				llaDef.getValueDefinitions()) {
			vds.add(vd.getId());
		}
		assertTrue("value def '" + userConstraintName +
				"' does not exist; what does exist? " + 
				StringUtils.join(vds, ", "), 
				llaDef.getValueDefinition(userConstraintName) != null);
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
	public void testMinValueGTForValueDef() {
		assertEquals("wrong min value comp for value def: '" + 
				userConstraintName + "'", 
				">", 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testMaxValueThresholdForValueDef() {
		assertEquals("wrong max value for value def: '" + 
				userConstraintName + "'", 
				NumberValue.getInstance(200L), 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterValue("maxThreshold"));
	}
	
	@Test
	public void testMaxValueCompLTForValueDef() {
		assertEquals("wrong max value comp for value def: '" + 
				userConstraintName + "'", 
				"<", 
				llaDef.getValueDefinition(userConstraintName)
				.getParameterComp("maxThreshold").getComparatorString());
	}
	
	@Test
	public void testValueDefDoesNotExistComp() {
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
	public void minValueCompGEForValueDef() {
		assertEquals("wrong min value comp for value def: '" +
				compConstraintName + "'", 
				">=", 
				llaDef.getValueDefinition(compConstraintName)
				.getParameterComp("minThreshold").getComparatorString());
	}
	
	@Test
	public void testMaxValueCompThresholdForValueDef() {
		assertEquals("wrong max value for value def: '" + 
				compConstraintName + "'", 
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
	
	@Test
	public void testHLAId() {
		String hladExpectedId = frequency.getKey() + 
				ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		assertEquals("wrong id", hladExpectedId, hlad.getId());
	}
	
	@Test
	public void testNumberOfExtendedPropositionDefinitions() {
		assertEquals("wrong number of extended proposition definitions", 
				1, hlad.getExtendedPropositionDefinitions().size());
	}
	
	@Test
	public void testExtendedPropositionDefinitionPropositionId() {
		TemporalExtendedParameterDefinition tepd = 
				(TemporalExtendedParameterDefinition) 
				hlad.getExtendedPropositionDefinitions().iterator().next();
		assertEquals("wrong extended proposition definition", 
				frequency.getKey() + "_SUB", tepd.getPropositionId());
	}
	
	@Test
	public void testValueForExtendedPropositionDefinition() {
		TemporalExtendedParameterDefinition tepd = 
				(TemporalExtendedParameterDefinition) 
				hlad.getExtendedPropositionDefinitions().iterator().next();
		assertEquals("wrong value for extended proposition definition", 
				NominalValue.getInstance(thresholdGroupKey + "_VALUE"), 
				tepd.getValue());
	}
	
	@Test
	public void testNumberOfRelations() {
		assertEquals("wrong number of relations defined", 
				1, 
				hlad.getTemporalExtendedPropositionDefinitionPairs().size());
	}
	
	@Test
	public void testRelationExists() {
		
		Relation relation = hlad.getRelation(tepd, tepd);
		Assert.assertNotNull("relation is null", relation);
	}
	
	@Test
	public void testGapFunctionExists() {
		Assert.assertNotNull("gap function is null", gf);
	}
	
	@Test
	public void testMinGap() {
		assertEquals("wrong min gap ", Integer.valueOf(1), gf.getMinimumGap());
	}
	
	@Test
	public void testMinGapUnit() {
		assertEquals("wrong min gap unit", 
				AbsoluteTimeUnit.DAY, gf.getMinimumGapUnit());
	}
	
	@Test
	public void testMaxGap() {
		assertEquals("wrong max gap", new Integer(90), gf.getMaximumGap());
	}
	
	@Test
	public void testMaxGapUnit() {
		assertEquals("wrong max gap unit", 
				AbsoluteTimeUnit.DAY, gf.getMaximumGapUnit());
	}
}
