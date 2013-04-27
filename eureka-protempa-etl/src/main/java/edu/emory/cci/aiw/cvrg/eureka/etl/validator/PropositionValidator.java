/*
 * #%L
 * Eureka Protempa ETL
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.validator;

import java.util.List;

import org.protempa.PropositionDefinition;


/**
 *
 * @author hrathod
 */
public interface PropositionValidator {

	void setConfigId(String inConfigId);

	void setTargetProposition(PropositionDefinition inProposition);

	void setPropositions(List<PropositionDefinition> inPropositions);
	
	void setUserPropositions(List<PropositionDefinition> inUserPropositions);

	boolean validate() throws PropositionValidatorException;

	List<String> getMessages();

}
