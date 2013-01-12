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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

public final class PropositionTranslatorVisitor implements
		PropositionEntityVisitor {

	private final SystemPropositionTranslator systemPropositionTranslator;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final FrequencyTranslator frequencyTranslator;
	private final ValueThresholdsTranslator valueThresholdsTranslator;
	
	private DataElement dataElement;

	@Inject
	public PropositionTranslatorVisitor(
			SystemPropositionTranslator inSystemPropositionTranslator,
			SequenceTranslator inSequenceTranslator,
			CategorizationTranslator inCategorizationTranslator,
			FrequencyTranslator inFrequencyTranslator,
			ValueThresholdsTranslator inValueThresholdsTranslator) {
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.sequenceTranslator = inSequenceTranslator;
		this.frequencyTranslator = inFrequencyTranslator;
		this.valueThresholdsTranslator = inValueThresholdsTranslator;
	}

	public DataElement getDataElement() {
		return dataElement;
	}

	@Override
	public void visit(SystemProposition entity) {
		dataElement = this.systemPropositionTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(CategoryEntity entity) {
		dataElement = this.categorizationTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(SequenceEntity entity) {
		dataElement = this.sequenceTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(FrequencyEntity entity) {
		dataElement = this.frequencyTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		dataElement = this.valueThresholdsTranslator
				.translateFromProposition(entity);
	}
}
