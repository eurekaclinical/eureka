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
package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.util.ArrayList;
import java.util.List;

import org.arp.javautil.arrays.Arrays;
import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;

import org.eurekaclinical.eureka.client.comm.SystemPhenotype;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.PhenotypeEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFindException;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

/**
 * Provides common utility functions operating on {@link PhenotypeEntity}s.
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
	public static SystemPhenotype toSystemPhenotype(
			String sourceConfigId,
	        PropositionDefinition inDefinition, boolean summarize,
	        SystemPropositionFinder inPropositionFinder)
	        throws PropositionFindException {
		if (inDefinition == null) {
			throw new IllegalArgumentException("inDefinition cannot be null");
		}
		SystemPhenotype systemPhenotype = new SystemPhenotype();
		systemPhenotype.setKey(inDefinition.getId());
		systemPhenotype.setInSystem(true);
		systemPhenotype.setInternalNode(inDefinition.getChildren().length > 0);
		systemPhenotype.setDescription(inDefinition.getAbbreviatedDisplayName());
		systemPhenotype.setDisplayName(inDefinition.getDisplayName());
		systemPhenotype.setSummarized(summarize);
		PropositionDefinitionTypeVisitor propDefTypeVisitor = new PropositionDefinitionTypeVisitor();
		inDefinition.accept(propDefTypeVisitor);
		systemPhenotype.setSystemType(propDefTypeVisitor.getSystemType());
		String[] inDefChildren = inDefinition.getChildren();
		systemPhenotype.setParent(inDefChildren.length > 0);

		List<String> properties = new ArrayList<>();
		for (PropertyDefinition propertyDef : inDefinition
				.getPropertyDefinitions()) {
			properties.add(propertyDef.getId());
		}
		systemPhenotype.setProperties(properties);

		if (!summarize) {
			List<SystemPhenotype> children = new ArrayList<>();
			List<PropositionDefinition> pds = inPropositionFinder.findAll(
					sourceConfigId,
			        Arrays.<String> asList(inDefChildren),
			        Boolean.FALSE);
			for (PropositionDefinition pd : pds) {
				children.add(toSystemPhenotype(sourceConfigId, pd, true,
				        inPropositionFinder));
			}
			systemPhenotype.setChildren(children);

		}

		return systemPhenotype;
	}
}
