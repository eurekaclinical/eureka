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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CompoundValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SliceAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class PropositionTranslatorVisitor implements
		PropositionEntityVisitor {

	private final SystemPropositionTranslator systemPropositionTranslator;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final FrequencySliceTranslator frequencySliceTranslator;
	private final FrequencyHighLevelAbstractionTranslator frequencyHighLevelAbstractionTranslator;
	private final ResultThresholdsLowLevelAbstractionTranslator
			resultThresholdsLowLevelAbstractionTranslator;
	private final ResultThresholdsCompoundLowLevelAbstractionTranslator
			resultThresholdsCompoundLowLevelAbstractionTranslator;
	private DataElement dataElement;

	@Inject
	public PropositionTranslatorVisitor(
			SystemPropositionTranslator inSystemPropositionTranslator,
			SequenceTranslator inSequenceTranslator,
			CategorizationTranslator inCategorizationTranslator,
			FrequencySliceTranslator inFrequencySliceTranslator,
			FrequencyHighLevelAbstractionTranslator inFrequencyHighLevelAbstractionTranslator,
			ResultThresholdsLowLevelAbstractionTranslator
					inResultThresholdsLowLevelAbstractionTranslator,
			ResultThresholdsCompoundLowLevelAbstractionTranslator
					inResultThresholdsCompoundLowLevelAbstractionTranslator) {
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.sequenceTranslator = inSequenceTranslator;
		this.frequencySliceTranslator = inFrequencySliceTranslator;
		this.frequencyHighLevelAbstractionTranslator = inFrequencyHighLevelAbstractionTranslator;
		this.resultThresholdsLowLevelAbstractionTranslator =
				inResultThresholdsLowLevelAbstractionTranslator;
		this.resultThresholdsCompoundLowLevelAbstractionTranslator =
				inResultThresholdsCompoundLowLevelAbstractionTranslator;
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
		if (highLevelAbstraction.getCreatedFrom() == HighLevelAbstraction
				.CreatedFrom.SEQUENCE) {
		dataElement = this.sequenceTranslator
				.translateFromProposition(highLevelAbstraction);
		} else if (highLevelAbstraction.getCreatedFrom() ==
				HighLevelAbstraction.CreatedFrom.FREQUENCY) {
			dataElement = this.frequencyHighLevelAbstractionTranslator
					.translateFromProposition(highLevelAbstraction);
		} else {
			throw new IllegalStateException("HighLevelAbstraction can only be" +
					" created from sequence or frequency");
		}
	}

	@Override
	public void visit(SliceAbstraction sliceAbstraction) {
		dataElement = this.frequencySliceTranslator
				.translateFromProposition(sliceAbstraction);
	}

	@Override
	public void visit(ValueThresholdEntity valueThreshold) {
		dataElement = this.resultThresholdsLowLevelAbstractionTranslator
				.translateFromProposition(valueThreshold);
	}

	@Override
	public void visit(CompoundValueThreshold compoundLowLevelAbstraction) {
		dataElement = this
				.resultThresholdsCompoundLowLevelAbstractionTranslator
				.translateFromProposition(compoundLowLevelAbstraction);

	}
}
