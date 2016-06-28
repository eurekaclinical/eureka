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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity_;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigGroupMembership_;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import org.eurekaclinical.standardapis.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class JpaEtlGroupDao extends GenericDao<EtlGroup, Long> implements EtlGroupDao {
	
	private static Logger LOGGER =
			LoggerFactory.getLogger(JpaEtlGroupDao.class);

	@Inject
	public JpaEtlGroupDao(Provider<EntityManager> inManagerProvider) {
		super(EtlGroup.class, inManagerProvider);
	}

	@Override
	public EtlGroup getByName(String name) {
		return getUniqueByAttribute(EtlGroup_.name, name);
	}
	
	@Override
	public ResolvedPermissions resolveSourceConfigPermissions(
			AuthorizedUserEntity etlUser, SourceConfigEntity entity) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> q = 
				cb.createQuery(Tuple.class);
		Root<SourceConfigGroupMembership> groupMembership = q.from(SourceConfigGroupMembership.class);
		q.select(
				cb.tuple(
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(SourceConfigGroupMembership_.groupRead), true), 1).otherwise(0).as(Integer.class)),
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(SourceConfigGroupMembership_.groupWrite), true), 1).otherwise(0).as(Integer.class)),
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(SourceConfigGroupMembership_.groupExecute), true), 1).otherwise(0).as(Integer.class))
				));
		ListJoin<EtlGroup, AuthorizedUserEntity> join = groupMembership.join(SourceConfigGroupMembership_.group).join(EtlGroup_.users);
		q.where(
				cb.and(
						cb.equal(groupMembership.get(SourceConfigGroupMembership_.sourceConfig).get(SourceConfigEntity_.id), entity.getId()), 
						cb.equal(join.get(AuthorizedUserEntity_.id), etlUser.getId())
				)
		);
		q.groupBy(groupMembership.get(SourceConfigGroupMembership_.sourceConfig).get(SourceConfigEntity_.id));
		List<Tuple> resultList = entityManager.createQuery(q).getResultList();
		if (resultList.isEmpty()) {
			return new ResolvedPermissions(false, false, false);
		} else {
			Tuple result = resultList.get(0);
			return new ResolvedPermissions(result.get(0, Integer.class) == 1, result.get(1, Integer.class) == 1, result.get(2, Integer.class) == 1);
		}
	}
	
	@Override
	public ResolvedPermissions resolveDestinationPermissions(
			AuthorizedUserEntity etlUser, DestinationEntity entity) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> q = 
				cb.createQuery(Tuple.class);
		Root<DestinationGroupMembership> groupMembership = q.from(DestinationGroupMembership.class);
		q.select(
				cb.tuple(
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(DestinationGroupMembership_.groupRead), true), 1).otherwise(0).as(Integer.class)),
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(DestinationGroupMembership_.groupWrite), true), 1).otherwise(0).as(Integer.class)),
						cb.greatest(cb.selectCase().when(cb.equal(groupMembership.get(DestinationGroupMembership_.groupExecute), true), 1).otherwise(0).as(Integer.class))
				));
		ListJoin<EtlGroup, AuthorizedUserEntity> join = groupMembership.join(DestinationGroupMembership_.group).join(EtlGroup_.users);
		q.where(
				cb.and(
						cb.equal(groupMembership.get(DestinationGroupMembership_.destination).get(DestinationEntity_.id), entity.getId()), 
						cb.equal(join.get(AuthorizedUserEntity_.id), etlUser.getId())
						)
		);
		q.groupBy(groupMembership.get(DestinationGroupMembership_.destination).get(DestinationEntity_.id));
		TypedQuery<Tuple> typedQuery = entityManager.createQuery(q);
		List<Tuple> resultList = typedQuery.getResultList();
		if (resultList.isEmpty()) {
			return new ResolvedPermissions(false, false, false);
		} else {
			Tuple result = resultList.get(0);
			return new ResolvedPermissions(result.get(0, Integer.class) == 1, result.get(1, Integer.class) == 1, result.get(2, Integer.class) == 1);
		}
	}
	
}
