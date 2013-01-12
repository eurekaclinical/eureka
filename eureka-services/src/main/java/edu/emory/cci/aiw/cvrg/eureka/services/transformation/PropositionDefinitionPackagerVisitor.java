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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;

public final class PropositionDefinitionPackagerVisitor implements
        PropositionEntityVisitor {

	private PropositionDefinition propositionDefinition;
	private Long userId;

	private final ValueComparatorDao valueCompDao;

	@Inject
	public PropositionDefinitionPackagerVisitor(
	        ValueComparatorDao inValueCompDao) {
		valueCompDao = inValueCompDao;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public PropositionDefinition getPropositionDefinition() {
		return propositionDefinition;
	}

	@Override
	public void visit(SystemProposition entity) {
		this.propositionDefinition = new SystemPropositionPackager(this.userId)
		        .pack(entity);
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.propositionDefinition = new CategorizationPackager().pack(entity);
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.propositionDefinition = new HighLevelAbstractionPackager()
		        .pack(entity);
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds().size() > 1) {
			this.propositionDefinition = new ValueThresholdsCompoundLowLevelAbstractionPackager()
			        .pack(entity);
		} else {
			this.propositionDefinition = new ValueThresholdsLowLevelAbstractionPackager(
			        valueCompDao).pack(entity);
		}
	}

	@Override
	public void visit(FrequencyEntity entity) {
		if (entity.isConsecutive()) {
			this.propositionDefinition = new FrequencyHighLevelAbstractionPackager(
			        this.valueCompDao).pack(entity);
		} else {
			this.propositionDefinition = new FrequencySliceAbstractionPackager()
			        .pack(entity);
		}
	}
}
