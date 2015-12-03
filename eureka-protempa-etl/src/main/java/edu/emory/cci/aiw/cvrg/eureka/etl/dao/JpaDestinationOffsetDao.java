/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DeidPerPatientParams_;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

/**
 * Implements the {@link JobDao} interface, with the use of JPA entity managers.
 *
 * @author Andrew Post
 *
 */
public class JpaDestinationOffsetDao extends GenericDao<DeidPerPatientParams, Long> implements DeidPerPatientParamDao {

	/**
	 * Construct instance with the given EntityManager provider.
	 *
	 * @param inEMProvider The entity manager provider.
	 */
	@Inject
	public JpaDestinationOffsetDao(final Provider<EntityManager> inEMProvider) {
		super(DeidPerPatientParams.class, inEMProvider);
	}

	@Override
	public void deleteAll(DestinationEntity destination) {
		// No bulk delete in JPA 2.0 for criteria queries, so we have to use
		// a regular query. JPA 2.1 does support bulk delete.
		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createQuery("DELETE FROM DestinationOffset do WHERE do.destination = :d");
		query.setParameter("d", destination);
		query.executeUpdate();
	}

	@Override
	public DeidPerPatientParams getByKeyId(String keyId) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeidPerPatientParams> criteriaQuery = builder.createQuery(DeidPerPatientParams.class);
		Root<DeidPerPatientParams> root = criteriaQuery.from(DeidPerPatientParams.class);
		Path<String> usernamePath = root.get(DeidPerPatientParams_.keyId);
		TypedQuery<DeidPerPatientParams> query = entityManager.createQuery(criteriaQuery.where(
				builder.equal(usernamePath, keyId)));
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
