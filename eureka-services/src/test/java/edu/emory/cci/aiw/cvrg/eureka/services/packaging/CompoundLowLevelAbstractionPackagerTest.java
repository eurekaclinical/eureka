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

public class CompoundLowLevelAbstractionPackagerTest {

//	private final CompoundLowLevelAbstractionPackager packager = new CompoundLowLevelAbstractionPackager();
	
	@Test
	public void testCompoundLowLevelAbstractionPackager() {
//		TimeUnit dayUnit = new TimeUnit();
//		dayUnit.setName("day");
//		
//		ValueComparator lt = new ValueComparator();
//		lt.setName("<");
//		ValueComparator gt = new ValueComparator();
//		gt.setName(">");
//		
//		SystemProposition primParam1 = new SystemProposition();
//		primParam1.setId(1L);
//		primParam1.setKey("test-primparam1");
//		primParam1.setInSystem(true);
//		primParam1.setSystemType(SystemType.PRIMITIVE_PARAMETER);
//		
//		SystemProposition primParam2 = new SystemProposition();
//		primParam2.setId(2L);
//		primParam2.setKey("test-primparam2");
//		primParam2.setInSystem(true);
//		primParam2.setSystemType(SystemType.PRIMITIVE_PARAMETER); 
//		
//		ValueThresholdGroupEntity vte1 = new ValueThresholdGroupEntity();
//		vte1.setId(3L);
//		vte1.setKey("test-vte1");
//		vte1.setName("test-threshold1");
////		vte1.setAbstractedFrom(primParam1);
//		vte1.setMinValues(1);
//		vte1.setMinGapValues(2);
//		vte1.setMinGapValuesUnits(dayUnit);
//		vte1.setMaxGapValues(4);
//		vte1.setMaxGapValuesUnits(dayUnit);
//		
//		ValueThresholdEntity userConstraint1 = new ValueThresholdEntity();
//		userConstraint1.setMinValueThreshold(100);
//		userConstraint1.setMinValueComp(gt);
//		userConstraint1.setMaxValueThreshold(200);
//		userConstraint1.setMaxValueComp(lt);
//		
//		ValueThresholdEntity compConstraint1 = new ValueThresholdEntity();
//		compConstraint1.setMinValueThreshold(200);
//		compConstraint1.setMinValueComp(gt);
//		compConstraint1.setMaxValueThreshold(100);
//		compConstraint1.setMaxValueComp(lt);
//		
//		vte1.setValueThresholds(userConstraint1);
//		vte1.setComplementConstraint(compConstraint1);
//		
//		ValueThresholdGroupEntity vte2 = new ValueThresholdGroupEntity();
//		vte2.setId(4L);
//		vte2.setKey("test-vte2");
//		vte2.setName("test-threshold2");
////		vte2.setAbstractedFrom(primParam2);
//		vte2.setMinValues(1);
//		vte2.setMinGapValues(2);
//		vte2.setMinGapValuesUnits(dayUnit);
//		vte2.setMaxGapValues(4);
//		vte2.setMaxGapValuesUnits(dayUnit);
//		
//		ValueThresholdEntity userConstraint2 = new ValueThresholdEntity();
//		userConstraint2.setMinValueThreshold(300);
//		userConstraint2.setMinValueComp(gt);
//		userConstraint2.setMaxValueThreshold(400);
//		userConstraint2.setMaxValueComp(lt);
//		
//		ValueThresholdEntity compConstraint2 = new ValueThresholdEntity();
//		compConstraint2.setMinValueThreshold(400);
//		compConstraint2.setMinValueComp(gt);
//		compConstraint2.setMaxValueThreshold(300);
//		compConstraint2.setMaxValueComp(lt);
//		
//		vte2.setValueThresholds(userConstraint2);
//		vte2.setComplementConstraint(compConstraint2);
//		
//		CompoundValueThreshold cvt = new CompoundValueThreshold();
//		cvt.setId(5L);
//		cvt.setKey("test-valuethreshold");
//		cvt.setMinimumNumberOfValues(1);
//		cvt.setUserValueDefinitionName("test-threshold-XX");
//		cvt.setComplementValueDefinitionName("test-threshold-XX_COMP");
//		ThresholdsOperator op = new ThresholdsOperator();
//		op.setName("any");
//		cvt.setThresholdsOperator(op);
//		List<ValueThresholdGroupEntity> abstractedFrom = new ArrayList<ValueThresholdGroupEntity>();
//		abstractedFrom.add(vte1);
//		abstractedFrom.add(vte2);
//		cvt.setAbstractedFrom(abstractedFrom);
//		
//		CompoundLowLevelAbstractionDefinition cllaDef = this.packager.pack(cvt);
//		MinMaxGapFunction gf = (MinMaxGapFunction) cllaDef.getGapFunction();
//		assertEquals("wrong id", "test-valuethreshold", cllaDef.getId());
//		assertEquals("wrong minimum number of values", 1, cllaDef.getMinimumNumberOfValues());
//		assertEquals("wrong min gap b/w values", new Integer(2), gf.getMinimumGap());
//		assertEquals("wrong min gap b/w values units", "day", gf.getMinimumGapUnit().getName().toLowerCase());
//		assertEquals("wrong max gap b/w values", new Integer(4), gf.getMaximumGap());
//		assertEquals("wrong max gap b/w values units", "day", gf.getMaximumGapUnit().getName().toLowerCase());
		
	}
}
