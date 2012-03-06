package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;

/**
 * Test case for the User data access object.
 * 
 * @author hrathod
 * 
 */
public class UserDaoTest {

	/**
	 * Hold the injector used to test the DAO.
	 */
	private final Injector injector;

	/**
	 * Set up the test with some fake data.
	 */
	public UserDaoTest() {
		this.injector = Guice.createInjector(new AppTestModule());

		PersistService persistService = this.injector
				.getInstance(PersistService.class);
		persistService.start();

		EntityManager entityManager = this.injector
				.getInstance(EntityManager.class);

		entityManager.getTransaction().begin();

		final User u1 = new User();
		u1.setFirstName("John");
		u1.setLastName("Doe");
		u1.setOrganization("TEST");
		u1.setRoles(null);
		entityManager.persist(u1);

		final User u2 = new User();
		u2.setFirstName("Jane");
		u2.setLastName("Doe");
		u2.setOrganization("TEST");
		u2.setRoles(null);
		entityManager.persist(u2);

		entityManager.getTransaction().commit();
	}

	/**
	 * Test the number of objects returned by the data access object. The number
	 * should match the number of users seeded in the class constructor.
	 */
	@Test
	public void testDao() {
		UserDao dao = this.injector.getInstance(UserDao.class);
		List<User> users = dao.getUsers();
		Assert.assertEquals(2, users.size());
	}

}
