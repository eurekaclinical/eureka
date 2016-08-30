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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.eurekaclinical.eureka.client.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventEntity_;
import org.eurekaclinical.standardapis.dao.GenericDao;

/**
 * Implements the {@link JobDao} interface, with the use of JPA entity managers.
 *
 * @author gmilton
 * @author hrathod
 *
 */
public class JpaJobDao extends GenericDao<JobEntity, Long> implements JobDao {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaJobDao.class);

	/**
	 * Construct instance with the given EntityManager provider.
	 *
	 * @param inEMProvider The entity manager provider.
	 */
	@Inject
	public JpaJobDao(final Provider<EntityManager> inEMProvider) {
		super(JobEntity.class, inEMProvider);
	}

	@Override
	public List<JobEntity> getWithFilterDesc(JobFilter jobFilter) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JobEntity> query = builder.createQuery(JobEntity.class);
		Root<JobEntity> root = query.from(JobEntity.class);
		Predicate[] predicatesArray = buildWhere(jobFilter, builder, root, null);
		query.where(predicatesArray);
		query.orderBy(builder.desc(root.get(JobEntity_.created)));
		LOGGER.debug("Creating typed query.");
		TypedQuery<JobEntity> typedQuery = entityManager.createQuery(query);
		LOGGER.debug("Returning results.");
		return typedQuery.getResultList();
	}

	@Override
	public List<JobEntity> getLatestWithFilter(JobFilter jobFilter) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JobEntity> query = builder.createQuery(JobEntity.class);
		Root<JobEntity> root = query.from(JobEntity.class);
		Subquery<Date> subQuery = query.subquery(Date.class);
		Root<JobEntity> subQueryRoot = subQuery.from(JobEntity.class);
		subQuery.select(builder.greatest(subQueryRoot.get(JobEntity_.created)));
		Predicate[] predicatesArray = buildWhere(jobFilter, builder, root,subQuery);
		query.where(predicatesArray);
		LOGGER.debug("Creating typed query.");
		TypedQuery<JobEntity> typedQuery = entityManager.createQuery(query);
		LOGGER.debug("Returning results.");
		return typedQuery.getResultList();

	}

	@Override
	public List<JobEntity> getWithFilter(final JobFilter jobFilter) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JobEntity> query = builder.createQuery(JobEntity.class);
		Root<JobEntity> root = query.from(JobEntity.class);
		Predicate[] predicatesArray = buildWhere(jobFilter, builder, root, null);
		query.where(predicatesArray);
		LOGGER.debug("Creating typed query.");
		TypedQuery<JobEntity> typedQuery = entityManager.createQuery(query);
		LOGGER.debug("Returning results.");
		return typedQuery.getResultList();
	}


	private Predicate[] buildWhere(JobFilter jobFilter, CriteriaBuilder builder, Root<JobEntity> root, Subquery<Date> subQuery) {
		List<Predicate> predicates = new ArrayList<>();
		if (jobFilter != null) {
			LOGGER.debug("Checking for job ID.");
			if (jobFilter.getJobId() != null) {
				LOGGER.debug("Found job ID: {}", jobFilter.getJobId());
				predicates.add(builder.equal(root.get(JobEntity_.id),
						jobFilter.getJobId()));
			}
			LOGGER.debug("Checking for user ID.");
			if (jobFilter.getUserId() != null) {
				LOGGER.debug("Found user ID: {}", jobFilter.getUserId());
				Path<Long> userId = root.get(JobEntity_.user).get(AuthorizedUserEntity_.id);
				predicates.add(builder.equal(userId, jobFilter.getUserId()));
				if (subQuery != null) {
					subQuery.where(builder.equal(userId, jobFilter.getUserId()));
				}
			}
			LOGGER.debug("Checking for start time.");
			if (jobFilter.getFrom() != null) {
				LOGGER.debug("Found start time: {}", jobFilter.getFrom());
				predicates.add(builder.greaterThanOrEqualTo(
						root.<Date>get(JobEntity_.created), jobFilter.getFrom()));
			}
			LOGGER.debug("Checking for end time.");
			if (jobFilter.getTo() != null) {
				LOGGER.debug("Found end time: {}", jobFilter.getTo());
				predicates.add(builder.lessThanOrEqualTo(
						root.<Date>get(JobEntity_.created), jobFilter.getTo()));
			}
			LOGGER.debug("Checking for state.");
			if (jobFilter.getState() != null) {
				LOGGER.debug("Found state: {}", jobFilter.getState());
				predicates.add(builder.equal(
						root.join(JobEntity_.jobEvents).get(JobEventEntity_.status),
						jobFilter.getState()));
			}
			LOGGER.debug("Checking for recent.");
			if (jobFilter.getLatest()!=null && jobFilter.getLatest()) {
				LOGGER.debug("Get latest job");
				if(subQuery!=null) {
					predicates.add(builder.equal(root.<Date>get(JobEntity_.created), subQuery));
				}else{
					LOGGER.debug("Subquery is null");

				}
			}
		}
		LOGGER.debug("{} predicates found from filter", predicates.size());
		Predicate[] predicatesArray = new Predicate[predicates.size()];
		predicates.toArray(predicatesArray);
		return predicatesArray;
	}
}
