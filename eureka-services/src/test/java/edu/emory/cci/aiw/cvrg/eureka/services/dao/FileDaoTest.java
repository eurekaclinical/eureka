package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceTest;
import junit.framework.Assert;

/**
 * Tests relating to the {@link FileDao} data access object.
 *
 * @author hrathod
 */
public class FileDaoTest extends AbstractServiceTest {

	/**
	 * Test to make sure that all files uploaded by a user a properly
	 * retrieved.
	 */
	@Test
	public void testGetByUserId() {
		UserDao userDao = this.getInstance(UserDao.class);
		FileDao fileDao = this.getInstance(FileDao.class);

		List<User> users = userDao.getAll();
		User testUser = users.get(0);
		List<FileUpload> expectedUploads = testUser.getFileUploads();

		List<FileUpload> actualUploads = fileDao.getByUserId(testUser.getId
				());

		Assert.assertEquals(expectedUploads.size(), actualUploads.size());
	}
}
