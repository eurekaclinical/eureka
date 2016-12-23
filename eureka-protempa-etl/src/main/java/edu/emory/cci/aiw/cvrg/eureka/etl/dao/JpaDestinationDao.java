package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

/*
 * #%L
 * Eureka Protempa ETL
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
import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TabularFileDestinationEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.eurekaclinical.standardapis.dao.GenericDao;

/**
 *
 * @author Andrew Post
 */
public class JpaDestinationDao extends GenericDao<DestinationEntity, Long> implements DestinationDao {

	@Inject
	public JpaDestinationDao(Provider<EntityManager> inManagerProvider) {
		super(DestinationEntity.class, inManagerProvider);
	}

	@Override
	public DestinationEntity getByName(String name) {
		DestinationEntity result;
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DestinationEntity> criteriaQuery = builder.createQuery(DestinationEntity.class);
		Root<DestinationEntity> root = criteriaQuery.from(DestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.and(
				builder.equal(root.get(DestinationEntity_.name), name),
				builder.or(
						builder.isNull(root.get(DestinationEntity_.expiredAt)),
						builder.greaterThanOrEqualTo(expiredAt, new Date()))
		));
		try {
			result = entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException ex) {
			result = null;
		}
		return result;
	}

	@Override
	public List<DestinationEntity> getAll() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DestinationEntity> criteriaQuery = builder.createQuery(DestinationEntity.class);
		Root<DestinationEntity> root = criteriaQuery.from(DestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<DestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<CohortDestinationEntity> getAllCohortDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CohortDestinationEntity> criteriaQuery = builder.createQuery(CohortDestinationEntity.class);
		Root<CohortDestinationEntity> root = criteriaQuery.from(CohortDestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<CohortDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<I2B2DestinationEntity> getAllI2B2Destinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<I2B2DestinationEntity> criteriaQuery = builder.createQuery(I2B2DestinationEntity.class);
		Root<I2B2DestinationEntity> root = criteriaQuery.from(I2B2DestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<I2B2DestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<PatientSetExtractorDestinationEntity> getAllPatientSetExtractorDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PatientSetExtractorDestinationEntity> criteriaQuery = builder.createQuery(PatientSetExtractorDestinationEntity.class);
		Root<PatientSetExtractorDestinationEntity> root = criteriaQuery.from(PatientSetExtractorDestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<PatientSetExtractorDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	@Override
	public List<PatientSetSenderDestinationEntity> getAllPatientSetSenderDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PatientSetSenderDestinationEntity> criteriaQuery = builder.createQuery(PatientSetSenderDestinationEntity.class);
		Root<PatientSetSenderDestinationEntity> root = criteriaQuery.from(PatientSetSenderDestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<PatientSetSenderDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	@Override
	public List<TabularFileDestinationEntity> getAllTabularFileDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<TabularFileDestinationEntity> criteriaQuery = builder.createQuery(TabularFileDestinationEntity.class);
		Root<TabularFileDestinationEntity> root = criteriaQuery.from(TabularFileDestinationEntity.class);
		Path<Date> expiredAt = root.get(DestinationEntity_.expiredAt);
		criteriaQuery.where(builder.or(
				builder.isNull(root.get(DestinationEntity_.expiredAt)),
				builder.greaterThanOrEqualTo(expiredAt, new Date())));
		TypedQuery<TabularFileDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

}
