package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;

import junit.framework.Assert;

/**
 * Test case for the User data access object.
 *
 * @author hrathod
 */
public class UserDaoTest extends AbstractServiceTest {

	/**
	 * Test the number of objects returned by the data access object. The number should match the number of users
	 * seeded in
	 * the class constructor.
	 */
	@Test
	public void testDao() {
		UserDao dao = this.getInstance(UserDao.class);
		List<User> users = dao.getAll();
		Assert.assertEquals(3, users.size());
	}

	/**
	 * Tests the ability to get a User by their email address from the DAO.
	 */
	@Test
	public void testGetByName() {
		UserDao dao = this.getInstance(UserDao.class);
		List<User> users = dao.getAll();
		User user = users.get(0);
		User testUser = dao.getByName(user.getEmail());
		Assert.assertEquals(user.getEmail(), testUser.getEmail());
	}

}
