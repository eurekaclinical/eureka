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

import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

public final class SystemPropositionTranslator implements
        PropositionTranslator<SystemElement, SystemProposition> {

	private final SystemPropositionFinder finder;

	public SystemPropositionTranslator(SystemPropositionFinder inFinder) {
		finder = inFinder;
	}

	@Override
	public SystemProposition translateFromElement(SystemElement element) {
		SystemProposition proposition = new SystemProposition();
		PropositionTranslatorUtil.populateCommonPropositionFields(proposition,
		        element);
		proposition.setSystemType(element.getSystemType());
		return proposition;
	}

	@Override
	public SystemElement translateFromProposition(SystemProposition proposition) {
		SystemElement element = new SystemElement();
		PropositionTranslatorUtil.populateCommonDataElementFields(element,
		        proposition);
		PropositionDefinition propDef = finder.find(proposition.getUserId(),
		        proposition.getKey());
		List<SystemElement> children = new ArrayList<SystemElement>();
		for (String child : propDef.getInverseIsA()) {
			PropositionDefinition childDef = finder.find(
			        proposition.getUserId(), child);
			SystemElement childElement = new SystemElement();
			childElement.setKey(child);
			childElement.setInSystem(true);
			childElement.setDisplayName(childDef.getDisplayName());
			childElement.setAbbrevDisplayName(childDef
			        .getAbbreviatedDisplayName());
			childElement.setSummarized(true);
			childElement.setUserId(proposition.getUserId());
			childElement.setCreated(proposition.getCreated());
			childElement.setLastModified(proposition.getLastModified());

			children.add(childElement);
		}
		element.setChildren(children);

		List<String> properties = new ArrayList<String>();
		for (PropertyDefinition property : propDef.getPropertyDefinitions()) {
			properties.add(property.getName());
		}
		element.setInSystem(proposition.isInSystem());
		element.setProperties(properties);
		element.setSystemType(proposition.getSystemType());

		return element;
	}

}
