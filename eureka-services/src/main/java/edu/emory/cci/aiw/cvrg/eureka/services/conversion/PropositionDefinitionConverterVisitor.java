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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import org.protempa.PropositionDefinition;

import java.util.List;

public final class PropositionDefinitionConverterVisitor implements
        PropositionEntityVisitor {

	private List<PropositionDefinition> propositionDefinition;
	private Long userId;

	private final ValueComparatorDao valueCompDao;

	@Inject
	public PropositionDefinitionConverterVisitor(
			ValueComparatorDao inValueCompDao) {
		valueCompDao = inValueCompDao;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public List<PropositionDefinition> getPropositionDefinition() {
		return propositionDefinition;
	}

	@Override
	public void visit(SystemProposition entity) {
		this.propositionDefinition = new SystemPropositionConverter(this.userId)
		        .convert(entity);
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.propositionDefinition = new CategorizationConverter().convert(entity);
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.propositionDefinition = new SequenceConverter(this.valueCompDao)
		        .convert(entity);
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds().size() > 1) {
			this.propositionDefinition = new ValueThresholdsCompoundLowLevelAbstractionConverter()
			        .convert(entity);
		} else {
			this.propositionDefinition = new
					ValueThresholdsLowLevelAbstractionConverter(this.userId,
			        this.valueCompDao).convert(entity);
		}
	}

	@Override
	public void visit(FrequencyEntity entity) {
		if (entity.isConsecutive()) {
			this.propositionDefinition = new
					FrequencyHighLevelAbstractionConverter(this.userId,
			        this.valueCompDao).convert(entity);
		} else {
			this.propositionDefinition = new FrequencySliceAbstractionConverter()
			        .convert(entity);
		}
	}
}
