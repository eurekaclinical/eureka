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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelatedDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

/**
 * Translates from sequences (UI data element) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
        PropositionTranslator<Sequence, HighLevelAbstraction> {

	private Map<String, ExtendedProposition> extendedProps;
	private final PropositionDao dao;
	private final Long userId;
	private final SystemPropositionFinder finder;

	public SequenceTranslator(Long inUserId, PropositionDao inDao,
	        SystemPropositionFinder inFinder) {
		this.userId = inUserId;
		this.dao = inDao;
		this.finder = inFinder;
		this.extendedProps = new HashMap<String, ExtendedProposition>();
	}

	@Override
	public HighLevelAbstraction translateFromElement(Sequence element) {
		HighLevelAbstraction result = new HighLevelAbstraction();
		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);

		List<Proposition> abstractedFrom = new ArrayList<Proposition>();
		createExtendedProposition(element.getPrimaryDataElement());
		result.setPrimaryProposition(extendedProps.get(element.getKey()));
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			createExtendedProposition(rde.getDataElementField());
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

	private Proposition getOrCreateProposition(String key, Sequence element) {
		Proposition proposition = dao.getByUserAndKey(this.userId, key);
		if (proposition == null) {
			PropositionDefinition propDef = finder.find(userId, key);
			SystemProposition sysProp = new SystemProposition();
			sysProp.setKey(key);
			sysProp.setInSystem(true);
			sysProp.setDisplayName(propDef.getDisplayName());
			sysProp.setAbbrevDisplayName(propDef.getAbbreviatedDisplayName());
			sysProp.setUserId(userId);
			sysProp.setCreated(element.getCreated());
			sysProp.setLastModified(element.getLastModified());
			proposition = sysProp;
		}
		return proposition;
	}

	private void createExtendedProposition(DataElementField dataElement) {
		if (!this.extendedProps.containsKey(dataElement.getDataElementKey())) {
			ExtendedProposition ep = new ExtendedProposition();
			ep.setProposition(this.dao.getByUserAndKey(userId,
			        dataElement.getDataElementKey()));
			if (dataElement.getHasDuration()) {
				ep.setMinDuration(dataElement.getMinDuration());
				TimeUnit minDurationUnits = new TimeUnit();
				minDurationUnits.setName(dataElement.getMinDurationUnits());
				ep.setMinDurationTimeUnit(minDurationUnits);
				ep.setMaxDuration(dataElement.getMaxDuration());
				TimeUnit maxDurationUnits = new TimeUnit();
				maxDurationUnits.setName(dataElement.getMaxDurationUnits());
				ep.setMaxDurationTimeUnit(maxDurationUnits);
			}
			if (dataElement.getHasPropertyConstraint()) {
				PropertyConstraint pc = new PropertyConstraint();
				pc.setPropertyName(dataElement.getProperty());
				pc.setValue(dataElement.getPropertyValue());
				ValueComparator vc = new ValueComparator();
				vc.setName("EQUAL_TO");
				ep.setPropertyConstraint(pc);
			}

			this.extendedProps.put(dataElement.getDataElementKey(), ep);
		}
	}

	private Relation createRelation(
	        RelatedDataElementField relatedDataElementField) {
		Relation rel = new Relation();

		ExtendedProposition dataElement = this.extendedProps
		        .get(relatedDataElementField.getDataElementField()
		                .getDataElementKey());
		ExtendedProposition relatedDataElement = this.extendedProps
		        .get(relatedDataElementField.getSequentialDataElement());

		rel.setMinf1s2(relatedDataElementField.getRelationMinCount());
		TimeUnit minTimeUnit = new TimeUnit();
		minTimeUnit.setName(relatedDataElementField.getRelationMinUnits());
		rel.setMinf1s2TimeUnit(minTimeUnit);
		rel.setMaxf1s2(relatedDataElementField.getRelationMaxCount());
		TimeUnit maxTimeUnit = new TimeUnit();
		maxTimeUnit.setName(relatedDataElementField.getRelationMaxUnits());
		rel.setMaxf1s2TimeUnit(maxTimeUnit);

		edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator op = new edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator();
		if (relatedDataElementField.getRelationOperator() == RelationOperator.BEFORE) {
			rel.setLhsExtendedProposition(dataElement);
			rel.setRhsExtendedProposition(relatedDataElement);
			op.setName("BEFORE");
			rel.setOp(op);

		} else if (relatedDataElementField.getRelationOperator() == RelationOperator.AFTER) {
			rel.setLhsExtendedProposition(relatedDataElement);
			rel.setRhsExtendedProposition(dataElement);
			op.setName("AFTER");
			rel.setOp(op);
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
		}

		return result;
	}

	private RelatedDataElementField createRelatedDataElementField(
	        Relation relation) {
		RelatedDataElementField relatedDataElement = new RelatedDataElementField();

		relatedDataElement.setRelationMinCount(relation.getMinf1s2());
		relatedDataElement.setRelationMaxUnits(relation.getMaxf1s2TimeUnit()
		        .getName());
		relatedDataElement.setRelationMaxCount(relation.getMaxf1s2());
		relatedDataElement.setRelationMaxUnits(relation.getMaxf1s2TimeUnit()
		        .getName());

		if (relation.getOp().getName().equals("BEFORE")) {
			relatedDataElement
			        .setDataElementField(createDataElementField(relation
			                .getLhsExtendedProposition()));
			relatedDataElement.setSequentialDataElement(relation
			        .getRhsExtendedProposition().getProposition().getKey());
			relatedDataElement.setRelationOperator(RelationOperator.BEFORE);
		} else if (relation.getOp().getName().equals("AFTER")) {
			relatedDataElement
			        .setDataElementField(createDataElementField(relation
			                .getRhsExtendedProposition()));
			relatedDataElement.setSequentialDataElement(relation
			        .getLhsExtendedProposition().getProposition().getKey());
			relatedDataElement.setRelationOperator(RelationOperator.AFTER);
		}

		return relatedDataElement;
	}

	private DataElementField createDataElementField(ExtendedProposition ep) {
		DataElementField dataElement = new DataElementField();
		dataElement.setDataElementKey(ep.getProposition().getKey());
		if (ep.getMinDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMinDuration(ep.getMinDuration());
			dataElement.setMinDurationUnits(ep.getMinDurationTimeUnit()
			        .getName());
		}
		if (ep.getMaxDuration() != null) {
			dataElement.setHasDuration(true);
			dataElement.setMaxDuration(ep.getMaxDuration());
			dataElement.setMaxDurationUnits(ep.getMaxDurationTimeUnit()
			        .getName());
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
