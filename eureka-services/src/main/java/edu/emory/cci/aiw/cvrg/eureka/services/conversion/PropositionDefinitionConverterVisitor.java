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
import java.util.Collections;
import org.protempa.PropositionDefinition;

import java.util.List;

public final class PropositionDefinitionConverterVisitor implements
		PropositionEntityVisitor {

	private List<PropositionDefinition> propositionDefinitions;
	private PropositionDefinition primaryProposition;

	private Long userId;

//	private final SystemPropositionConverter systemPropositionConverter;
	private final CategorizationConverter categorizationConverter;
	private final SequenceConverter sequenceConverter;
	private final ValueThresholdsLowLevelAbstractionConverter
			valueThresholdsLowLevelAbstractionConverter;
	private final ValueThresholdsCompoundLowLevelAbstractionConverter
			valueThresholdsCompoundLowLevelAbstractionConverter;
	private final FrequencySliceAbstractionConverter
			frequencySliceAbstractionConverter;
	private final FrequencyHighLevelAbstractionConverter
			frequencyHighLevelAbstractionConverter;

	@Inject
	public PropositionDefinitionConverterVisitor(/*SystemPropositionConverter
														 inSystemPropositionConverter,*/
												 CategorizationConverter inCategorizationConverter,
												 SequenceConverter inSequenceConverter,
												 ValueThresholdsLowLevelAbstractionConverter
														 inValueThresholdsLowLevelAbstractionConverter,
												 ValueThresholdsCompoundLowLevelAbstractionConverter
														 inValueThresholdsCompoundLowLevelAbstractionConverter,
												 FrequencySliceAbstractionConverter inFrequencySliceAbstractionConverter,
												 FrequencyHighLevelAbstractionConverter
														 inFrequencyHighLevelAbstractionConverter) {
//		systemPropositionConverter = inSystemPropositionConverter;
		categorizationConverter = inCategorizationConverter;
		categorizationConverter.setConverterVisitor(this);
		sequenceConverter = inSequenceConverter;
		sequenceConverter.setConverterVisitor(this);
		valueThresholdsLowLevelAbstractionConverter =
				inValueThresholdsLowLevelAbstractionConverter;
		valueThresholdsLowLevelAbstractionConverter.setConverterVisitor(this);
		valueThresholdsCompoundLowLevelAbstractionConverter =
				inValueThresholdsCompoundLowLevelAbstractionConverter;
		valueThresholdsCompoundLowLevelAbstractionConverter.setConverterVisitor(this);
		frequencySliceAbstractionConverter =
				inFrequencySliceAbstractionConverter;
		frequencySliceAbstractionConverter.setVisitor(this);
		frequencyHighLevelAbstractionConverter =
				inFrequencyHighLevelAbstractionConverter;
		frequencyHighLevelAbstractionConverter.setConverterVisitor(this);
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public List<PropositionDefinition> getPropositionDefinitions() {
		return propositionDefinitions;
	}

	public PropositionDefinition getPrimaryProposition() {
		return primaryProposition;
	}

	@Override
	public void visit(SystemProposition entity) {
		this.propositionDefinitions = Collections.emptyList();
//		this.systemPropositionConverter.setUserId(this.userId);
//		this.propositionDefinitions = this.systemPropositionConverter
//				.convert(entity);
//		this.primaryProposition = this.systemPropositionConverter
//				.getPrimaryPropositionDefinition();
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.propositionDefinitions = this.categorizationConverter
				.convert(entity);
		this.primaryProposition = this.categorizationConverter
				.getPrimaryPropositionDefinition();
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.propositionDefinitions = this.sequenceConverter
				.convert(entity);
		this.primaryProposition = this.sequenceConverter
				.getPrimaryPropositionDefinition();
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds() != null) {
			if (entity.getValueThresholds().size() > 1) {
				this.propositionDefinitions = this
						.valueThresholdsCompoundLowLevelAbstractionConverter
						.convert(entity);
				this.primaryProposition = this
						.valueThresholdsCompoundLowLevelAbstractionConverter
						.getPrimaryPropositionDefinition();
			} else {
				this.propositionDefinitions = this
						.valueThresholdsLowLevelAbstractionConverter
						.convert(entity);
				this.primaryProposition = this
						.valueThresholdsLowLevelAbstractionConverter
						.getPrimaryPropositionDefinition();
			}
		}
	}

	@Override
	public void visit(FrequencyEntity entity) {
		if (entity.isConsecutive()) {
			this.propositionDefinitions = this
					.frequencyHighLevelAbstractionConverter.convert(entity);
			this.primaryProposition = this
					.frequencyHighLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
		} else {
			this.propositionDefinitions = this.frequencySliceAbstractionConverter
					.convert(entity);
			this.primaryProposition = this.frequencySliceAbstractionConverter
					.getPrimaryPropositionDefinition();
		}
	}
}
