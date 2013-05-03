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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;

/**
 * Translates categorical data elements (UI element) into categorization
 * propositions.
 */
public final class CategorizationTranslator implements
		PropositionTranslator<Category, CategoryEntity> {

	private final SequenceTranslator sequenceTranslator;
	private final SystemPropositionTranslator systemPropositionTranslator;
	private final FrequencyTranslator frequencyTranslator;
	private final ValueThresholdsTranslator valueThresholdsTranslator;
	private final TranslatorSupport translatorSupport;

	@Inject
	public CategorizationTranslator(
			PropositionDao inPropositionDao,
			SystemPropositionFinder inFinder,
			SequenceTranslator inSequenceTranslator,
			SystemPropositionTranslator inSystemPropositionTranslator,
			FrequencyTranslator inFrequencyTranslator,
			ValueThresholdsTranslator inValueThresholdsTranslator) {
		this.translatorSupport =
				new TranslatorSupport(inPropositionDao, inFinder);
		this.sequenceTranslator = inSequenceTranslator;
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.frequencyTranslator = inFrequencyTranslator;
		this.valueThresholdsTranslator = inValueThresholdsTranslator;
	}

	@Override
	public CategoryEntity translateFromElement(Category element)
			throws DataElementHandlingException {
		CategoryEntity result =
				this.translatorSupport.getUserEntityInstance(element,
				CategoryEntity.class);

		List<DataElementEntity> inverseIsA = new ArrayList<DataElementEntity>();
		if (element.getChildren() != null) {
			for (DataElement de : element.getChildren()) {
				DataElementEntity proposition =
						this.translatorSupport.getSystemEntityInstance(
						element.getUserId(), de.getKey());
				inverseIsA.add(proposition);
			}
		}
		result.setMembers(inverseIsA);
		result.setCategoryType(checkPropositionType(element, inverseIsA));

		return result;
	}

	private CategoryType checkPropositionType(Category element,
			List<DataElementEntity> inverseIsA)
			throws DataElementHandlingException {
		if (inverseIsA.isEmpty()) {
			throw new DataElementHandlingException(
					Response.Status.PRECONDITION_FAILED, "Category "
					+ element.getKey()
					+ " is invalid because it has no children");
		}
		Set<CategoryType> categorizationTypes =
				EnumSet.noneOf(CategoryType.class);
		for (DataElementEntity dataElement : inverseIsA) {
			categorizationTypes.add(dataElement.getCategoryType());
		}
		if (categorizationTypes.size() > 1) {
			throw new DataElementHandlingException(
					Response.Status.PRECONDITION_FAILED, "Category "
					+ element.getKey()
					+ " has children with inconsistent types: "
					+ StringUtils.join(categorizationTypes, ", "));
		}
		return categorizationTypes.iterator().next();
	}

	@Override
	public Category translateFromProposition(
			CategoryEntity proposition) {
		Category result = new Category();

		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);
		List<DataElement> children = new ArrayList<DataElement>();
		for (DataElementEntity p : proposition.getMembers()) {
			PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
					this.systemPropositionTranslator, this.sequenceTranslator,
					this,
					this.frequencyTranslator, this.valueThresholdsTranslator);
			p.accept(visitor);
			children.add(visitor.getDataElement());
		}
		result.setChildren(children);
		result.setCategoricalType(checkElementType(proposition));

		return result;
	}

	private CategoricalType checkElementType(CategoryEntity proposition) {
		switch (proposition.getCategoryType()) {
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
			default:
				throw new AssertionError("Invalid category type: "
						+ proposition.getCategoryType());
		}
	}
}
