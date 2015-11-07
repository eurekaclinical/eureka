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

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DestinationEntity> criteriaQuery = builder.createQuery(DestinationEntity.class);
		Root<DestinationEntity> root = criteriaQuery.from(DestinationEntity.class);
		criteriaQuery.where(builder.and(
						builder.equal(root.get(DestinationEntity_.name), name),
						builder.or(
							builder.isNull(root.get(DestinationEntity_.expiredAt)),
							builder.greaterThanOrEqualTo(root.get(DestinationEntity_.expiredAt), new Date()))
				));
		try {
			return entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public List<DestinationEntity> getAll() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DestinationEntity> criteriaQuery = builder.createQuery(DestinationEntity.class);
		Root<DestinationEntity> root = criteriaQuery.from(DestinationEntity.class);
		criteriaQuery.where(builder.or(
							builder.isNull(root.get(DestinationEntity_.expiredAt)),
							builder.greaterThanOrEqualTo(root.get(DestinationEntity_.expiredAt), new Date())));
		TypedQuery<DestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	@Override
	public List<CohortDestinationEntity> getAllCohortDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CohortDestinationEntity> criteriaQuery = builder.createQuery(CohortDestinationEntity.class);
		Root<CohortDestinationEntity> root = criteriaQuery.from(CohortDestinationEntity.class);
		criteriaQuery.where(builder.or(
							builder.isNull(root.get(DestinationEntity_.expiredAt)),
							builder.greaterThanOrEqualTo(root.get(DestinationEntity_.expiredAt), new Date())));
		TypedQuery<CohortDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<I2B2DestinationEntity> getAllI2B2Destinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<I2B2DestinationEntity> criteriaQuery = builder.createQuery(I2B2DestinationEntity.class);
		Root<I2B2DestinationEntity> root = criteriaQuery.from(I2B2DestinationEntity.class);
		criteriaQuery.where(builder.or(
							builder.isNull(root.get(DestinationEntity_.expiredAt)),
							builder.greaterThanOrEqualTo(root.get(DestinationEntity_.expiredAt), new Date())));
		TypedQuery<I2B2DestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<PatientSetExtractorDestinationEntity> getAllPatientSetSenderDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PatientSetExtractorDestinationEntity> criteriaQuery = builder.createQuery(PatientSetExtractorDestinationEntity.class);
		Root<PatientSetExtractorDestinationEntity> root = criteriaQuery.from(PatientSetExtractorDestinationEntity.class);
		criteriaQuery.where(builder.or(
							builder.isNull(root.get(DestinationEntity_.expiredAt)),
							builder.greaterThanOrEqualTo(root.get(DestinationEntity_.expiredAt), new Date())));
		TypedQuery<PatientSetExtractorDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	
}
