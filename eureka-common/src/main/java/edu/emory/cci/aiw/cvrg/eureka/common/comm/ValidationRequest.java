/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

import org.protempa.PropositionDefinition;

public class ValidationRequest {
	private Long userId;
	private PropositionDefinition targetProposition;
	private List<PropositionDefinition> propositions;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public PropositionDefinition getTargetProposition() {
		return targetProposition;
	}

	public void setTargetProposition(PropositionDefinition inTargetProposition) {
		targetProposition = inTargetProposition;
	}

	public List<PropositionDefinition> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<PropositionDefinition> inPropositions) {
		propositions = inPropositions;
	}
}
