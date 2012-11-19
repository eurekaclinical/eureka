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
package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class PropositionDefinitionPackagerVisitor implements
        PropositionEntityVisitor {

	private PropositionDefinition propositionDefinition;
	private final Long userId;

	public PropositionDefinitionPackagerVisitor(Long inUserId) {
		this.userId = inUserId;
	}

	public PropositionDefinition getPropositionDefinition() {
		return propositionDefinition;
	}

	@Override
	public void visit(SystemProposition proposition) {
		this.propositionDefinition = new SystemPropositionPackager(this.userId)
		        .pack(proposition);
	}

	@Override
	public void visit(Categorization categorization) {
		this.propositionDefinition = new CategorizationPackager()
		        .pack(categorization);
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		this.propositionDefinition = new HighLevelAbstractionPackager()
		        .pack(highLevelAbstraction);
	}

}
