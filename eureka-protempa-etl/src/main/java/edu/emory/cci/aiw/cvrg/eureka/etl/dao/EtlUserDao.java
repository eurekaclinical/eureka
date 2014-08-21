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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * A data access object interface for working with {@link UserEntity} objects in the
 * data store.
 *
 * @author hrathod
 *
 */
public interface EtlUserDao extends Dao<EtlUserEntity, Long> {
	
	EtlUserEntity getByAttributePrincipal(AttributePrincipal principal);

	/**
	 * Get a user object, given the user name.
	 *
	 * @param name The name of the user to retrieve.
	 * @return The user object that corresponds to the given user name.
	 */
	EtlUserEntity getByUsername(String username);
}
