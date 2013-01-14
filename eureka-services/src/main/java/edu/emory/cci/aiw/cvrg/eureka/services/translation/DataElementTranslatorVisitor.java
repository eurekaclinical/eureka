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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;

public final class DataElementTranslatorVisitor implements DataElementVisitor {

	private final SystemPropositionTranslator systemPropositionTranslator;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final FrequencyTranslator frequencyTranslator;
	private final ValueThresholdsTranslator valueThresholdsTranslator;

	private DataElementEntity proposition;

	@Inject
	public DataElementTranslatorVisitor(
	        SystemPropositionTranslator inSystemPropositionTranslator,
	        SequenceTranslator inSequenceTranslator,
	        CategorizationTranslator inCategorizationTranslator,
	        FrequencyTranslator inFrequencyTranslator,
	        ValueThresholdsTranslator inValueThresholdsTranslator) {
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.sequenceTranslator = inSequenceTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.frequencyTranslator = inFrequencyTranslator;
		this.valueThresholdsTranslator = inValueThresholdsTranslator;
	}

	public DataElementEntity getProposition() {
		return proposition;
	}

	@Override
	public void visit(SystemElement systemElement)
	        throws DataElementHandlingException {
		proposition = this.systemPropositionTranslator
		        .translateFromElement(systemElement);


	}

	@Override
	public void visit(Category categoricalElement)
	        throws DataElementHandlingException {
		proposition = this.categorizationTranslator
		        .translateFromElement(categoricalElement);
	}

	@Override
	public void visit(Sequence sequence) throws DataElementHandlingException {
		proposition = this.sequenceTranslator.translateFromElement(sequence);
	}

	@Override
	public void visit(Frequency frequency) throws DataElementHandlingException {
		proposition = this.frequencyTranslator.translateFromElement(frequency);
	}

	@Override
	public void visit(ValueThresholds thresholds)
	        throws DataElementHandlingException {
		proposition = this.valueThresholdsTranslator.translateFromElement(thresholds);
	}

}
