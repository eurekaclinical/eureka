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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;

/**
 * A data access object interface for working with {@link DataElementEntity} objects
 * in the data store.
 *
 * @author hrathod
 */
public interface DataElementEntityDao extends Dao<DataElementEntity, Long> {
	/**
	 * Gets a proposition based on the "key" attribute.
	 * @param inUserId The userId to search for in the database.
	 * @param inKey The key to be searched in the database.
	 * @return A proposition if found, null otherwise.
	 */
	public DataElementEntity getByUserAndKey(Long inUserId, String inKey);

	/**
	 * Gets a list of propositions for the given user ID.
	 * @param inId the unique identifier for the given user.
	 * @return A list of propositions belonging to the given user.
	 */
	public List<DataElementEntity> getByUserId(Long inId);
}
