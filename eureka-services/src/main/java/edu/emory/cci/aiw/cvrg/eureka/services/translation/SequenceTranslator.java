/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import org.eurekaclinical.eureka.client.comm.PhenotypeField;
import org.eurekaclinical.eureka.client.comm.Sequence;
import org.eurekaclinical.eureka.client.comm.RelatedPhenotypeField;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.PhenotypeEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedPhenotype;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionTypeVisitor;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.Relation;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ExtendedPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PropositionTypeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.Relation;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SequenceEntity;
import org.eurekaclinical.eureka.client.comm.exception.PhenotypeHandlingException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.resource.SourceConfigResource;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PhenotypeEntityDao;

/**
 * Translates from sequences (UI phenotype) to high-level abstractions.
 * Creates extended propositions and relations as needed.
 */
public class SequenceTranslator implements
		PropositionTranslator<Sequence, SequenceEntity> {

	private Map<Long, ExtendedPhenotype> extendedProps;
	private final Map<String, PhenotypeEntity> propositions;
	private final TimeUnitDao timeUnitDao;
	private final RelationOperatorDao relationOperatorDao;
	private final TranslatorSupport translatorSupport;
	private final ValueComparatorDao valueComparatorDao;

	@Inject
	public SequenceTranslator(PhenotypeEntityDao inPropositionDao,
			TimeUnitDao inTimeUnitDao, RelationOperatorDao inRelationOperatorDao,
			SystemPropositionFinder inFinder,
			ValueComparatorDao inValueComparatorDao,
			SourceConfigResource inSourceConfigResource) {
		this.translatorSupport =
				new TranslatorSupport(inPropositionDao, inFinder, inSourceConfigResource);
		this.timeUnitDao = inTimeUnitDao;
		this.relationOperatorDao = inRelationOperatorDao;
		this.extendedProps = new HashMap<>();
		this.propositions = new HashMap<>();
		this.valueComparatorDao = inValueComparatorDao;
	}

	@Override
	public SequenceEntity translateFromPhenotype(Sequence phenotype)
			throws PhenotypeHandlingException {
		if (phenotype == null) {
			throw new IllegalArgumentException("phenotype cannot be null");
		}
		Long userId = phenotype.getUserId();
		SequenceEntity result =
				this.translatorSupport.getUserEntityInstance(phenotype,
				SequenceEntity.class);

		Map<String, PhenotypeField> phenotypesMap =
				new HashMap<>();
		PhenotypeField primaryPhenotypeField = phenotype.getPrimaryPhenotype();
		ExtendedPhenotype ep =
				createExtendedProposition(result.getPrimaryExtendedPhenotype(),
				primaryPhenotypeField, Long.valueOf(1), userId);
		phenotypesMap.put(primaryPhenotypeField.getPhenotypeKey(), primaryPhenotypeField);
		result.setPrimaryExtendedPhenotype(ep);

		List<Relation> relations = result.getRelations();
		if (relations == null) {
			relations = new ArrayList<>();
			result.setRelations(relations);
		}

		int i = 0;
		for (RelatedPhenotypeField rde : phenotype.getRelatedPhenotypes()) {
			ExtendedPhenotype lhsEP = null;
			ExtendedPhenotype rhsEP = null;
			Relation relation;
			if (relations.size() > i) {
				relation = relations.get(i);
				lhsEP = relation.getLhsExtendedPhenotype();
				rhsEP = relation.getRhsExtendedPhenotype();
			} else {
				relation = new Relation();
				relations.add(relation);
			}

			PhenotypeField lhsDEF = rde.getPhenotypeField();
			lhsEP = createExtendedProposition(lhsEP,
					rde.getPhenotypeField(), Long.valueOf(i + 2), userId);
			phenotypesMap.put(lhsDEF.getPhenotypeKey(), lhsDEF);
			PhenotypeField rhsDEF = phenotypesMap.get(
					rde.getSequentialPhenotype());
			if (rhsDEF == null) {
				throw new PhenotypeHandlingException(
						Response.Status.PRECONDITION_FAILED,
						"Invalid phenotype "
						+ rde.getSequentialPhenotype());
			}
			rhsEP = createExtendedProposition(rhsEP, rhsDEF,
					rde.getSequentialPhenotypeSource(), userId);

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

			String relOpName = relationOperator.getName();
			if (relOpName.equals("before")) {
				relation.setLhsExtendedPhenotype(lhsEP);
				relation.setRhsExtendedPhenotype(rhsEP);
			} else if (relOpName.equals("after")) {
				relation.setLhsExtendedPhenotype(rhsEP);
				relation.setRhsExtendedPhenotype(lhsEP);
			} else {
				throw new PhenotypeHandlingException(
						Response.Status.BAD_REQUEST,
						"Invalid temporal relationship '" + relOpName + "'");
			}

			i++;
		}

		return result;
	}

	private PhenotypeEntity getOrCreateProposition(Long userId, String key)
			throws PhenotypeHandlingException {

		PhenotypeEntity proposition = null;

		// first see if we already have the proposition
		if (this.propositions.containsKey(key)) {
			proposition = this.propositions.get(key);
		}

		// next we try to fetch it from the database
		if (proposition == null) {
			proposition =
					this.translatorSupport.getUserOrSystemEntityInstance(userId, key);
			this.propositions.put(proposition.getKey(), proposition);
		}

		return proposition;
	}

	private ExtendedPhenotype createExtendedProposition(
			ExtendedPhenotype origExtendedProposition,
			PhenotypeField phenotype, Long sequenceNumber, Long userId)
			throws PhenotypeHandlingException {
		ExtendedPhenotype result =
				this.extendedProps.get(sequenceNumber);
		if (result == null) {
			ExtendedPhenotype ep = origExtendedProposition;
			if (origExtendedProposition == null) {
				ep = new ExtendedPhenotype();
			}
			PhenotypeEntity proposition =
					getOrCreateProposition(userId,
					phenotype.getPhenotypeKey());
			PropositionTranslatorUtil.populateExtendedProposition(ep,
					proposition, phenotype, timeUnitDao, valueComparatorDao);

			this.extendedProps.put(sequenceNumber, ep);
			result = ep;
		}
		return result;
	}

	@Override
	public Sequence translateFromProposition(SequenceEntity proposition) {
		Sequence result = new Sequence();
		PropositionTranslatorUtil.populateCommonPhenotypeFields(result,
				proposition);

		if (proposition.getPrimaryExtendedPhenotype() != null) {
			// identify the primary phenotype
			result.setPrimaryPhenotype(createPhenotypeField(proposition
					.getPrimaryExtendedPhenotype()));

			List<Relation> relations = proposition.getRelations();
			Long pId = proposition.getPrimaryExtendedPhenotype().getId();
			Map<Long, Long> sequentialSources =
					assignSources(pId, proposition);

			List<RelatedPhenotypeField> relatedFields =
					new ArrayList<>();
			for (Relation relation : relations) {
				RelatedPhenotypeField field =
						createRelatedPhenotypeField(relation);
				field.setSequentialPhenotypeSource(
						sequentialSources.get(
						relation.getRhsExtendedPhenotype().getId()));
				relatedFields.add(field);
			}
			result.setRelatedPhenotypes(relatedFields);
		}

		return result;
	}

	private RelatedPhenotypeField createRelatedPhenotypeField(
			Relation relation) {
		RelatedPhenotypeField relatedPhenotype =
				new RelatedPhenotypeField();

		relatedPhenotype.setRelationMinCount(relation.getMinf1s2());
		relatedPhenotype.setRelationMinUnits(relation.getMinf1s2TimeUnit()
				.getId());
		relatedPhenotype.setRelationMaxCount(relation.getMaxf1s2());
		relatedPhenotype.setRelationMaxUnits(relation.getMaxf1s2TimeUnit()
				.getId());
		relatedPhenotype.setRelationOperator(relation.getRelationOperator().getId());

		if (relation.getRelationOperator().getName().equalsIgnoreCase("before")) {
			relatedPhenotype
					.setPhenotypeField(createPhenotypeField(relation
					.getLhsExtendedPhenotype()));
			relatedPhenotype.setSequentialPhenotype(relation
					.getRhsExtendedPhenotype().getPhenotypeEntity().getKey());
		} else if (relation.getRelationOperator().getName().equalsIgnoreCase("after")) {
			relatedPhenotype
					.setPhenotypeField(createPhenotypeField(relation
					.getRhsExtendedPhenotype()));
			relatedPhenotype.setSequentialPhenotype(relation
					.getLhsExtendedPhenotype().getPhenotypeEntity().getKey());
		}

		return relatedPhenotype;
	}

	private PhenotypeField createPhenotypeField(ExtendedPhenotype ep) {

		PhenotypeField phenotype = new PhenotypeField();
		PhenotypeEntity entity = ep.getPhenotypeEntity();
		PropositionTypeVisitor visitor = new PropositionTypeVisitor();

		entity.accept(visitor);
		phenotype.setType(visitor.getType());
		phenotype.setPhenotypeKey(entity.getKey());
		phenotype.setPhenotypeDescription(entity.getDescription());
		phenotype.setPhenotypeDisplayName(entity.getDisplayName());
		if (ep.getMinDuration() != null) {
			phenotype.setHasDuration(true);
			phenotype.setMinDuration(ep.getMinDuration());
			phenotype.setMinDurationUnits(ep.getMinDurationTimeUnit().getId());
		}
		if (ep.getMaxDuration() != null) {
			phenotype.setHasDuration(true);
			phenotype.setMaxDuration(ep.getMaxDuration());
			phenotype.setMaxDurationUnits(ep.getMaxDurationTimeUnit().getId());
		}
		if (ep.getPropertyConstraint() != null) {
			phenotype.setHasPropertyConstraint(true);
			phenotype.setProperty(ep.getPropertyConstraint()
					.getPropertyName());
			phenotype.setPropertyValue(ep.getPropertyConstraint().getValue());
		}
		return phenotype;
	}

	private Map<Long, Long> assignSources(Long pId, SequenceEntity proposition) {
		// determine the correct source for each sequential phenotype
		Map<Long, Long> sequentialSources = new HashMap<>();
		sequentialSources.put(pId, Long.valueOf(1));
		int i = 2;
		for (Relation relation : proposition.getRelations()) {
			Long epId = relation.getRhsExtendedPhenotype().getId();
			if (!sequentialSources.containsKey(epId)) {
				sequentialSources.put(epId, Long.valueOf(i++));
			}
		}
		return sequentialSources;
	}
}
