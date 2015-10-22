package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

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

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ConfigEntity;

/**
 *
 * @author Andrew Post
 */
public interface ConfigDao<E extends ConfigEntity> extends Dao<E, Long>{
	/**
	 * Gets a configuration by name.
	 * @param name the configuration, or <code>null</code> if there is not one
	 * with that name.
	 * @return 
	 */
	E getByName(String name);
}
