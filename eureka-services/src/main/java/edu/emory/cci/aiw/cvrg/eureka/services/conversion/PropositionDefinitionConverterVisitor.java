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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdGroupEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.protempa.PropositionDefinition;

import java.util.List;
import java.util.Set;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntityVisitor;

public final class PropositionDefinitionConverterVisitor implements
		PhenotypeEntityVisitor {

	private List<PropositionDefinition> propositionDefinitions;
	private final Set<String> propIds;
	private PropositionDefinition primaryProposition;
	private String primaryPropositionId;
	private final SystemPropositionConverter systemPropositionConverter;
	private final CategorizationConverter categorizationConverter;
	private final SequenceConverter sequenceConverter;
	private final ValueThresholdsLowLevelAbstractionConverter valueThresholdsLowLevelAbstractionConverter;
	private final ValueThresholdsCompoundLowLevelAbstractionConverter valueThresholdsCompoundLowLevelAbstractionConverter;
	private final FrequencyNotValueThresholdConverter frequencyNotValueThresholdConverter;
	private final FrequencyValueThresholdConverter frequencyValueThresholdConverter;

	@Inject
	public PropositionDefinitionConverterVisitor(SystemPropositionConverter inSystemPropositionConverter,
			CategorizationConverter inCategorizationConverter,
			SequenceConverter inSequenceConverter,
			ValueThresholdsLowLevelAbstractionConverter inValueThresholdsLowLevelAbstractionConverter,
			ValueThresholdsCompoundLowLevelAbstractionConverter inValueThresholdsCompoundLowLevelAbstractionConverter,
			FrequencyNotValueThresholdConverter inFrequencySliceAbstractionConverter,
			FrequencyValueThresholdConverter inFrequencyHighLevelAbstractionConverter) {
		systemPropositionConverter = inSystemPropositionConverter;
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
		frequencyNotValueThresholdConverter =
				inFrequencySliceAbstractionConverter;
		frequencyNotValueThresholdConverter.setVisitor(this);
		frequencyValueThresholdConverter =
				inFrequencyHighLevelAbstractionConverter;
		frequencyValueThresholdConverter.setConverterVisitor(this);
		propIds = new HashSet<>();
	}

	public Collection<PropositionDefinition> getPropositionDefinitions() {
		return propositionDefinitions;
	}

	public PropositionDefinition getPrimaryProposition() {
		return primaryProposition;
	}

	public String getPrimaryPropositionId() {
		return primaryPropositionId;
	}

	public boolean addPropositionId(String propId) {
		return this.propIds.add(propId);
	}

	@Override
	public void visit(SystemProposition entity) {
		this.propositionDefinitions = Collections.emptyList();
		this.propositionDefinitions = this.systemPropositionConverter
				.convert(entity);
		this.primaryProposition = this.systemPropositionConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.systemPropositionConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.propositionDefinitions = this.categorizationConverter
				.convert(entity);
		this.primaryProposition = this.categorizationConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.categorizationConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.propositionDefinitions = this.sequenceConverter
				.convert(entity);
		this.primaryProposition = this.sequenceConverter
				.getPrimaryPropositionDefinition();
		this.primaryPropositionId =
				this.sequenceConverter.getPrimaryPropositionId();
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds() != null
				&& entity.getValueThresholds().size() > 1) {
			this.propositionDefinitions = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.convert(entity);
			this.primaryProposition = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId = 
					this.valueThresholdsCompoundLowLevelAbstractionConverter
					.getPrimaryPropositionId();
		} else {
			this.propositionDefinitions = this.valueThresholdsLowLevelAbstractionConverter
					.convert(entity);
			this.primaryProposition = this.valueThresholdsLowLevelAbstractionConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.valueThresholdsLowLevelAbstractionConverter
					.getPrimaryPropositionId();
		}
	}

	@Override
	public void visit(FrequencyEntity entity) {
		if (entity.getAbstractedFrom() instanceof ValueThresholdGroupEntity) {
			this.propositionDefinitions = 
					this.frequencyValueThresholdConverter.convert(entity);
			this.primaryProposition = this.frequencyValueThresholdConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.frequencyValueThresholdConverter
					.getPrimaryPropositionId();
		} else {
			this.propositionDefinitions = 
					this.frequencyNotValueThresholdConverter.convert(entity);
			this.primaryProposition = this.frequencyNotValueThresholdConverter
					.getPrimaryPropositionDefinition();
			this.primaryPropositionId =
					this.frequencyNotValueThresholdConverter
					.getPrimaryPropositionId();
		}
	}
}
