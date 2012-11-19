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
import java.util.List;

import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.packaging.PropositionDefinitionPackagerVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.DataElementTranslatorVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.translation.PropositionTranslatorVisitor;

/**
 * Provides common utility functions operating on {@link Proposition}s.
 */
public final class PropositionUtil {

	private PropositionUtil() {
		// do not allow instantiation.
	}

	public static DataElement wrap(Proposition inProposition,
	        PropositionDao inPropositionDao, SystemPropositionFinder inFinder) {
		PropositionTranslatorVisitor visitor = new PropositionTranslatorVisitor(
		        inProposition.getUserId(), inPropositionDao, inFinder);
		inProposition.accept(visitor);
		return visitor.getDataElement();
	}

	/**
	 * Wraps a proposition definition into a proposition wrapper.
	 */
	public static SystemElement wrap(PropositionDefinition inDefinition,
	        boolean summarize, Long inUserId,
	        SystemPropositionFinder inPropositionFinder) {
		SystemElement systemElement = new SystemElement();
		systemElement.setKey(inDefinition.getId());
		systemElement.setInSystem(true);
		systemElement.setAbbrevDisplayName(inDefinition
		        .getAbbreviatedDisplayName());
		systemElement.setDisplayName(inDefinition.getDisplayName());
		systemElement.setSummarized(summarize);
		systemElement.setParent(inDefinition.getChildren().length > 0);

		if (!summarize) {
			List<SystemElement> children = new ArrayList<SystemElement>();
			for (String key : inDefinition.getChildren()) {
				children.add(wrap(inPropositionFinder.find(inUserId, key),
				        true, inUserId, inPropositionFinder));
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

	public static Proposition unwrap(DataElement inElement, Long inUserId,
	        PropositionDao inPropositionDao, SystemPropositionFinder inFinder) {
		DataElementTranslatorVisitor visitor = new DataElementTranslatorVisitor(
		        inUserId, inPropositionDao, inFinder);
		inElement.accept(visitor);
		return visitor.getProposition();
	}

	/**
	 * Converts a proposition entity into an equivalent proposition definition
	 * understood by Protempa.
	 * 
	 * @param inProposition
	 *            the {@link Proposition} to convert
	 * @return a {@link PropositionDefinition} corresponding to the given
	 *         proposition entity
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
	 * @param inPropositions
	 *            a {@link List} of {@link Proposition}s to convert
	 * @return a {@link List} of {@link PropositionDefinition}s corresponding to
	 *         the given proposition entities
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
