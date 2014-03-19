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

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity_;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author hrathod
 */
public class JpaUserDao extends GenericDao<UserEntity, Long> implements UserDao {

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 *                     the data store.
	 */
	@Inject
	public JpaUserDao(Provider<EntityManager> inEMProvider) {
		super(UserEntity.class, inEMProvider);
	}

	@Override
	public UserEntity getByName(String name) {
		return this.getUniqueByAttribute(UserEntity_.username, name);
	}
	
}
