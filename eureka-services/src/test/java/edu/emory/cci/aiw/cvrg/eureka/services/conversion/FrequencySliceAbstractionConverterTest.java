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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.protempa.MinMaxGapFunction;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FrequencySliceAbstractionConverterTest extends
		AbstractServiceTest {

	private PropositionDefinitionConverterVisitor converterVisitor;
	private FrequencySliceAbstractionConverter converter;

	@Before
	public void setUp() {
		converterVisitor = this.getInstance
				(PropositionDefinitionConverterVisitor.class);
		converter = new FrequencySliceAbstractionConverter();
		converter.setVisitor(converterVisitor);
	}
	
	@Test
	public void testSliceConverter() {
		SystemProposition event = new SystemProposition();
		event.setId(1L);
		event.setKey("test-event1");
		event.setDisplayName("test-event1-display");
		event.setAbbrevDisplayName("test-event1-abbrev");
		event.setInSystem(true);
		event.setSystemType(SystemType.EVENT);
		
		TimeUnit dayUnit = new TimeUnit();
		dayUnit.setName("day");
		
		FrequencyEntity sa = new FrequencyEntity();
		sa.setId(2L);
		sa.setKey("test-slice-key");
		sa.setAtLeastCount(3);
		sa.setWithinAtLeast(1);
		sa.setWithinAtLeastUnits(dayUnit);
		sa.setWithinAtMost(90);
		sa.setWithinAtMostUnits(dayUnit);
		
		ExtendedProposition af = new ExtendedProposition();
		af.setProposition(event);
		
		sa.setExtendedProposition(af);

		List<PropositionDefinition> sliceDefs = this.converter.convert
				(sa);
		System.out.println(sliceDefs);
		assertEquals("wrong number of proposition definitions created", 2,
				sliceDefs.size());
		SliceDefinition sliceDef = this.converter
				.getPrimaryPropositionDefinition();
		assertEquals("wrong ID", "test-slice-key", sliceDef.getId());
		assertEquals("wrong min index", 3, sliceDef.getMinIndex());
		assertEquals("wrong abstracted from size", 1, sliceDef.getAbstractedFrom().size());
		assertEquals("wrong abstracted from", "test-event1",
				sliceDef.getAbstractedFrom().iterator().next());
		MinMaxGapFunction gf = (MinMaxGapFunction) sliceDef.getGapFunction();
		assertEquals("wrong gap min", new Integer(1), gf.getMinimumGap());
		assertEquals("wrong gap min unit", "day", gf.getMinimumGapUnit().getName().toLowerCase());
		assertEquals("wrong gap max", new Integer(90), gf.getMaximumGap());
		assertEquals("wrong gap max unit", "day", gf.getMaximumGapUnit().getName().toLowerCase());
	}
}
