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
package edu.emory.cci.aiw.cvrg.eureka.services.test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

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
	
	private final Provider<EntityManager> managerProvider;
	private Role researcherRole;
	private Role adminRole;
	private Role superRole;
	private User researcherUser;
	private User adminUser;
	private User superUser;
	private List<DataElementEntity> propositions;
	private List<TimeUnit> timeUnits;

	/**
	 * Create a Bootstrap class with an EntityManager.
	 */
	@Inject
	Setup(Provider<EntityManager> inManagerProvider) {
		this.managerProvider = inManagerProvider;
	}

	private EntityManager getEntityManager() {
		return this.managerProvider.get();
	}

	@Override
	public void setUp() throws TestDataException {
		this.researcherRole = this.createResearcherRole();
		this.adminRole = this.createAdminRole();
		this.superRole = this.createSuperRole();
		this.researcherUser = this.createResearcherUser();
		this.adminUser = this.createAdminUser();
		this.superUser = this.createSuperUser();
		this.propositions =
				this.createDataElements(this.researcherUser, this.adminUser,
						this.superUser);
		this.timeUnits = this.createTimeUnits();
	}

	@Override
	public void tearDown() throws TestDataException {
		this.remove(FileUpload.class);
//		this.removeDataElements();
		this.remove(DataElementEntity.class);
		this.remove(User.class);
		this.remove(Role.class);
		this.remove(TimeUnit.class);
		this.remove(ThresholdsOperator.class);
		this.researcherRole = null;
		this.adminRole = null;
		this.superRole = null;
		this.researcherUser = null;
		this.adminUser = null;
		this.superUser = null;
		this.propositions = null;
		this.timeUnits = null;
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
	
	private List<DataElementEntity> createDataElements(User... users) {
		System.out.println("Creating data elements...");
		List<DataElementEntity> dataElements =
				new ArrayList<DataElementEntity>(users.length);
		EntityManager entityManager = this.getEntityManager();
		Date now = new Date();
		ThresholdsOperator any = new ThresholdsOperator();
		any.setDescription("ANY");
		any.setName("any");
		entityManager.getTransaction().begin();
		entityManager.persist(any);
		entityManager.getTransaction().commit();
		
		for (User u : users) {
			entityManager.getTransaction().begin();
			System.out.println("Loading user " + u.getEmail());
			CategoryEntity proposition1 = new CategoryEntity();
			proposition1.setKey("test-cat");
			proposition1.setAbbrevDisplayName("test");
			proposition1.setDisplayName("Test Proposition");
			proposition1.setUserId(u.getId());
			proposition1.setCategoryType(CategoryType.EVENT);
			proposition1.setCreated(now);
			entityManager.persist(proposition1);
			dataElements.add(proposition1);
			
			SystemProposition sysProp = new SystemProposition();
			sysProp.setUserId(u.getId());
			sysProp.setSystemType(SystemProposition.SystemType.PRIMITIVE_PARAMETER);
			sysProp.setInSystem(true);
			sysProp.setKey("test-prim-param");
			sysProp.setCreated(now);

			ValueThresholdGroupEntity lowLevelAbstraction = 
					new ValueThresholdGroupEntity();
			lowLevelAbstraction.setAbbrevDisplayName("test-low-level");
			lowLevelAbstraction.setKey("test-low-level");
			lowLevelAbstraction.setCreated(now);
//			lowLevelAbstraction.setValueThresholds(new ValueThresholdEntity());
//			lowLevelAbstraction.setComplementConstraint(new ValueThresholdEntity());
			lowLevelAbstraction.setThresholdsOperator(any);
			lowLevelAbstraction.setUserId(u.getId());
//			lowLevelAbstraction.setAbstractedFrom(sysProp);
			entityManager.persist(lowLevelAbstraction);
			dataElements.add(lowLevelAbstraction);
			entityManager.getTransaction().commit();
		}
		
		System.err.println("Done creating data elements!");
		return dataElements;
	}

	private User createResearcherUser() throws TestDataException {
		return this.createAndPersistUser("user@emory.edu", "Regular", "User",
				createFileUpload(), this.researcherRole);
	}

	private User createAdminUser() throws TestDataException {
		return this.createAndPersistUser("admin.user@emory.edu", "Admin",
				"User", createFileUpload(), this.researcherRole, this.adminRole);
	}

	private User createSuperUser() throws TestDataException {
		return this.createAndPersistUser("super.user@emory.edu", "Super",
				"User", createFileUpload(), this.researcherRole, this.adminRole,
				this.superRole);
	}

	private User createAndPersistUser(String email, String firstName,
	                                  String lastName, FileUpload upload,
	                                  Role... roles) throws
			TestDataException {
		EntityManager entityManager = this.getEntityManager();
		User user = new User();
		try {
			user.setActive(true);
			user.setVerified(true);
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setOrganization(ORGANIZATION);
			user.setPassword(StringUtil.md5(PASSWORD));
			user.setLastLogin(new Date());
			user.setRoles(Arrays.asList(roles));
		} catch (NoSuchAlgorithmException nsaex) {
			throw new TestDataException(nsaex);
		}
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.flush();
		entityManager.getTransaction().commit();
		
		entityManager.getTransaction().begin();
		upload.setUserId(user.getId());
		entityManager.persist(upload);
		entityManager.flush();
		entityManager.getTransaction().commit();
		return user;
	}
	
	private FileUpload createFileUpload () {
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("test_location");
		fileUpload.setTimestamp(new Date());
		return fileUpload;
	}

	private Role createResearcherRole() {
		return this.createAndPersistRole("researcher", Boolean.TRUE);
	}

	private Role createAdminRole() {
		return this.createAndPersistRole("admin", Boolean.FALSE);
	}

	private Role createSuperRole() {
		return this.createAndPersistRole("superuser", Boolean.FALSE);
	}

	private Role createAndPersistRole(String name, Boolean isDefault) {
		EntityManager entityManager = this.getEntityManager();
		Role role = new Role();
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
		List<TimeUnit> result = new ArrayList<TimeUnit>();
		result.add(timeUnit);
		return result;
	}
}
