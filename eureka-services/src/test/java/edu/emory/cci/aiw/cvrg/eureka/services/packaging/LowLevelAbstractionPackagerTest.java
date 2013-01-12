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
package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.junit.Test;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.proposition.value.NumberValue;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LowLevelAbstractionPackagerTest {

//	private LowLevelAbstractionPackager packager = new LowLevelAbstractionPackager();

	@Test
	public void testLowLevelAbstractionPackager() {
//		SystemProposition primParam = new SystemProposition();
//		primParam.setId(1L);
//		primParam.setKey("test-primparam");
//		primParam.setInSystem(true);
//		primParam.setSystemType(SystemType.PRIMITIVE_PARAMETER);
//		
//		TimeUnit dayUnit = new TimeUnit();
//		dayUnit.setName("day");
//		
//		ValueThresholdGroupEntity vte = new ValueThresholdGroupEntity();
//		vte.setId(2L);
//		vte.setKey("test-valuethreshold");
//		vte.setName("test-threshold-XX");
//		vte.setAbstractedFrom(primParam);
//		vte.setMinValues(1);
//		vte.setMinGapValues(2);
//		vte.setMinGapValuesUnits(dayUnit);
//		vte.setMaxGapValues(4);
//		vte.setMaxGapValuesUnits(dayUnit);
//		
//		ValueComparator lt = new ValueComparator();
//		lt.setName("<");
//		ValueComparator gt = new ValueComparator();
//		gt.setName(">");
//		
//		ValueThresholdEntity userConstraint = new ValueThresholdEntity();
//		userConstraint.setMinValueThreshold(100);
//		userConstraint.setMinValueComp(gt);
//		userConstraint.setMaxValueThreshold(200);
//		userConstraint.setMaxValueComp(lt);
//		
//		ValueThresholdEntity complementConstraint = new ValueThresholdEntity();
//		complementConstraint.setMinValueThreshold(200);
//		complementConstraint.setMinValueComp(gt);
//		complementConstraint.setMaxValueThreshold(100);
//		complementConstraint.setMaxValueComp(lt);
//		
//		vte.setValueThresholds(userConstraint);
//		vte.setComplementConstraint(complementConstraint);
//		
//		LowLevelAbstractionDefinition llaDef = this.packager.pack(vte);
//		assertEquals("wrong id", "test-valuethreshold", llaDef.getId());
//		assertEquals("wrong abstracted from size", 1, llaDef.getAbstractedFrom().size());
//		assertEquals("wrong abstracted from", "test-primparam", llaDef.getAbstractedFrom().iterator().next());
//		assertEquals("wrong minimum number of values", 1, llaDef.getMinimumNumberOfValues());
//		assertEquals("wrong min gap b/w values", new Integer(2), llaDef.getMinimumGapBetweenValues());
//		assertEquals("wrong min gap b/w values units", "day", llaDef.getMinimumGapBetweenValuesUnits().getName().toLowerCase());
//		assertEquals("wrong max gap b/w values", new Integer(4), llaDef.getMaximumGapBetweenValues());
//		assertEquals("wrong max gap b/w values units", "day", llaDef.getMaximumGapBetweenValuesUnits().getName().toLowerCase());
//		assertEquals("wrong algorithm", "stateDetector", llaDef.getAlgorithmId());
//		assertEquals("wrong number of value definitions", 2, llaDef.getValueDefinitions().size());
//		final String userConstraintName = "test-threshold-XX";
//		final String compConstraintName = "test-threshold-XX_COMPLEMENT";
//		assertTrue("value def '" + userConstraintName +"' does not exist", llaDef.getValueDefinition(userConstraintName) != null);
//		assertEquals("wrong min value for value def: '" + userConstraintName + "'", NumberValue.getInstance(100L), llaDef.getValueDefinition(userConstraintName).getParameterValue("minThreshold"));
//		assertEquals("wrong min value comp for value def: '" + userConstraintName + "'", ">", llaDef.getValueDefinition(userConstraintName).getParameterComp("minThreshold").getComparatorString());
//		assertEquals("wrong max value for value def: '" + userConstraintName + "'", NumberValue.getInstance(200L), llaDef.getValueDefinition(userConstraintName).getParameterValue("maxThreshold"));
//		assertEquals("wrong max value comp for value def: '" + userConstraintName + "'", "<", llaDef.getValueDefinition(userConstraintName).getParameterComp("maxThreshold").getComparatorString());
//		assertTrue("value def '" + compConstraintName +"' does not exist", llaDef.getValueDefinition(compConstraintName) != null);
//		assertEquals("wrong min value for value def: '" + compConstraintName + "'", NumberValue.getInstance(200L), llaDef.getValueDefinition(compConstraintName).getParameterValue("minThreshold"));
//		assertEquals("wrong min value comp for value def: '" + compConstraintName + "'", ">", llaDef.getValueDefinition(compConstraintName).getParameterComp("minThreshold").getComparatorString());
//		assertEquals("wrong max value for value def: '" + compConstraintName + "'", NumberValue.getInstance(100L), llaDef.getValueDefinition(compConstraintName).getParameterValue("maxThreshold"));
//		assertEquals("wrong max value comp for value def: '" + compConstraintName + "'", "<", llaDef.getValueDefinition(compConstraintName).getParameterComp("maxThreshold").getComparatorString());
	}
}
