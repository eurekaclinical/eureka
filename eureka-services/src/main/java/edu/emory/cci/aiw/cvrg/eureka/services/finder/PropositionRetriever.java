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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import org.protempa.PropositionDefinition;

/**
 * Interface for accessing sources of proposition definitions.
 * 
 * @author hrathod
 * @param <U> user id objects.
 * @param <K> proposition id objects.
 */
public interface PropositionRetriever<U, K> {
	/**
	 * Retrieves a proposition definition with the specified id and for the
	 * specified user.
	 * 
	 * @param inUserId the user's id.
	 * @param inKey the proposition id of interest.
	 * @return the proposition definition of interest, or <code>null</code> if 
	 * not found.
	 */
	PropositionDefinition retrieve (U inUserId, K inKey)
			throws PropositionFindException;
}
