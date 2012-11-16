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

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionChildrenVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionTypeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.packaging.PropositionDefinitionPackagerVisitor;

/**
 * Provides common utility functions operating on {@link Proposition}s.
 */
public final class PropositionUtil {

	private PropositionUtil() {
		// do not allow instantiation.
	}

	private static List<Proposition> getTargets(Proposition inProposition) {
		List<Proposition> propositions;
		List<Proposition> targets;

		PropositionChildrenVisitor visitor = new PropositionChildrenVisitor();
		inProposition.accept(visitor);
		targets = visitor.getChildren();

		if (targets == null) {
			propositions = new ArrayList<Proposition>();
		} else {
			propositions = targets;
		}

		return propositions;
	}

	public static PropositionWrapper wrap(Proposition inProposition,
	        boolean summarize) {

		PropositionWrapper wrapper = new PropositionWrapper();
		List<Proposition> targets = PropositionUtil.getTargets(inProposition);

		if (!summarize) {
			List<PropositionWrapper> children = new ArrayList<PropositionWrapper>();
			for (Proposition target : targets) {
				children.add(PropositionUtil.wrap(target, true));
			}
			wrapper.setChildren(children);
		}

		if (inProposition.getId() != null) {
			wrapper.setId(inProposition.getId());
		}

		if (inProposition.getUserId() != null) {
			wrapper.setUserId(inProposition.getUserId());
		}

		wrapper.setParent(targets.size() > 0);
		wrapper.setSummarized(summarize);
		wrapper.setInSystem(inProposition.isInSystem());
		wrapper.setAbbrevDisplayName(inProposition.getAbbrevDisplayName());
		wrapper.setDisplayName(inProposition.getDisplayName());
		wrapper.setKey(inProposition.getKey());
		wrapper.setCreated(inProposition.getCreated());
		wrapper.setLastModified(inProposition.getLastModified());

		PropositionTypeVisitor visitor = new PropositionTypeVisitor();
		inProposition.accept(visitor);
		wrapper.setType(visitor.getType());

		return wrapper;
	}

	public static List<PropositionWrapper> wrapAll(
	        List<Proposition> inPropositions) {
		List<PropositionWrapper> wrappers = new ArrayList<PropositionWrapper>(
		        inPropositions.size());
		for (Proposition proposition : inPropositions) {
			wrappers.add(PropositionUtil.wrap(proposition, false));
		}
		return wrappers;
	}

	/**
	 * Wraps a proposition definition into a proposition wrapper. 
	 */
	public static PropositionWrapper wrap(PropositionDefinition inDefinition,
	        boolean summarize, Long inUserId,
	        SystemPropositionFinder inPropositionFinder) {
		PropositionWrapper wrapper = new PropositionWrapper();
		wrapper.setKey(inDefinition.getId());
		wrapper.setInSystem(true);
		wrapper.setAbbrevDisplayName(inDefinition.getAbbreviatedDisplayName());
		wrapper.setDisplayName(inDefinition.getDisplayName());
		wrapper.setSummarized(summarize);
		wrapper.setParent(inDefinition.getChildren().length > 0);

		if (!summarize) {
			List<PropositionWrapper> children = new ArrayList<PropositionWrapper>();
			for (String key : inDefinition.getChildren()) {
				children.add(wrap(inPropositionFinder.find(inUserId, key),
				        true, inUserId, inPropositionFinder));
			}
			wrapper.setChildren(children);
		}

		return wrapper;
	}

	public static Proposition unwrap(PropositionWrapper inWrapper,
	        PropositionDao inPropositionDao) {

		Proposition proposition;
		PropositionTypeVisitor visitor = new PropositionTypeVisitor();
		List<Proposition> targets = new ArrayList<Proposition>();

		if (inWrapper.getId() != null) {
			proposition = inPropositionDao.retrieve(inWrapper.getId());
		} else {
			switch (inWrapper.getType()) {
				case CATEGORIZATION:
					proposition = new Categorization();
					break;
				case SEQUENCE:
					proposition = new HighLevelAbstraction();
					break;
				default:
					throw new UnsupportedOperationException(
					        "Only categorization and sequence are currently supported");
			}
		}

		if (inWrapper.getChildren() != null) {
			for (PropositionWrapper child : inWrapper.getChildren()) {
				if (child.isInSystem()) {
					Proposition p = inPropositionDao.getByKey(child.getKey());
					if (p == null) {
						p = new SystemProposition();
						p.setKey(child.getKey());
						p.setInSystem(true);
					}
					targets.add(p);
				} else {
					targets.add(inPropositionDao.retrieve(child.getId()));
				}
			}
		}

		if (inWrapper.getType() == PropositionWrapper.Type.SEQUENCE) {
			((HighLevelAbstraction) proposition).setAbstractedFrom(targets);
		} else if (inWrapper.getType() == PropositionWrapper.Type.CATEGORIZATION) {
			((Categorization) proposition).setInverseIsA(targets);
		}

		proposition.setKey(inWrapper.getKey());
		proposition.setAbbrevDisplayName(inWrapper.getAbbrevDisplayName());
		proposition.setDisplayName(inWrapper.getDisplayName());
		proposition.setInSystem(inWrapper.isInSystem());

		if (inWrapper.getUserId() != null) {
			proposition.setUserId(inWrapper.getUserId());
		}
		return proposition;
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
		PropositionDefinitionPackagerVisitor visitor = new PropositionDefinitionPackagerVisitor();
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
	        List<Proposition> inPropositions) {
		List<PropositionDefinition> result = new ArrayList<PropositionDefinition>();

		for (Proposition p : inPropositions) {
			result.add(pack(p));
		}

		return result;
	}
}
