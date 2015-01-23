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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.test.AbstractServiceDataTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test case for the User data access object.
 *
 * @author hrathod
 */
public class UserDaoTest extends AbstractServiceDataTest {

	/**
	 * Test the number of objects returned by the data access object. The number should match the number of users
	 * seeded in
	 * the class constructor.
	 */
	@Test
	public void testDao() {
		UserDao dao = this.getInstance(UserDao.class);
		List<UserEntity> users = dao.getAll();
		assertEquals(3, users.size());
	}

	/**
	 * Tests the ability to get a User by their email address from the DAO.
	 */
	@Test
	public void testGetByName() {
		UserDao dao = this.getInstance(UserDao.class);
		List<UserEntity> users = dao.getAll();
		UserEntity user = users.get(0);
		UserEntity testUser = dao.getByUsername(user.getUsername());
		assertEquals(user.getEmail(), testUser.getEmail());
	}

}
