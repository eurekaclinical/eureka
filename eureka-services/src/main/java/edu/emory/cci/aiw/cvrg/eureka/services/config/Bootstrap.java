package edu.emory.cci.aiw.cvrg.eureka.services.config;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.User;

/**
 * To be used for Demo purposes only. Bootstraps the data store with some test
 * items so the integration between the web application and the services layer
 * can be tested more fully.
 * 
 * @author hrathod
 * 
 */
public class Bootstrap {

	/**
	 * The entity manager used to store test data in the data store.
	 */
	private final EntityManager entityManager;

	/**
	 * Create a Bootstrap class with an EntityManager.
	 * 
	 * @param inEntityManager
	 */
	@Inject
	Bootstrap(EntityManager inEntityManager) {
		this.entityManager = inEntityManager;
	}

	/**
	 * The work method of the class, adds some roles and users to the data
	 * store.
	 */
	void configure() {
		addDefaultRoles();
		addDefaultUsers();
	}

	/**
	 * Add the default roles to the data store.
	 */
	void addDefaultRoles() {
		Role role1 = new Role();
		role1.setDefaultRole(Boolean.TRUE);
		role1.setName("researcher");

		Role role2 = new Role();
		role2.setDefaultRole(Boolean.FALSE);
		role2.setName("admin");

		this.entityManager.getTransaction().begin();
		this.entityManager.persist(role1);
		this.entityManager.persist(role2);
		this.entityManager.getTransaction().commit();
	}

	/**
	 * Add the default users to the data store.
	 */
	void addDefaultUsers() {
		User user = new User();
		user.setActive(Boolean.TRUE);
		user.setVerified(Boolean.TRUE);
		user.setEmail("test.user@emory.edu");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setOrganization("Emory University");
		user.setPassword("testpassword");

		this.entityManager.getTransaction().begin();
		this.entityManager.persist(user);
		this.entityManager.getTransaction().commit();
	}
}
