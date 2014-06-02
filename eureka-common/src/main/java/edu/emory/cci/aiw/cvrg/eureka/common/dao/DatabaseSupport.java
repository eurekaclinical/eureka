package edu.emory.cci.aiw.cvrg.eureka.common.dao;

/*
 * #%L
 * Eureka Common
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

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public final class DatabaseSupport {
	private static Logger LOGGER =
			LoggerFactory.getLogger(DatabaseSupport.class);
	
	private final EntityManager entityManager;
	
	public DatabaseSupport(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public <T> List<T> getAll(Class<T> entity) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = 
				builder.createQuery(entity);
		criteriaQuery.from(entity);
		TypedQuery<T> typedQuery = 
				entityManager.createQuery(criteriaQuery);
		List<T> jobs = typedQuery.getResultList();
		return jobs;
	}
	
	public <T, Y> T getUniqueByAttribute(Class<T> entityCls, 
			SingularAttribute<T, Y> attribute, Y value) {
		TypedQuery<T> query = createTypedQuery(entityCls, attribute, value);
		T result = null;
		try {
			result = query.getSingleResult();
		} catch (NonUniqueResultException nure) {
			LOGGER.warn("Result not unique for {} = {}", attribute, value);
		} catch (NoResultException nre) {
			LOGGER.debug("Result not existant for {} = {}", attribute, value);
		}
		return result;
	}
	
	public <T, Y> List<T> getListByAttribute(
			Class<T> entityCls, SingularAttribute<T,Y> attribute, Y value) {
		TypedQuery<T> query = createTypedQuery(entityCls, attribute, value);
		return query.getResultList();
	}
	
	/**
	 * Creates a typed query based on the attribute given and the target value
	 * for that attribute.
	 * @param attribute The attribute to compare.
	 * @param value The target value for the given attribute.
	 * @param <Y> The type of the target attribute and target value.
	 * @return A typed query that contains the given criteria.
	 */
	private <T, Y> TypedQuery<T> createTypedQuery(Class<T> entityCls, 
			SingularAttribute<T, Y> attribute, Y value) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(entityCls);
		Root<T> root = criteriaQuery.from(entityCls);
		Path<Y> path = root.get(attribute);
		return entityManager.createQuery(criteriaQuery.where(
				builder.equal(path, value)));
	}
}
