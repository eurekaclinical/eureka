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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization.CategorizationType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionTypeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

/**
 * Translates categorical data elements (UI element) into categorization
 * propositions.
 */
final class CategorizationTranslator implements
        PropositionTranslator<CategoricalElement, Categorization> {

	private final PropositionDao dao;
	private final SystemPropositionFinder finder;

	public CategorizationTranslator(PropositionDao inDao, SystemPropositionFinder inFinder) {
		this.dao = inDao;
		this.finder = inFinder;
	}

	@Override
	public Categorization translateFromElement(CategoricalElement element) {
		Categorization result = new Categorization();
		PropositionTranslatorUtil.populateCommonPropositionFields(result,
		        element);

		PropositionTypeVisitor visitor = new PropositionTypeVisitor();
		CategorizationType type = CategorizationType.UNKNOWN;
		List<Proposition> inverseIsA = new ArrayList<Proposition>();
		for (DataElement de : element.getChildren()) {
			Proposition child = getOrCreateProposition(de.getKey(), element);
			inverseIsA.add(child);
			child.accept(visitor);
			CategorizationType childType = checkType(child, visitor.getType());
			if (type == CategorizationType.UNKNOWN) {
				type = childType;
			} else if (childType != type) {
				type = CategorizationType.MIXED;
			}

		}
		result.setInverseIsA(inverseIsA);
		result.setCategorizationType(type);

		return result;
	}
	
	private Proposition getOrCreateProposition(String key, CategoricalElement element) {
		Proposition proposition = dao.getByUserAndKey(element.getUserId(), key);
		if (proposition == null) {
			PropositionDefinition propDef = finder.find(element.getUserId(), key);
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

	private CategorizationType checkType(Proposition child,
	        DataElement.Type type) {
		switch (type) {
		case CATEGORIZATION:
			return ((Categorization) child).getCategorizationType();
		case SEQUENCE:
			// fall through
		case FREQUENCY:
			// fall through
		case VALUE_THRESHOLD:
			return CategorizationType.ABSTRACTION;
		case SYSTEM:
			switch (((SystemProposition) child).getSystemType()) {
			case CONSTANT:
				return CategorizationType.CONSTANT;
			case EVENT:
				return CategorizationType.EVENT;
			case PRIMITIVE_PARAMETER:
				return CategorizationType.PRIMITIVE_PARAMETER;
			case HIGH_LEVEL_ABSTRACTION:
				// fall through
			case LOW_LEVEL_ABSTRACTION:
				// fall through
			case SLICE_ABSTRACTION:
				return CategorizationType.ABSTRACTION;
			}
		}
		return CategorizationType.UNKNOWN;
	}

	@Override
	public CategoricalElement translateFromProposition(Categorization proposition) {
		CategoricalElement result = new CategoricalElement();
		
		PropositionTranslatorUtil.populateCommonDataElementFields(result, proposition);
		PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(proposition.getUserId(), dao, null);
		List<DataElement> children = new ArrayList<DataElement>();
		for (Proposition p : proposition.getInverseIsA()) {
			p.accept(visitor);
			children.add(visitor.getDataElement());
		}
		
		return result;
	}
}
