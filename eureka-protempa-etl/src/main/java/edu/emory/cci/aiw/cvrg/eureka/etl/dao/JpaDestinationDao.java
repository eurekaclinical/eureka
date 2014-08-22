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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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
		return getUniqueByAttribute(DestinationEntity_.name, name);
	}
	
	@Override
	public List<CohortDestinationEntity> getAllCohortDestinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<CohortDestinationEntity> criteriaQuery = builder.createQuery(CohortDestinationEntity.class);
		criteriaQuery.from(CohortDestinationEntity.class);
		TypedQuery<CohortDestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	@Override
	public List<I2B2DestinationEntity> getAllI2B2Destinations() {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<I2B2DestinationEntity> criteriaQuery = builder.createQuery(I2B2DestinationEntity.class);
		criteriaQuery.from(I2B2DestinationEntity.class);
		TypedQuery<I2B2DestinationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
}
