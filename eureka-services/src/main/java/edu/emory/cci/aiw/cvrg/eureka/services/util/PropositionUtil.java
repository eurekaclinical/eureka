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
package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PairDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.PropositionDefinitionVisitor;
import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition.SystemType;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.packaging.PropositionDefinitionPackagerVisitor;

/**
 * Provides common utility functions operating on {@link Proposition}s.
 */
public final class PropositionUtil {

	private PropositionUtil() {
		// do not allow instantiation.
	}

//	public static DataElement wrap(Proposition inProposition,
//	        PropositionDao inPropositionDao, TimeUnitDao inTimeUnitDao,
//		SystemPropositionFinder inFinder) {
//		PropositionTranslatorVisitor visitor = new
//			PropositionTranslatorVisitor(inProposition.getUserId(), inPropositionDao, inTimeUnitDao, inFinder);
//		inProposition.accept(visitor);
//		return visitor.getDataElement();
//	}
	private static class PropositionDefinitionTypeVisitor implements
			PropositionDefinitionVisitor {

		private SystemType systemType;

		@Override
		public void visit(Collection<? extends PropositionDefinition> arg0) {
			throw new UnsupportedOperationException(
					"getting the type of a collection is not supported");
		}

		@Override
		public void visit(ConstantDefinition arg0) {
			systemType = SystemType.CONSTANT;
		}

		@Override
		public void visit(EventDefinition arg0) {
			systemType = SystemType.EVENT;
		}

		@Override
		public void visit(HighLevelAbstractionDefinition arg0) {
			systemType = SystemType.HIGH_LEVEL_ABSTRACTION;
		}

		@Override
		public void visit(LowLevelAbstractionDefinition arg0) {
			systemType = SystemType.LOW_LEVEL_ABSTRACTION;
		}

		@Override
		public void visit(PairDefinition arg0) {
			systemType = SystemType.HIGH_LEVEL_ABSTRACTION;
		}

		@Override
		public void visit(PrimitiveParameterDefinition arg0) {
			systemType = SystemType.PRIMITIVE_PARAMETER;
		}

		@Override
		public void visit(SliceDefinition arg0) {
			systemType = SystemType.SLICE_ABSTRACTION;
		}

		@Override
		public void visit(CompoundLowLevelAbstractionDefinition def) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
		
		
	}
	private static final PropositionDefinitionTypeVisitor PROP_DEF_TYPE_VISITOR = new PropositionDefinitionTypeVisitor();

	/**
	 * Wraps a proposition definition into a proposition wrapper.
	 */
	public static SystemElement wrap(PropositionDefinition inDefinition,
			boolean summarize, Long inUserId,
			SystemPropositionFinder inPropositionFinder)
			throws PropositionFindException {
		if (inDefinition == null) {
			throw new IllegalArgumentException("inDefinition cannot be null");
		}
		SystemElement systemElement = new SystemElement();
		systemElement.setKey(inDefinition.getId());
		systemElement.setInSystem(true);
		systemElement.setAbbrevDisplayName(inDefinition
				.getAbbreviatedDisplayName());
		systemElement.setDisplayName(inDefinition.getDisplayName());
		systemElement.setSummarized(summarize);
		systemElement.setParent(inDefinition.getChildren().length > 0);
		inDefinition.accept(PROP_DEF_TYPE_VISITOR);
		systemElement.setSystemType(PROP_DEF_TYPE_VISITOR.systemType);

		if (!summarize) {
			List<SystemElement> children = new ArrayList<SystemElement>();
			for (String key : inDefinition.getChildren()) {
				PropositionDefinition pd = 
						inPropositionFinder.find(inUserId, key);
				if (pd != null) {
					children.add(
							wrap(pd, true, inUserId, inPropositionFinder));
				} else {
					throw new PropositionFindException("Could not find child '" 
							+ key + "' of proposition definition '" 
							+ inDefinition.getId() + "'");
				}
			}
			systemElement.setChildren(children);

			List<String> properties = new ArrayList<String>();
			for (PropertyDefinition propertyDef : inDefinition
					.getPropertyDefinitions()) {
				properties.add(propertyDef.getName());
			}
			systemElement.setProperties(properties);
		}

		return systemElement;
	}

//	public static Proposition unwrap(DataElement inElement, Long inUserId,
//		PropositionDao inPropositionDao, TimeUnitDao inTimeUnitDao,
//		SystemPropositionFinder inFinder) {
//
//		DataElementTranslatorVisitor visitor = new DataElementTranslatorVisitor(
//		        inUserId, inPropositionDao, inTimeUnitDao, inFinder);
//		inElement.accept(visitor);
//		return visitor.getProposition();
//	}
	/**
	 * Converts a proposition entity into an equivalent proposition definition
	 * understood by Protempa.
	 *
	 * @param inProposition the {@link Proposition} to convert
	 * @return a {@link PropositionDefinition} corresponding to the given
	 * proposition entity
	 */
	public static PropositionDefinition pack(Proposition inProposition) {
		PropositionDefinitionPackagerVisitor visitor = new PropositionDefinitionPackagerVisitor(
				inProposition.getUserId());
		inProposition.accept(visitor);
		return visitor.getPropositionDefinition();
	}

	/**
	 * Converts a list of proposition entities into equivalent proposition
	 * definitions by repeatedly calling {@link #pack(Proposition)}.
	 *
	 * @param inPropositions a {@link List} of {@link Proposition}s to convert
	 * @return a {@link List} of {@link PropositionDefinition}s corresponding to
	 * the given proposition entities
	 */
	public static List<PropositionDefinition> packAll(
			List<Proposition> inPropositions, Long inUserId) {
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();

		for (Proposition p : inPropositions) {
			result.add(pack(p));
		}

		return result;
	}
}
