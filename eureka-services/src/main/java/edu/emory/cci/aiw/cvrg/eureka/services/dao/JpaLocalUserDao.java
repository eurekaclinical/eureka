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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity_;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

/**
 * An implementation of the {@link UserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author hrathod
 */
public class JpaLocalUserDao extends GenericDao<LocalUserEntity, Long> implements LocalUserDao {

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 *                     the data store.
	 */
	@Inject
	public JpaLocalUserDao(Provider<EntityManager> inEMProvider) {
		super(LocalUserEntity.class, inEMProvider);
	}
	
	@Override
	public LocalUserEntity getByName(String name) {
		CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<LocalUserEntity> criteriaQuery = builder.createQuery(LocalUserEntity.class);
		Root<LocalUserEntity> root = criteriaQuery.from(LocalUserEntity.class);
		
		Subquery<UserEntity> subquery = 
				criteriaQuery.subquery(UserEntity.class);
		Root<UserEntity> userEntity = subquery.from(UserEntity.class);
		Path<String> username = userEntity.get(UserEntity_.username);
		subquery.select(userEntity);
		subquery = subquery.where(builder.equal(username, name));
		
		TypedQuery<LocalUserEntity> query = 
				this.getEntityManager().createQuery(criteriaQuery.where(
				builder.equal(root, subquery)));
		return query.getSingleResult();
	}

	@Override
	public LocalUserEntity getByVerificationCode(String inCode) {
		return this.getUniqueByAttribute(LocalUserEntity_.verificationCode, inCode);
	}
	
}
