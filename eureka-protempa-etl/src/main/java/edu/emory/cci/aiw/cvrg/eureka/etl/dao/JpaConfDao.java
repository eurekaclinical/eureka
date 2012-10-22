/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration_;

/**
 * Implementation of the {@link ConfDao} interface, based on JPA.
 *
 * @author hrathod
 *
 */
public class JpaConfDao extends GenericDao<Configuration, Long> implements
		ConfDao {

	/**
	 * Creates an object using the given entity manager.
	 *
	 * @param provider A provider that can deliver new entity manager instances
	 * on-demand.
	 */
	@Inject
	public JpaConfDao(Provider<EntityManager> provider) {
		super(Configuration.class, provider);
	}

	@Override
	public Configuration getByUserId(Long userId) {
		return getUniqueByAttribute(Configuration_.userId, userId);
	}
}
