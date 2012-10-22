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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

/**
 * A data access object interface for working with {@link Proposition} objects
 * in the data store.
 *
 * @author hrathod
 */
public interface PropositionDao extends Dao<Proposition, Long> {
	/**
	 * Gets a proposition based on the "key" attribute.
	 * @param inKey The key to be searched in the database.
	 * @return A proposition if found, null otherwise.
	 */
	public Proposition getByKey(String inKey);

	/**
	 * Gets a list of propositions for the given user ID.
	 * @param inId the unique identifier for the given user.
	 * @return A list of propositions belonging to the given user.
	 */
	public List<Proposition> getByUserId(Long inId);
}
