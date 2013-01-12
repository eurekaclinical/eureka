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
package edu.emory.cci.aiw.cvrg.eureka.services.transformation;

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionRetriever;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SystemPropositionPackager implements
        PropositionDefinitionPackager<SystemProposition, PropositionDefinition> {

	private final Long userId;
	
	public SystemPropositionPackager(Long inUserId) {
		this.userId = inUserId;
	}
	
	@Override
	public PropositionDefinition pack(SystemProposition proposition) {
		SystemPropositionFinder finder = new SystemPropositionFinder(
		        new SystemPropositionRetriever(new ServiceProperties()));
		try {		
			return finder.find(this.userId, proposition.getKey());
		} catch (PropositionFindException ex) {
			throw new AssertionError("Error retrieving system propositions: " + 
					ex.getMessage());
		}
	}
}
