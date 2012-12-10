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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction.CreatedFrom;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SliceAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class PropositionTranslatorVisitor implements
		PropositionEntityVisitor {

	private final SystemPropositionTranslator systemPropositionTranslator;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final FrequencySliceTranslator frequencySliceTranslator;
	private final FrequencyLowLevelAbstractionTranslator frequencyLowLevelAbstractionTranslator;
	private final ResultThresholdsTranslator resultThresholdsTranslator;
	private DataElement dataElement;

	@Inject
	public PropositionTranslatorVisitor(
			SystemPropositionTranslator inSystemPropositionTranslator,
			SequenceTranslator inSequenceTranslator,
			CategorizationTranslator inCategorizationTranslator,
			FrequencySliceTranslator inFrequencySliceTranslator,
			FrequencyLowLevelAbstractionTranslator inFrequencyLowLevelAbstractionTranslator,
			ResultThresholdsTranslator inResultThresholdsTranslator) {
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.sequenceTranslator = inSequenceTranslator;
		this.frequencySliceTranslator = inFrequencySliceTranslator;
		this.frequencyLowLevelAbstractionTranslator = inFrequencyLowLevelAbstractionTranslator;
		this.resultThresholdsTranslator = inResultThresholdsTranslator;
	}

	public DataElement getDataElement() {
		return dataElement;
	}

	@Override
	public void visit(SystemProposition proposition) {
		dataElement = this.systemPropositionTranslator
				.translateFromProposition(proposition);
	}

	@Override
	public void visit(Categorization categorization) {
		dataElement = this.categorizationTranslator
				.translateFromProposition(categorization);
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		dataElement = this.sequenceTranslator
				.translateFromProposition(highLevelAbstraction);
	}

	@Override
	public void visit(SliceAbstraction sliceAbstraction) {
		dataElement = this.frequencySliceTranslator
				.translateFromProposition(sliceAbstraction);
	}

	@Override
	public void visit(LowLevelAbstraction lowLevelAbstraction) {
		if (lowLevelAbstraction.getCreatedFrom() == CreatedFrom.FREQUENCY) {
			dataElement = this.frequencyLowLevelAbstractionTranslator
					.translateFromProposition(lowLevelAbstraction);
		} else if (lowLevelAbstraction.getCreatedFrom() == CreatedFrom.VALUE_THRESHOLD) {
			dataElement = this.resultThresholdsTranslator
					.translateFromProposition(lowLevelAbstraction);
		} else {
			throw new IllegalArgumentException(
					"Low-level abstractions may only be created from frequency or value threshold data elements");
		}
	}
}
