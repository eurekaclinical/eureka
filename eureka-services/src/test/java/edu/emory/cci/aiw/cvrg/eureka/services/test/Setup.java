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
package edu.emory.cci.aiw.cvrg.eureka.services.test;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.*;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserEntityFactory;
import org.eurekaclinical.eureka.client.comm.SystemType;

/**
 * Sets up the environment for testing, by bootstrapping the data store with
 * certain items to test against.
 *
 * @author hrathod
 */
public class Setup implements TestDataProvider {

	private static final String ORGANIZATION = "Emory University";
	private static final String PASSWORD = "testpassword";
	public static final String TESTING_TIME_UNIT_NAME = "test";
	public static final String TESTING_FREQ_TYPE_NAME = "at least";
	
	private final Provider<EntityManager> managerProvider;
	private RoleEntity researcherRole;
	private RoleEntity adminRole;
	private final UserEntityFactory userEntityFactory;


	/**
	 * Create a Bootstrap class with an EntityManager.
	 */
	@Inject
	Setup(Provider<EntityManager> inManagerProvider) {
		this.managerProvider = inManagerProvider;
		this.userEntityFactory = new UserEntityFactory();
	}

	private EntityManager getEntityManager() {
		return this.managerProvider.get();
	}

	@Override
	public void setUp() throws TestDataException {
		this.researcherRole = this.createResearcherRole();
		this.adminRole = this.createAdminRole();
		UserEntity researcherUser = this.createResearcherUser();
		UserEntity adminUser = this.createAdminUser();
		this.createPhenotypes(researcherUser, adminUser);
		this.createTimeUnits();
		this.createFrequencyTypes();
	}

	@Override
	public void tearDown() throws TestDataException {
		this.remove(PhenotypeEntity.class);
		this.remove(UserEntity.class);
		this.remove(RoleEntity.class);
		this.remove(TimeUnit.class);
		this.remove(ThresholdsOperator.class);
		this.remove(FrequencyType.class);
	}

	private <T> void remove(Class<T> className) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(className);
		criteriaQuery.from(className);
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		List<T> entities = query.getResultList();
		System.out.println("Deleting " + className.getName() + "; count: " +
				entities.size());
		entityManager.getTransaction().begin();
		int i = 1;
		for (T t : entities) {
			System.out.println("on " + i++ + "; " + t);
			entityManager.flush();
			entityManager.remove(t);
		}
		entityManager.getTransaction().commit();
	}
	
	private List<PhenotypeEntity> createPhenotypes(UserEntity... users) {
		System.out.println("Creating phenotypes...");
		List<PhenotypeEntity> phenotypes =
				new ArrayList<>(users.length);
		EntityManager entityManager = this.getEntityManager();
		Date now = new Date();
		ThresholdsOperator any = new ThresholdsOperator();
		any.setDescription("ANY");
		any.setName("any");
		entityManager.getTransaction().begin();
		entityManager.persist(any);
		entityManager.getTransaction().commit();
		
		for (UserEntity u : users) {
			entityManager.getTransaction().begin();
			System.out.println("Loading user " + u.getUsername());
			CategoryEntity proposition1 = new CategoryEntity();
			proposition1.setKey("test-cat");
			proposition1.setDescription("test");
			proposition1.setDisplayName("Test Proposition");
			proposition1.setUserId(u.getId());
			proposition1.setCategoryType(CategoryType.EVENT);
			proposition1.setCreated(now);
			entityManager.persist(proposition1);
			phenotypes.add(proposition1);
			
			SystemProposition sysProp = new SystemProposition();
			sysProp.setUserId(u.getId());
			sysProp.setSystemType(SystemType.PRIMITIVE_PARAMETER);
			sysProp.setInSystem(true);
			sysProp.setKey("test-prim-param");
			sysProp.setCreated(now);

			ValueThresholdGroupEntity lowLevelAbstraction = 
					new ValueThresholdGroupEntity();
			lowLevelAbstraction.setDescription("test-low-level");
			lowLevelAbstraction.setKey("test-low-level");
			lowLevelAbstraction.setCreated(now);
			lowLevelAbstraction.setThresholdsOperator(any);
			lowLevelAbstraction.setUserId(u.getId());
			entityManager.persist(lowLevelAbstraction);
			phenotypes.add(lowLevelAbstraction);
			entityManager.getTransaction().commit();
		}
		
		System.err.println("Done creating phenotypes!");
		return phenotypes;
	}

	private UserEntity createResearcherUser() throws TestDataException {
		return this.createAndPersistUser("user@emory.edu", "Regular", "User",
				this.researcherRole);
	}

	private UserEntity createAdminUser() throws TestDataException {
		return this.createAndPersistUser("admin.user@emory.edu", "Admin",
				"User", this.researcherRole, this.adminRole);
	}

	private UserEntity createAndPersistUser(String email, String firstName,
	                                  String lastName,
	                                  RoleEntity... roles) throws
			TestDataException {
		EntityManager entityManager = this.getEntityManager();
		UserEntity user = this.userEntityFactory.getUserEntityInstance();

                user.setUsername(email);
                user.setRoles(Arrays.asList(roles));

		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.flush();
		entityManager.getTransaction().commit();
		
		entityManager.getTransaction().begin();
		entityManager.flush();
		entityManager.getTransaction().commit();
		return user;
	}

	private RoleEntity createResearcherRole() {
		return this.createAndPersistRole("researcher", Boolean.TRUE);
	}

	private RoleEntity createAdminRole() {
		return this.createAndPersistRole("admin", Boolean.FALSE);
	}

	private RoleEntity createAndPersistRole(String name, Boolean isDefault) {
		EntityManager entityManager = this.getEntityManager();
		RoleEntity role = new RoleEntity();
		role.setName(name);
		role.setDefaultRole(isDefault);
		entityManager.getTransaction().begin();
		entityManager.persist(role);
		entityManager.flush();
		entityManager.getTransaction().commit();
		return role;
	}

	private List<TimeUnit> createTimeUnits () {
		EntityManager entityManager = this.getEntityManager();
		TimeUnit timeUnit = new TimeUnit();
		timeUnit.setName(TESTING_TIME_UNIT_NAME);
		timeUnit.setDescription("test timeunit");
		entityManager.getTransaction().begin();
		entityManager.persist(timeUnit);
		entityManager.flush();
		entityManager.getTransaction().commit();
		return Collections.singletonList(timeUnit);
	}
	
	private List<FrequencyType> createFrequencyTypes() {
		EntityManager entityManager = getEntityManager();
		FrequencyType freqType = new FrequencyType();
		freqType.setName(TESTING_FREQ_TYPE_NAME);
		freqType.setDescription("test frequency type");
		freqType.setRank(1);
		freqType.setDefault(true);
		entityManager.getTransaction().begin();
		entityManager.persist(freqType);
		entityManager.getTransaction().commit();
		return Collections.singletonList(freqType);
	}
}
