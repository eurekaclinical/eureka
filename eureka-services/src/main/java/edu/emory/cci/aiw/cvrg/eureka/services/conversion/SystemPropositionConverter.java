/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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

import java.util.Collections;
import java.util.List;

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFinder;

public final class SystemPropositionConverter implements
		PropositionDefinitionConverter<SystemProposition, PropositionDefinition> {

	private final PropositionFinder<Long, String> finder;

	private Long userId;

	private PropositionDefinition primary;

	@Inject
	public SystemPropositionConverter(PropositionFinder<Long, 
			String> inFinder) {
		finder = inFinder;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public List<PropositionDefinition> convert(SystemProposition proposition) {
		try {
			this.primary = finder.find(this.userId,
					proposition.getKey());
			return Collections.singletonList(primary);
		} catch (PropositionFindException ex) {
			throw new AssertionError("Error retrieving system propositions: " + 
					ex.getMessage());
		}
	}
}
