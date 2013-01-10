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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category.CategoricalType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization.CategorizationType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Translates categorical data elements (UI element) into categorization
 * propositions.
 */
public final class CategorizationTranslator implements
		PropositionTranslator<Category, Categorization> {

	private final SequenceTranslator sequenceTranslator;
	private final SystemPropositionTranslator systemPropositionTranslator;
	private final FrequencySliceTranslator frequencySliceTranslator;
	private final FrequencyHighLevelAbstractionTranslator frequencyHighLevelAbstractionTranslator;
	private final ResultThresholdsLowLevelAbstractionTranslator resultThresholdsLowLevelAbstractionTranslator;
	private final ResultThresholdsCompoundLowLevelAbstractionTranslator resultThresholdsCompoundLowLevelAbstractionTranslator;
	private final TranslatorSupport translatorSupport;

	@Inject
	public CategorizationTranslator(
			PropositionDao inPropositionDao,
			SystemPropositionFinder inFinder,
			SequenceTranslator inSequenceTranslator,
			SystemPropositionTranslator inSystemPropositionTranslator,
			FrequencySliceTranslator inFrequencySliceTranslator,
			FrequencyHighLevelAbstractionTranslator inFrequencyHighLevelAbstractionTranslator,
			ResultThresholdsLowLevelAbstractionTranslator inResultThresholdsLowLevelAbstractionTranslator,
			ResultThresholdsCompoundLowLevelAbstractionTranslator inResultThresholdsCompoundLowLevelAbstractionTranslator) {
		this.translatorSupport = 
				new TranslatorSupport(inPropositionDao, inFinder);
		this.sequenceTranslator = inSequenceTranslator;
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.frequencySliceTranslator = inFrequencySliceTranslator;
		this.frequencyHighLevelAbstractionTranslator = 
				inFrequencyHighLevelAbstractionTranslator;
		this.resultThresholdsLowLevelAbstractionTranslator =
				inResultThresholdsLowLevelAbstractionTranslator;
		this.resultThresholdsCompoundLowLevelAbstractionTranslator = 
				inResultThresholdsCompoundLowLevelAbstractionTranslator;
	}

	@Override
	public Categorization translateFromElement(Category element)
			throws DataElementHandlingException {
		Categorization result = 
				this.translatorSupport.getUserEntityInstance(element, 
				Categorization.class);

		List<Proposition> inverseIsA = new ArrayList<Proposition>();
		for (DataElement de : element.getChildren()) {
			Proposition proposition = 
					this.translatorSupport.getSystemEntityInstance(
					element.getUserId(), de.getKey());
			inverseIsA.add(proposition);
		}
		result.setInverseIsA(inverseIsA);
		result.setCategorizationType(checkPropositionType(element));

		return result;
	}

	private CategorizationType checkPropositionType(Category element) {
		switch (element.getCategoricalType()) {
			case LOW_LEVEL_ABSTRACTION:
				return CategorizationType.LOW_LEVEL_ABSTRACTION;
			case HIGH_LEVEL_ABSTRACTION:
				return CategorizationType.HIGH_LEVEL_ABSTRACTION;
			case SLICE_ABSTRACTION:
				return CategorizationType.SLICE_ABSTRACTION;
			case CONSTANT:
				return CategorizationType.CONSTANT;
			case EVENT:
				return CategorizationType.EVENT;
			case PRIMITIVE_PARAMETER:
				return CategorizationType.PRIMITIVE_PARAMETER;
			case MIXED:
				return CategorizationType.MIXED;
			default:
				return CategorizationType.UNKNOWN;
		}
	}

	@Override
	public Category translateFromProposition(
			Categorization proposition) {
		Category result = new Category();

		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);
		List<DataElement> children = new ArrayList<DataElement>();
		for (Proposition p : proposition.getInverseIsA()) {
			PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
					this.systemPropositionTranslator, this.sequenceTranslator,
					this,
					this.frequencySliceTranslator,
					this.frequencyHighLevelAbstractionTranslator,
					this.resultThresholdsLowLevelAbstractionTranslator,
					this.resultThresholdsCompoundLowLevelAbstractionTranslator);
			p.accept(visitor);
			children.add(visitor.getDataElement());
		}
		result.setChildren(children);
		result.setCategoricalType(checkElementType(proposition));

		return result;
	}

	private CategoricalType checkElementType(Categorization proposition) {
		switch (proposition.getCategorizationType()) {
			case LOW_LEVEL_ABSTRACTION:
				return CategoricalType.LOW_LEVEL_ABSTRACTION;
			case HIGH_LEVEL_ABSTRACTION:
				return CategoricalType.HIGH_LEVEL_ABSTRACTION;
			case SLICE_ABSTRACTION:
				return CategoricalType.SLICE_ABSTRACTION;
			case CONSTANT:
				return CategoricalType.CONSTANT;
			case EVENT:
				return CategoricalType.EVENT;
			case PRIMITIVE_PARAMETER:
				return CategoricalType.PRIMITIVE_PARAMETER;
			case MIXED:
				return CategoricalType.MIXED;
			default:
				return CategoricalType.UNKNOWN;
		}
	}
}
