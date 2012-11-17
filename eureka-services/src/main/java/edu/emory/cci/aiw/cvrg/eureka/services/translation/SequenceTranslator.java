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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelatedDataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropertyConstraint;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;

/**
 * Translates from sequences (UI data element) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
        PropositionTranslator<Sequence, HighLevelAbstraction> {

	private Map<String, ExtendedProposition> extendedProps;
	private final PropositionDao dao;
	private final Long userId;

	public SequenceTranslator(Long inUserId, PropositionDao dao) {
		this.userId = inUserId;
		this.dao = dao;
		this.extendedProps = new HashMap<String, ExtendedProposition>();
	}

	@Override
	public HighLevelAbstraction translate(Sequence element) {
		HighLevelAbstraction result = new HighLevelAbstraction();
		PropositionTranslatorUtil.populateCommonFields(result, element);

		List<Proposition> abstractedFrom = new ArrayList<Proposition>();
		createExtendedProposition(element.getPrimaryDataElement());
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			createExtendedProposition(rde.getDataElementField());
			abstractedFrom.add(dao.getByUserAndKey(this.userId,
				rde.getDataElementField().getDataElementKey()));
		}
		result.setAbstractedFrom(abstractedFrom);
		List<Relation> relations = new ArrayList<Relation>();
		for (RelatedDataElementField rde : element.getRelatedDataElements()) {
			relations.add(createRelation(rde));
		}
		result.setRelations(relations);

		return result;
	}

	private Proposition getOrCreateProposition (String key) {
		Proposition proposition = this.dao.getByUserAndKey(this.userId, key);
		if (proposition == null) {
		}
		return proposition;
	}

	private void createExtendedProposition(DataElementField dataElement) {
		if (!this.extendedProps.containsKey(dataElement.getDataElementKey())) {
			ExtendedProposition ep = new ExtendedProposition();
			ep.setId(dataElement.getDataElementKey());
			if (dataElement.getHasDuration()) {
				ep.setMinDuration(dataElement.getMinDuration());
				ep.setMaxDuration(dataElement.getMaxDuration());
			}
			if (dataElement.getHasPropertyConstraint()) {
				PropertyConstraint pc = new PropertyConstraint();
				pc.setPropertyName(dataElement.getProperty());
				pc.setValue(dataElement.getPropertyValue());
				ValueComparator vc = new ValueComparator();
				vc.setName("EQUAL_TO");
				ep.setPropertyConstraint(pc);
			}

			this.extendedProps.put(ep.getId(), ep);
		}
	}

	private Relation createRelation(
	        RelatedDataElementField relatedDataElementField) {
		Relation rel = new Relation();

		ExtendedProposition dataElement = this.extendedProps
		        .get(relatedDataElementField.getDataElementField());
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

		if (relatedDataElementField.getRelationOperator() == RelationOperator.BEFORE) {
			rel.setLhsExtendedProposition(dataElement);
			rel.setRhsExtendedProposition(relatedDataElement);
		} else if (relatedDataElementField.getRelationOperator() == RelationOperator.AFTER) {
			rel.setLhsExtendedProposition(relatedDataElement);
			rel.setRhsExtendedProposition(dataElement);
		}

		return rel;
	}
}
