package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Module;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;

/**
 * Test case for the User data access object.
 *
 * @author hrathod
 *
 */
public class UserDaoTest extends AbstractTest {

	/**
	 * Test the number of objects returned by the data access object. The number
	 * should match the number of users seeded in the class constructor.
	 */
	@Test
	public final void testDao() {
		UserDao dao = this.getInstance(UserDao.class);
		List<User> users = dao.getUsers();
		Assert.assertEquals(3, users.size());
	}

	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[] { new AppTestModule() };
	}

}
