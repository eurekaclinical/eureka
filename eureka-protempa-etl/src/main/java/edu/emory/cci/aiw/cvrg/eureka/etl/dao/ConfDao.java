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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

/**
 * A data access object interface used to retrieve and store information about
 * backend configurations.
 *
 * @author hrathod
 *
 */
public interface ConfDao extends Dao<Configuration, Long> {

	/**
	 * Gets a configuration that belongs to the give user (denoted by the unique
	 * identifier).
	 *
	 * @param userId Unique identifier of the user to whom the configuration
	 * belongs.
	 * @return A configuration belonging to the give user.
	 */
	public Configuration getByUserId(Long userId);
}
