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

import java.util.ArrayList;
import java.util.List;

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement.CategoricalType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization.CategorizationType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

/**
 * Translates categorical data elements (UI element) into categorization
 * propositions.
 */
public final class CategorizationTranslator implements
		PropositionTranslator<CategoricalElement, Categorization> {

	private final PropositionDao propositionDao;
	private final SystemPropositionFinder finder;
	private final SequenceTranslator sequenceTranslator;
	private final SystemPropositionTranslator systemPropositionTranslator;
	private final FrequencySliceTranslator frequencySliceTranslator;
	private final FrequencyLowLevelAbstractionTranslator frequencyLowLevelAbstractionTranslator;
	private final ResultThresholdsTranslator resultThresholdsTranslator;

	@Inject
	public CategorizationTranslator(
			PropositionDao inInPropositionDao,
			SystemPropositionFinder inFinder,
			SequenceTranslator inSequenceTranslator,
			SystemPropositionTranslator inSystemPropositionTranslator,
			FrequencySliceTranslator inFrequencySliceTranslator,
			FrequencyLowLevelAbstractionTranslator inFrequencyLowLevelAbstractionTranslator,
			ResultThresholdsTranslator inResultThresholdsTranslator) {
		this.propositionDao = inInPropositionDao;
		this.finder = inFinder;
		this.sequenceTranslator = inSequenceTranslator;
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.frequencySliceTranslator = inFrequencySliceTranslator;
		this.frequencyLowLevelAbstractionTranslator = inFrequencyLowLevelAbstractionTranslator;
		this.resultThresholdsTranslator = inResultThresholdsTranslator;
	}

	@Override
	public Categorization translateFromElement(CategoricalElement element) {
		Categorization result = new Categorization();
		PropositionTranslatorUtil.populateCommonPropositionFields(result,
				element);

		List<Proposition> inverseIsA = new ArrayList<Proposition>();
		for (DataElement de : element.getChildren()) {
			Proposition child = getOrCreateProposition(de.getKey(), element);
			inverseIsA.add(child);
		}
		result.setInverseIsA(inverseIsA);
		result.setKey(element.getAbbrevDisplayName());
		result.setCategorizationType(checkPropositionType(element));

		return result;
	}

	private Proposition getOrCreateProposition(String key,
			CategoricalElement element) {
		Proposition proposition = this.propositionDao.getByUserAndKey(
				element.getUserId(), key);
		if (proposition == null) {
			PropositionDefinition propDef = this.finder.find(
					element.getUserId(), key);
			SystemProposition sysProp = new SystemProposition();
			sysProp.setKey(key);
			sysProp.setInSystem(true);
			sysProp.setDisplayName(propDef.getDisplayName());
			sysProp.setAbbrevDisplayName(propDef.getAbbreviatedDisplayName());
			sysProp.setUserId(element.getUserId());
			sysProp.setCreated(element.getCreated());
			sysProp.setLastModified(element.getLastModified());
			proposition = sysProp;
		}
		return proposition;
	}

	private CategorizationType checkPropositionType(CategoricalElement element) {
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
	public CategoricalElement translateFromProposition(
			Categorization proposition) {
		CategoricalElement result = new CategoricalElement();

		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);
		List<DataElement> children = new ArrayList<DataElement>();
		for (Proposition p : proposition.getInverseIsA()) {
			PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
					this.systemPropositionTranslator, this.sequenceTranslator,
					this,
					this.frequencySliceTranslator,
					this.frequencyLowLevelAbstractionTranslator,
					this.resultThresholdsTranslator);
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
