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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionRetriever;
import org.protempa.PropositionDefinition;

import java.util.Collections;
import java.util.List;

public final class SystemPropositionConverter implements
		PropositionDefinitionConverter<SystemProposition> {

	private final Long userId;

	private PropositionDefinition primary;
	
	public SystemPropositionConverter(Long inUserId) {
		this.userId = inUserId;
	}

	@Override
	public PropositionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}
	
	@Override
	public List<PropositionDefinition> convert(SystemProposition proposition) {
		SystemPropositionFinder finder = new SystemPropositionFinder(
		        new SystemPropositionRetriever(new ServiceProperties()));
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
