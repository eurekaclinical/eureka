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

import org.protempa.PropositionDefinition;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelatedDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.DataElementHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 * Translates from sequences (UI data element) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
		PropositionTranslator<Sequence, HighLevelAbstraction> {

	private Map<Long, ExtendedProposition> extendedProps;
	private final Map<String, Proposition> propositions;
	private final PropositionDao propositionDao;
	private final TimeUnitDao timeUnitDao;
	private final RelationOperatorDao relationOperatorDao;
	private final SystemPropositionFinder finder;
	private final SystemPropositionTranslator systemPropositionTranslator;

	@Inject
	public SequenceTranslator(PropositionDao inInPropositionDao,
			TimeUnitDao inTimeUnitDao, RelationOperatorDao inRelationOperatorDao,
			SystemPropositionFinder inFinder, SystemPropositionTranslator 
			inTranslator) {
		this.propositionDao = inInPropositionDao;
		this.timeUnitDao = inTimeUnitDao;
		this.relationOperatorDao = inRelationOperatorDao;
		this.finder = inFinder;
		this.systemPropositionTranslator = inTranslator;
		this.extendedProps = new HashMap<Long, ExtendedProposition>();
		this.propositions = new HashMap<String, Proposition>();
	}

	@Override
	public HighLevelAbstraction translateFromElement(Sequence element)
			throws DataElementHandlingException {
		HighLevelAbstraction result = new HighLevelAbstraction();
		PropositionTranslatorUtil.populateCommonPropositionFields(result,
				element);
		result.setCreatedFrom(HighLevelAbstraction.CreatedFrom.SEQUENCE);

		List<Proposition> abstractedFrom = new ArrayList<Proposition>();
		createExtendedProposition(element.getPrimaryDataElement(),
				element.getUserId());
		result.setPrimaryProposition(extendedProps.get(element
				.getPrimaryDataElement().getId()));
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			createExtendedProposition(rde.getDataElementField(),
					element.getUserId());
			abstractedFrom.add(getOrCreateProposition(rde.getDataElementField()
					.getDataElementKey(), element));
		}
		result.setAbstractedFrom(abstractedFrom);
		List<Relation> relations = new ArrayList<Relation>();
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			relations.add(createRelation(rde));
		}
		result.setRelations(relations);

		return result;
	}

	private Proposition getOrCreateProposition(String key, Sequence element)
			throws DataElementHandlingException {

		Proposition proposition = null;

		// first see if we already have the proposition
		if (this.propositions.containsKey(key)) {
			proposition = this.propositions.get(key);
		}

		// next we try to fetch it from the database
		if (proposition == null) {
			proposition = propositionDao.getByUserAndKey(element
				.getUserId(), key);
			this.propositions.put(key, proposition);
		}

		// finally, we try to fetch it from the ontology
		if (proposition == null) {
			try {
				PropositionDefinition propDef = finder.find(element.getUserId(),
						key);
				SystemElement systemElement = PropositionUtil.wrap(
						propDef, false, element.getUserId(), this.finder);
				proposition = this
						.systemPropositionTranslator.translateFromElement
								(systemElement);
				this.propositions.put(key, proposition);
			} catch (PropositionFindException ex) {
				throw new DataElementHandlingException(
						"Could not translate sequence " + element.getKey(),
						ex);
			}
		}
		return proposition;
	}

	private void createExtendedProposition(DataElementField dataElement,
			Long userId) throws DataElementHandlingException {
		if (!this.extendedProps.containsKey(dataElement.getId())) {
			ExtendedProposition ep = new ExtendedProposition();
			Proposition proposition = this.propositionDao.getByUserAndKey(
					userId, dataElement.getDataElementKey());
			if (proposition == null) {
				try {
				SystemElement element = PropositionUtil.wrap(
						this.finder.find(
						userId, dataElement.getDataElementKey()),
						true, userId, this.finder);
				SystemPropositionTranslator translator = new SystemPropositionTranslator(finder);
				proposition = translator.translateFromElement(element);
				} catch (PropositionFindException ex) {
					throw new DataElementHandlingException(
							"Could not translate data element " + 
							dataElement.getDataElementKey(), ex);
				}
			}
			ep.setProposition(proposition);
			if (dataElement.getHasDuration()) {
				ep.setMinDuration(dataElement.getMinDuration());
				ep.setMinDurationTimeUnit(this.timeUnitDao.retrieve(dataElement.getMinDurationUnits()));
				ep.setMaxDuration(dataElement.getMaxDuration());
				ep.setMaxDurationTimeUnit(this.timeUnitDao.retrieve(dataElement.getMaxDurationUnits()));
			}
			if (dataElement.getHasPropertyConstraint()) {
				PropertyConstraint pc = new PropertyConstraint();
				pc.setPropertyName(dataElement.getProperty());
				pc.setValue(dataElement.getPropertyValue());
				ValueComparator vc = new ValueComparator();
				vc.setName("EQUAL_TO");
				ep.setPropertyConstraint(pc);
			}

			this.extendedProps.put(dataElement.getId(), ep);
		}
	}

	private Relation createRelation(
			RelatedDataElementField relatedDataElementField) {
		Relation rel = new Relation();

		ExtendedProposition dataElement = this.extendedProps
				.get(relatedDataElementField.getDataElementField()
				.getId());
		ExtendedProposition relatedDataElement = this.extendedProps
				.get(relatedDataElementField.getSequentialDataElementSource());

		RelationOperator relationOperator = this.relationOperatorDao.retrieve(relatedDataElementField.getRelationOperator());

		rel.setMinf1s2(relatedDataElementField.getRelationMinCount());
		rel.setMinf1s2TimeUnit(this.timeUnitDao.retrieve(relatedDataElementField.getRelationMinUnits()));
		rel.setMaxf1s2(relatedDataElementField.getRelationMaxCount());
		rel.setMaxf1s2TimeUnit(this.timeUnitDao.retrieve(relatedDataElementField.getRelationMaxUnits()));
		rel.setOp(relationOperator);

		if (relationOperator.getName().equalsIgnoreCase("before")) {
			rel.setLhsExtendedProposition(dataElement);
			rel.setRhsExtendedProposition(relatedDataElement);
		} else if (relationOperator.getName().equalsIgnoreCase("after")) {
			rel.setLhsExtendedProposition(relatedDataElement);
			rel.setRhsExtendedProposition(dataElement);
		}

		return rel;
	}

	@Override
	public Sequence translateFromProposition(HighLevelAbstraction proposition) {
		Sequence result = new Sequence();
		PropositionTranslatorUtil.populateCommonDataElementFields(result,
				proposition);

		if (proposition.getPrimaryProposition() != null) {
			// identify the primary data element
			result.setPrimaryDataElement(createDataElementField(proposition
					.getPrimaryProposition()));

			List<RelatedDataElementField> relatedFields = new ArrayList<Sequence.RelatedDataElementField>();
			for (Relation relation : proposition.getRelations()) {
				relatedFields.add(createRelatedDataElementField(relation));
			}
			result.setRelatedDataElements(relatedFields);
		}

		return result;
	}

	private RelatedDataElementField createRelatedDataElementField(
			Relation relation) {
		RelatedDataElementField relatedDataElement = new RelatedDataElementField();

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
