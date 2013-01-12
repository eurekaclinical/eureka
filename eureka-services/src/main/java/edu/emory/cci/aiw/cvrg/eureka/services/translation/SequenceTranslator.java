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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelatedDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

/**
 * Translates from sequences (UI data element) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
		PropositionTranslator<Sequence, SequenceEntity> {

	private Map<Long, ExtendedProposition> extendedProps;
	private final Map<String, DataElementEntity> propositions;
	private final TimeUnitDao timeUnitDao;
	private final RelationOperatorDao relationOperatorDao;
	private final TranslatorSupport translatorSupport;
	private final ValueComparatorDao valueComparatorDao;

	@Inject
	public SequenceTranslator(PropositionDao inPropositionDao,
			TimeUnitDao inTimeUnitDao, RelationOperatorDao inRelationOperatorDao,
			SystemPropositionFinder inFinder,
			ValueComparatorDao inValueComparatorDao) {
		this.translatorSupport =
				new TranslatorSupport(inPropositionDao, inFinder);
		this.timeUnitDao = inTimeUnitDao;
		this.relationOperatorDao = inRelationOperatorDao;
		this.extendedProps = new HashMap<Long, ExtendedProposition>();
		this.propositions = new HashMap<String, DataElementEntity>();
		this.valueComparatorDao = inValueComparatorDao;
	}

	@Override
	public SequenceEntity translateFromElement(Sequence element)
			throws DataElementHandlingException {
		if (element == null) {
			throw new IllegalArgumentException("element cannot be null");
		}
		Long userId = element.getUserId();
		SequenceEntity result =
				this.translatorSupport.getUserEntityInstance(element,
				SequenceEntity.class);

		ExtendedProposition ep =
				createExtendedProposition(result.getPrimaryProposition(),
				element.getPrimaryDataElement(), userId);
		result.setPrimaryProposition(ep);

		List<Relation> relations = result.getRelations();
		if (relations == null) {
			relations = new ArrayList<Relation>();
			result.setRelations(relations);
		}

		int i = 0;
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			ExtendedProposition lhsEP;
			ExtendedProposition rhsEP;
			Relation relation;
			if (relations.size() > i) {
				relation = relations.get(i);
				lhsEP = relation.getLhsExtendedProposition();
				rhsEP = relation.getRhsExtendedProposition();
			} else {
				relation = new Relation();
				lhsEP = null;
				rhsEP = null;
				relations.add(relation);
			}
			
			lhsEP = createExtendedProposition(lhsEP,
					rde.getDataElementField(), userId);
			rhsEP = createExtendedProposition(rhsEP,
					rde.getDataElementField(), userId);
			
			RelationOperator relationOperator = 
					this.relationOperatorDao.retrieve(
					rde.getRelationOperator());

			relation.setMinf1s2(rde.getRelationMinCount());
			relation.setMinf1s2TimeUnit(
					this.timeUnitDao.retrieve(rde.getRelationMinUnits()));
			relation.setMaxf1s2(rde.getRelationMaxCount());
			relation.setMaxf1s2TimeUnit(
					this.timeUnitDao.retrieve(rde.getRelationMaxUnits()));
			relation.setOp(relationOperator);

			if (relationOperator.getName().equalsIgnoreCase("before")) {
				relation.setLhsExtendedProposition(lhsEP);
				relation.setRhsExtendedProposition(rhsEP);
			} else if (relationOperator.getName().equalsIgnoreCase("after")) {
				relation.setLhsExtendedProposition(rhsEP);
				relation.setRhsExtendedProposition(lhsEP);
			}
			
			i++;
		}

		List<DataElementEntity> abstractedFrom = new ArrayList<DataElementEntity>();
		for (ExtendedProposition extendedProp : this.extendedProps.values()) {
			abstractedFrom.add(extendedProp.getProposition());
		}
		result.setAbstractedFrom(abstractedFrom);

		return result;
	}

	private DataElementEntity getOrCreateProposition(Long userId, String key)
			throws DataElementHandlingException {

		DataElementEntity proposition = null;

		// first see if we already have the proposition
		if (this.propositions.containsKey(key)) {
			proposition = this.propositions.get(key);
		}

		// next we try to fetch it from the database
		if (proposition == null) {
			proposition =
					this.translatorSupport.getSystemEntityInstance(userId, key);
		}

		return proposition;
	}

	private ExtendedProposition createExtendedProposition(
			ExtendedProposition origExtendedProposition,
			DataElementField dataElement, Long userId)
			throws DataElementHandlingException {
		ExtendedProposition result =
				this.extendedProps.get(dataElement.getId());
		if (result == null) {
			ExtendedProposition ep = origExtendedProposition;
			if (origExtendedProposition == null) {
				ep = new ExtendedProposition();
			}
			DataElementEntity proposition =
					getOrCreateProposition(userId,
					dataElement.getDataElementKey());
			PropositionTranslatorUtil.populateExtendedProposition(ep,
					proposition, dataElement, timeUnitDao, valueComparatorDao);

			this.extendedProps.put(dataElement.getId(), ep);
			result = ep;
		}
		return result;
	}

	@Override
	public Sequence translateFromProposition(SequenceEntity proposition) {
		Sequence result = new Sequence();
		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);

		if (proposition.getPrimaryProposition() != null) {
			// identify the primary data element
			result.setPrimaryDataElement(createDataElementField(proposition
					.getPrimaryProposition()));

			// determine the correct source for each sequential data element
			Map<Long, Long> sequentialSources = new HashMap<Long, Long>();
			for (Relation relation : proposition.getRelations()) {
				Long epId = relation.getRhsExtendedProposition().getId();
				Long pId = proposition.getPrimaryProposition().getId();
				if (pId.equals(epId)) {
					sequentialSources.put(epId, pId);
				} else {
					for (Relation src : proposition.getRelations()) {
						Long srcId = src.getLhsExtendedProposition().getId();
						if (srcId.equals(epId)) {
							sequentialSources.put(epId, srcId);
							break;
						}
					}
				}
			}

			List<RelatedDataElementField> relatedFields = 
					new ArrayList<Sequence.RelatedDataElementField>();
			for (Relation relation : proposition.getRelations()) {
				RelatedDataElementField field = 
						createRelatedDataElementField(relation);
				field.setSequentialDataElementSource(
						sequentialSources.get(
						relation.getRhsExtendedProposition().getId()));
				relatedFields.add(field);
			}
			result.setRelatedDataElements(relatedFields);
		}

		return result;
	}

	private RelatedDataElementField createRelatedDataElementField(
			Relation relation) {
		RelatedDataElementField relatedDataElement = 
				new RelatedDataElementField();

		relatedDataElement.setRelationMinCount(relation.getMinf1s2());
		relatedDataElement.setRelationMinUnits(relation.getMinf1s2TimeUnit()
				.getId());
		relatedDataElement.setRelationMaxCount(relation.getMaxf1s2());
		relatedDataElement.setRelationMaxUnits(relation.getMaxf1s2TimeUnit()
				.getId());
		relatedDataElement.setRelationOperator(relation.getOp().getId());

		if (relation.getOp().getName().equalsIgnoreCase("before")) {
			relatedDataElement
					.setDataElementField(createDataElementField(relation
					.getLhsExtendedProposition()));
			relatedDataElement.setSequentialDataElement(relation
					.getRhsExtendedProposition().getProposition().getKey());
		} else if (relation.getOp().getName().equalsIgnoreCase("after")) {
			relatedDataElement
					.setDataElementField(createDataElementField(relation
					.getRhsExtendedProposition()));
			relatedDataElement.setSequentialDataElement(relation
					.getLhsExtendedProposition().getProposition().getKey());
		}

		return relatedDataElement;
	}

	private DataElementField createDataElementField(ExtendedProposition ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getProposition().getKey());
		dataElement.setDataElementAbbrevDisplayName(ep.getProposition()
				.getAbbrevDisplayName());
		dataElement.setDataElementDisplayName(ep.getProposition()
				.getDisplayName());
		if (ep.getMinDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMinDuration(ep.getMinDuration());
			dataElement.setMinDurationUnits(ep.getMinDurationTimeUnit().getId());
		}
		if (ep.getMaxDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMaxDuration(ep.getMaxDuration());
			dataElement.setMaxDurationUnits(ep.getMaxDurationTimeUnit().getId());
		}
		if (ep.getPropertyConstraint() != null) {
			dataElement.setHasPropertyConstraint(true);
			dataElement.setProperty(ep.getPropertyConstraint()
					.getPropertyName());
			dataElement.setPropertyValue(ep.getPropertyConstraint().getValue());
		}
		return dataElement;
	}
}
