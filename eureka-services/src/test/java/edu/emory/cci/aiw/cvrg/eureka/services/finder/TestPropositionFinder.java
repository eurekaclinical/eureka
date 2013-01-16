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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TestPropositionFinder implements PropositionFinder<Long, String> {

	private Map<String, PropositionDefinition> propDefs = new
			HashMap<String, PropositionDefinition>();

	private final EventDefinition TEST_EVENT_1 = new EventDefinition
			("test-event1");
	private final EventDefinition TEST_EVENT_2 = new EventDefinition
			("test-event2");
	private final ConstantDefinition TEST_CONSTANT_1 = new ConstantDefinition
			("test-constant1");
	private final ConstantDefinition TEST_CONSTANT_2 = new ConstantDefinition
			("test-constant2");
	private final PrimitiveParameterDefinition TEST_PRIMPARAM_1 = new
			PrimitiveParameterDefinition("test-primparam1");
	private final PrimitiveParameterDefinition TEST_PRIMPARAM_2 = new
			PrimitiveParameterDefinition("test-primparam2");


	TestPropositionFinder() {
		setup();
	}

	private void setup() {
		propDefs.put(TEST_EVENT_1.getId(), TEST_EVENT_1);
		propDefs.put(TEST_EVENT_2.getId(), TEST_EVENT_2);
		propDefs.put(TEST_CONSTANT_1.getId(), TEST_CONSTANT_1);
		propDefs.put(TEST_CONSTANT_2.getId(), TEST_CONSTANT_2);
		propDefs.put(TEST_PRIMPARAM_1.getId(), TEST_PRIMPARAM_1);
		propDefs.put(TEST_PRIMPARAM_2.getId(), TEST_PRIMPARAM_2);
	}

	@Override
	public PropositionDefinition find(Long inUserId,
									  String inKey) throws PropositionFindException {
		if (propDefs.containsKey(inKey)) {
			return propDefs.get(inKey);
		}
		return null;
	}

	@Override
	public void shutdown() {

	}
}
