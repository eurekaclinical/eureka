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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

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

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition_;

/**
 * An implementation of the {@link PropositionDao} interface, backed by JPA
 * entities and queries.
 *
 * @author hrathod
 */
public class JpaPropositionDao extends GenericDao<Proposition, Long>
		implements PropositionDao {

	private static Logger LOGGER = LoggerFactory.getLogger(JpaPropositionDao
		.class);

	/**
	 * Create an object with the given entity manager provider.
	 *
	 * @param inProvider An entity manager provider.
	 */
	@Inject
	public JpaPropositionDao(Provider<EntityManager> inProvider) {
		super(Proposition.class, inProvider);
	}

	@Override
	public Proposition getByUserAndKey(Long inUserId, String inKey) {
		Proposition result;
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Proposition> criteriaQuery = builder.createQuery(
			Proposition
				.class);
		Root<Proposition> root = criteriaQuery.from(Proposition.class);
		Predicate userPredicate = builder.equal(
			root.get(
				Proposition_.userId), inUserId);
		Predicate keyPredicate = builder.equal(root.get(Proposition_.key),
			inKey);
		TypedQuery<Proposition> typedQuery = entityManager.createQuery(
			criteriaQuery.where(
				builder.and(userPredicate, keyPredicate)));
		try {
			result = typedQuery.getSingleResult();
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Result not unique for user id = {} and key = {}",
				inUserId, inKey);
			result = null;
		} catch (NoResultException nre) {
			LOGGER.warn("Result not existant for user id = {} and key = {}",
				inUserId, inKey);
			result = null;
		}
		return result;
	}

	@Override
	public List<Proposition> getByUserId(Long inId) {
		return this.getListByAttribute(Proposition_.userId, inId);
	}
}
