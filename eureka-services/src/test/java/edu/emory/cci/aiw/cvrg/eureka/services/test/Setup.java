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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization.CategorizationType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
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
	private final Provider<EntityManager> managerProvider;
	private Role researcherRole;
	private Role adminRole;
	private Role superRole;
	private User researcherUser;
	private User adminUser;
	private User superUser;
	private List<Proposition> propositions;
	private List<ThresholdsOperator> thresholdsOperators;

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
				this.createPropositions(this.researcherUser, this.adminUser,
						this.superUser);
		this.thresholdsOperators = this.createThresholdOps();
	}

	@Override
	public void tearDown() throws TestDataException {
		this.remove(FileUpload.class);
		this.remove(Proposition.class);
		this.remove(User.class);
		this.remove(Role.class);
		this.remove(ThresholdsOperator.class);
		this.researcherRole = null;
		this.adminRole = null;
		this.superRole = null;
		this.researcherUser = null;
		this.adminUser = null;
		this.superUser = null;
		this.propositions = null;
		this.thresholdsOperators = null;
	}

	private <T> void remove(Class<T> className) {
		EntityManager entityManager = this.getEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(className);
		criteriaQuery.from(className);
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		List<T> entities = query.getResultList();

		entityManager.getTransaction().begin();
		for (T t : entities) {
			entityManager.remove(t);
		}
		entityManager.flush();
		entityManager.getTransaction().commit();
	}
	
	private List<ThresholdsOperator> createThresholdOps () {
		EntityManager entityManager = this.getEntityManager();
		List<ThresholdsOperator> result = new ArrayList<ThresholdsOperator>();
		ThresholdsOperator any = new ThresholdsOperator();
		any.setDescription("ANY");
		any.setName("any");

		entityManager.getTransaction().begin();
		entityManager.persist(any);
		entityManager.flush();
		entityManager.getTransaction().commit();

		return result;
	}

	private List<Proposition> createPropositions(User... users) {
		List<Proposition> propositions =
				new ArrayList<Proposition>(users.length);
		EntityManager entityManager = this.getEntityManager();
		entityManager.getTransaction().begin();
		Date now = new Date();
		for (User u : users) {
			Proposition encSysProp = new SystemProposition();
			encSysProp.setUserId(u.getId());
			encSysProp.setInSystem(true);
			encSysProp.setKey("Encounter");
			encSysProp.setDisplayName("Encounter");
			encSysProp.setAbbrevDisplayName("Encounter");
			encSysProp.setCreated(now);
			encSysProp.setLastModified(now);
			entityManager.persist(encSysProp);

			Categorization proposition1 = new Categorization();
			proposition1.setKey("test-cat");
			proposition1.setAbbrevDisplayName("test");
			proposition1.setDisplayName("Test Proposition");
			proposition1.setUserId(u.getId());
			proposition1.setCategorizationType(CategorizationType.EVENT);
			proposition1.setCreated(now);
			entityManager.persist(proposition1);
			propositions.add(proposition1);

			ValueThresholdEntity lowLevelAbstraction = new ValueThresholdEntity
					();
			lowLevelAbstraction.setAbbrevDisplayName("test-low-level");
			lowLevelAbstraction.setKey("test-low-level");
			lowLevelAbstraction.setCreated(now);
			lowLevelAbstraction.setCreatedFrom(ValueThresholdEntity
					.CreatedFrom.FREQUENCY);
			List<Proposition> abstractedFrom = new ArrayList<Proposition>();
			abstractedFrom.add(lowLevelAbstraction);

			HighLevelAbstraction proposition2 = new HighLevelAbstraction();
			proposition2.setKey("test-hla");
			proposition2.setAbbrevDisplayName("test");
			proposition2.setDisplayName("Test Proposition");
			proposition2.setUserId(u.getId());
			proposition2.setCreatedFrom(HighLevelAbstraction.CreatedFrom
					.FREQUENCY);
			proposition2.setAbstractedFrom(abstractedFrom);
			entityManager.persist(proposition2);
			propositions.add(proposition2);
		}
		entityManager.getTransaction().commit();
		return propositions;
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
}
