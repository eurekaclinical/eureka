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
package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.UserPrincipalAttributes;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser_;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.LoginType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity_;
import java.text.MessageFormat;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the {@link EtlUserDao} interface, backed by JPA entities
 * and queries.
 *
 * @author Andrew Post
 */
public class JpaEtlUserDao extends GenericDao<EtlUser, Long> implements EtlUserDao {
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			JpaEtlUserDao.class);

	/**
	 * Create an object with the give entity manager.
	 *
	 * @param inEMProvider The entity manager to be used for communication with
	 *                     the data store.
	 */
	@Inject
	public JpaEtlUserDao(Provider<EntityManager> inEMProvider) {
		super(EtlUser.class, inEMProvider);
	}

	@Override
	public EtlUser getByAttributePrincipal(AttributePrincipal principal) {
		return getByUsername(principal.getName());
	}
	
	@Override
	public EtlUser getByUsername(String username) {
		EntityManager entityManager = getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<EtlUser> criteriaQuery = builder.createQuery(EtlUser.class);
		Root<EtlUser> root = criteriaQuery.from(EtlUser.class);
		Path<String> usernamePath = root.get(EtlUser_.username);
		TypedQuery<EtlUser> query = entityManager.createQuery(criteriaQuery.where(
				builder.equal(usernamePath, username)));
		EtlUser result;
		try {
			result = query.getSingleResult();
		} catch (NonUniqueResultException nure) {
			String msg = MessageFormat.format(
					"More than one user with {0} = {1}", 
					UserEntity_.username, username);
			LOGGER.error(msg);
			throw new AssertionError(msg);
		} catch (NoResultException nre) {
			LOGGER.debug("No user with {} = {} and {} = {}", 
					UserEntity_.username, username);
			result = null;
		}
		return result;
	}
}
