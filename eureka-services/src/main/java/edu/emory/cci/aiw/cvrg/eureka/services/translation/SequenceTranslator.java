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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
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
import javax.ws.rs.core.Response;

/**
 * Translates from sequences (UI data element) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
		PropositionTranslator<Sequence, SequenceEntity> {

	private Map<Long, ExtendedDataElement> extendedProps;
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
		this.extendedProps = new HashMap<Long, ExtendedDataElement>();
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

		Map<String, DataElementField> dataElementsMap =
				new HashMap<String, DataElementField>();
		DataElementField primaryDataElementField = element.getPrimaryDataElement();
		ExtendedDataElement ep =
				createExtendedProposition(result.getPrimaryExtendedDataElement(),
				primaryDataElementField, Long.valueOf(1), userId);
		dataElementsMap.put(primaryDataElementField.getDataElementKey(), primaryDataElementField);
		result.setPrimaryExtendedDataElement(ep);

		List<Relation> relations = result.getRelations();
		if (relations == null) {
			relations = new ArrayList<Relation>();
			result.setRelations(relations);
		}

		int i = 0;
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			ExtendedDataElement lhsEP;
			ExtendedDataElement rhsEP;
			Relation relation;
			if (relations.size() > i) {
				relation = relations.get(i);
				lhsEP = relation.getLhsExtendedDataElement();
				rhsEP = relation.getRhsExtendedDataElement();
			} else {
				relation = new Relation();
				lhsEP = null;
				rhsEP = null;
				relations.add(relation);
			}

			DataElementField lhsDEF = rde.getDataElementField();
			lhsEP = createExtendedProposition(lhsEP,
					rde.getDataElementField(), Long.valueOf(i + 2), userId);
			dataElementsMap.put(lhsDEF.getDataElementKey(), lhsDEF);
			DataElementField rhsDEF = dataElementsMap.get(
					rde.getSequentialDataElement());
			if (rhsDEF == null) {
				throw new DataElementHandlingException(
						Response.Status.PRECONDITION_FAILED,
						"Invalid data element "
						+ rde.getSequentialDataElement());
			}
			rhsEP = createExtendedProposition(rhsEP, rhsDEF, rde.getSequentialDataElementSource(), userId);

			RelationOperator relationOperator =
					this.relationOperatorDao.retrieve(
					rde.getRelationOperator());

			relation.setMinf1s2(rde.getRelationMinCount());
			relation.setMinf1s2TimeUnit(
					this.timeUnitDao.retrieve(rde.getRelationMinUnits()));
			relation.setMaxf1s2(rde.getRelationMaxCount());
			relation.setMaxf1s2TimeUnit(
					this.timeUnitDao.retrieve(rde.getRelationMaxUnits()));
			relation.setRelationOperator(relationOperator);

			if (relationOperator.getName().equalsIgnoreCase("before")) {
				relation.setLhsExtendedDataElement(lhsEP);
				relation.setRhsExtendedDataElement(rhsEP);
			} else if (relationOperator.getName().equalsIgnoreCase("after")) {
				relation.setLhsExtendedDataElement(rhsEP);
				relation.setRhsExtendedDataElement(lhsEP);
			}

			i++;
		}

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

	private ExtendedDataElement createExtendedProposition(
			ExtendedDataElement origExtendedProposition,
			DataElementField dataElement, Long sequenceNumber, Long userId)
			throws DataElementHandlingException {
		ExtendedDataElement result =
				this.extendedProps.get(sequenceNumber);
		if (result == null) {
			ExtendedDataElement ep = origExtendedProposition;
			if (origExtendedProposition == null) {
				ep = new ExtendedDataElement();
			}
			DataElementEntity proposition =
					getOrCreateProposition(userId,
					dataElement.getDataElementKey());
			PropositionTranslatorUtil.populateExtendedProposition(ep,
					proposition, dataElement, timeUnitDao, valueComparatorDao);

			this.extendedProps.put(sequenceNumber, ep);
			result = ep;
		}
		return result;
	}

	@Override
	public Sequence translateFromProposition(SequenceEntity proposition) {
		Sequence result = new Sequence();
		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);

		if (proposition.getPrimaryExtendedDataElement() != null) {
			// identify the primary data element
			result.setPrimaryDataElement(createDataElementField(proposition
					.getPrimaryExtendedDataElement()));

			List<Relation> relations = proposition.getRelations();
			Long pId = proposition.getPrimaryExtendedDataElement().getId();
			Map<Long, Long> sequentialSources = 
					assignSources(pId, proposition);

			List<RelatedDataElementField> relatedFields =
					new ArrayList<Sequence.RelatedDataElementField>();
			for (Relation relation : relations) {
				RelatedDataElementField field =
						createRelatedDataElementField(relation);
				field.setSequentialDataElementSource(
						sequentialSources.get(
						relation.getRhsExtendedDataElement().getId()));
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
		relatedDataElement.setRelationOperator(relation.getRelationOperator().getId());

		if (relation.getRelationOperator().getName().equalsIgnoreCase("before")) {
			relatedDataElement
					.setDataElementField(createDataElementField(relation
					.getLhsExtendedDataElement()));
			relatedDataElement.setSequentialDataElement(relation
					.getRhsExtendedDataElement().getDataElementEntity().getKey());
		} else if (relation.getRelationOperator().getName().equalsIgnoreCase("after")) {
			relatedDataElement
					.setDataElementField(createDataElementField(relation
					.getRhsExtendedDataElement()));
			relatedDataElement.setSequentialDataElement(relation
					.getLhsExtendedDataElement().getDataElementEntity().getKey());
		}

		return relatedDataElement;
	}

	private DataElementField createDataElementField(ExtendedDataElement ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getDataElementEntity().getKey());
		dataElement.setDataElementDescription(ep.getDataElementEntity()
				.getDescription());
		dataElement.setDataElementDisplayName(ep.getDataElementEntity()
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

	private Map<Long, Long> assignSources(Long pId, SequenceEntity proposition) {
		// determine the correct source for each sequential data element
		Map<Long, Long> sequentialSources = new HashMap<Long, Long>();
		sequentialSources.put(pId, Long.valueOf(1));
		int i = 2;
		for (Relation relation : proposition.getRelations()) {
			Long epId = relation.getRhsExtendedDataElement().getId();
			if (!sequentialSources.containsKey(epId)) {
				sequentialSources.put(epId, Long.valueOf(i++));
			}
		}
		return sequentialSources;
	}
}
