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
public class TestPropositionFinder implements PropositionFinder<String> {

	private Map<String, PropositionDefinition> propDefs = new
			HashMap<>();

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
	public PropositionDefinition find(String sourceConfigId,
									  String inKey) throws PropositionFindException {
		if (propDefs.containsKey(inKey)) {
			return propDefs.get(inKey);
		}
		return null;
	}

}
