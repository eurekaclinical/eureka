/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.protempa.PropositionDefinition;

/**
 *
 * @author Andrew Post
 */
public class PropositionDefinitionCollector {

	public static PropositionDefinitionCollector getInstance(
			PropositionDefinitionConverterVisitor converterVisitor,
			List<DataElementEntity> dataElements) {
		List<PropositionDefinition> userProps
				= new ArrayList<>();
		for (DataElementEntity de : dataElements) {
			de.accept(converterVisitor);
			Collection<PropositionDefinition> propDefs
					= converterVisitor.getPropositionDefinitions();
			userProps.addAll(propDefs);
		}
		PropositionDefinitionCollector result
				= new PropositionDefinitionCollector();
		result.userPropDefs = userProps;
		
		List<String> toShow = new ArrayList<>();
		for (PropositionDefinition userProp : userProps) {
			toShow.add(userProp.getId());
		}
		result.toShowPropDefs = toShow;
		return result;
	}

	private List<PropositionDefinition> userPropDefs;
	private List<String> toShowPropDefs;

	private PropositionDefinitionCollector() {

	}

	public List<PropositionDefinition> getUserPropDefs() {
		return userPropDefs;
	}

	public List<String> getToShowPropDefs() {
		return toShowPropDefs;
	}

}
