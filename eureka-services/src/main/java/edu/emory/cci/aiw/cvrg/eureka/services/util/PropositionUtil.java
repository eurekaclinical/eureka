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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides common utility functions operating on {@link DataElementEntity}s.
 */
public final class PropositionUtil {

	private PropositionUtil() {
		// do not allow instantiation.
	}

	public static SystemProposition toSystemProposition(
	        PropositionDefinition inDefinition, Long inUserId) {
		if (inDefinition == null) {
			throw new IllegalArgumentException("inDefinition cannot be null");
		}
		SystemProposition sysProp = new SystemProposition();
		sysProp.setKey(inDefinition.getId());
		sysProp.setInSystem(true);
		sysProp.setDisplayName(inDefinition.getDisplayName());
		sysProp.setDescription(inDefinition.getAbbreviatedDisplayName());
		sysProp.setUserId(inUserId);
		PropositionDefinitionTypeVisitor propDefTypeVisitor = new PropositionDefinitionTypeVisitor();
		inDefinition.accept(propDefTypeVisitor);
		sysProp.setSystemType(propDefTypeVisitor.getSystemType());
		return sysProp;
	}

	/**
	 * Wraps a proposition definition into a proposition wrapper.
	 */
	public static SystemElement toSystemElement(
	        PropositionDefinition inDefinition, boolean summarize,
	        Long inUserId, SystemPropositionFinder inPropositionFinder)
	        throws PropositionFindException {
		if (inDefinition == null) {
			throw new IllegalArgumentException("inDefinition cannot be null");
		}
		SystemElement systemElement = new SystemElement();
		systemElement.setKey(inDefinition.getId());
		systemElement.setInSystem(true);
		systemElement.setDescription(inDefinition
		        .getAbbreviatedDisplayName());
		systemElement.setDisplayName(inDefinition.getDisplayName());
		systemElement.setSummarized(summarize);
		systemElement.setParent(inDefinition.getChildren().length > 0);
		PropositionDefinitionTypeVisitor propDefTypeVisitor = new PropositionDefinitionTypeVisitor();
		inDefinition.accept(propDefTypeVisitor);
		systemElement.setSystemType(propDefTypeVisitor.getSystemType());

		if (!summarize) {
			List<SystemElement> children = new ArrayList<SystemElement>();
			for (String key : inDefinition.getChildren()) {
				PropositionDefinition pd = inPropositionFinder.find(inUserId,
				        key);
				if (pd != null) {
					children.add(toSystemElement(pd, true, inUserId,
					        inPropositionFinder));
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
}
