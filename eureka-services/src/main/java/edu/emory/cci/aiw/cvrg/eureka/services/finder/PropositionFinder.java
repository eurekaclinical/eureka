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
 * Finds proposition definitions.
 *
 * @param <K> the type of the proposition key to use in look-ups
 */
public interface PropositionFinder<K> {
	/**
	 * Finds the proposition definition for the given user and key.
	 *
	 * @param inUserId user ID to use for the look-up
	 * @param inKey    key to use for the look-up
	 * @return a {@link PropositionDefinition} matching the user ID and key,
	 *         if any
	 * @throws PropositionFindException if an error occurs while performing
	 *                                  the operation
	 */
	public PropositionDefinition find(String sourceConfigId, 
									  K inKey) throws PropositionFindException;

	/**
	 * Performs any clean-up operations for the finder.
	 */
	public void shutdown();
}
