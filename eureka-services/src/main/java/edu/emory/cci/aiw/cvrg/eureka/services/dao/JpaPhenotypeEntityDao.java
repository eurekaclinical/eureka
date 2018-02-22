/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

//import edu.emory.cci.aiw.cvrg.eureka.common.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity_;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;

import org.eurekaclinical.standardapis.dao.GenericDao;

/**
 * An implementation of the {@link PhenotypeEntityDao} interface, backed by
 * JPA entities and queries.
 *
 * @author hrathod
 */
public class JpaPhenotypeEntityDao extends GenericDao<PhenotypeEntity, Long>
		implements PhenotypeEntityDao {

	private static final Logger LOGGER
			= LoggerFactory.getLogger(JpaPhenotypeEntityDao.class);

	/**
	 * Create an object with the given entity manager provider.
	 *
	 * @param inProvider An entity manager provider.
	 */
	@Inject
	public JpaPhenotypeEntityDao(Provider<EntityManager> inProvider) {
		super(PhenotypeEntity.class, inProvider);
	}

	@Override
	public PhenotypeEntity getByUserAndKey(Long inUserId, String inKey) {
		return getByUserAndKey(inUserId, inKey, true);
	}

	@Override
	public PhenotypeEntity getUserOrSystemByUserAndKey(Long inUserId, String inKey) {
		return getByUserAndKey(inUserId, inKey, false);
	}

	@Override
	public List<PhenotypeEntity> getByUserId(Long inUserId) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PhenotypeEntity> criteriaQuery = builder.createQuery(PhenotypeEntity.class);
		Root<PhenotypeEntity> root = criteriaQuery.from(PhenotypeEntity.class);
		Predicate userPredicate = builder.equal(
				root.get(
						PhenotypeEntity_.userId), inUserId);
		Predicate notInSystemPredicate = builder.equal(root.get(PhenotypeEntity_.inSystem), false);
		TypedQuery<PhenotypeEntity> typedQuery = entityManager.createQuery(
				criteriaQuery.where(
						builder.and(userPredicate, notInSystemPredicate)));
		return typedQuery.getResultList();
	}

	@Override
	public PhenotypeEntity getById(Long inId) {
		PhenotypeEntity result = null;
            
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PhenotypeEntity> criteriaQuery = builder.createQuery(PhenotypeEntity.class);
		Root<PhenotypeEntity> root = criteriaQuery.from(PhenotypeEntity.class);
                
		Predicate idPredicate = builder.equal(
				root.get(
						PhenotypeEntity_.id), inId);
		
		TypedQuery<PhenotypeEntity> typedQuery = entityManager.createQuery(
				criteriaQuery.where(
						builder.and(idPredicate)));
		result = typedQuery.getSingleResult();
		return result;
	}        
        
	private PhenotypeEntity getByUserAndKey(Long inUserId, String inKey, boolean excludeSystemElements) {
		PhenotypeEntity result = null;
                
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PhenotypeEntity> criteriaQuery = builder.createQuery(PhenotypeEntity.class);
		Root<PhenotypeEntity> root = criteriaQuery.from(PhenotypeEntity.class);
		
		List<Predicate> predicates = new ArrayList<>(3);
		Predicate userPredicate = builder.equal(
				root.get(
						PhenotypeEntity_.userId), inUserId);
		predicates.add(userPredicate);
		Predicate keyPredicate = builder.equal(root.get(PhenotypeEntity_.key),
				inKey);
		predicates.add(keyPredicate);
		if (excludeSystemElements) {
			Predicate notInSystemPredicate = builder.equal(root.get(PhenotypeEntity_.inSystem),
					false);
			predicates.add(notInSystemPredicate);
		}
		
		TypedQuery<PhenotypeEntity> typedQuery = entityManager.createQuery(
					criteriaQuery.where(
							builder.and(predicates.toArray(
									new Predicate[predicates.size()]))));

		try {
			result = typedQuery.getSingleResult();
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Result not unique for user id = {} and key = {}",
					inUserId, inKey);
		} catch (NoResultException nre) {
		}

		return result;
	}
}
