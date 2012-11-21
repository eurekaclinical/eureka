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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import java.util.List;

import org.junit.Test;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
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
		final Long userId = Long.valueOf(1);
		final int expectedSize = 1;
		FileDao fileDao = this.getInstance(FileDao.class);

		List<FileUpload> actualUploads = fileDao.getByUserId(userId);

		Assert.assertEquals(expectedSize, actualUploads.size());
	}
}
