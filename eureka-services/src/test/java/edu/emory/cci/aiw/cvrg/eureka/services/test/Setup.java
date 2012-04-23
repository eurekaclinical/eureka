package edu.emory.cci.aiw.cvrg.eureka.services.test;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.media.imageioimpl.plugins.tiff.TIFFAttrInfo;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

/**
 * Sets up the environment for testing, by bootstrapping the data store with
 * certain items to test against.
 *
 * @author hrathod
 *
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
	}

	@Override
	public void tearDown() throws TestDataException {
		this.remove(FileUpload.class);
		this.remove(User.class);
		this.remove(Role.class);
		this.researcherRole = null;
		this.adminRole = null;
		this.superRole = null;
		this.researcherUser = null;
		this.adminUser = null;
		this.superUser = null;
	}

	private <T> void remove (Class<T> className) {
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

	private User createResearcherUser() throws TestDataException {
		return this.createAndPersistUser("user@emory.edu", "Regular", "User",
				null, this.researcherRole);
	}

	private User createAdminUser() throws TestDataException {
		return this.createAndPersistUser("admin.user@emory.edu", "Admin", "User",
				null, this.researcherRole, this.adminRole);
	}

	private User createSuperUser() throws TestDataException {
		return this.createAndPersistUser("super.user@emory.edu", "Super", "User",
				null, this.researcherRole, this.adminRole, this.superRole);
	}

	private User createAndPersistUser(String email, String firstName,
			String lastName, FileUpload upload, Role... roles) throws
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
		return user;
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
