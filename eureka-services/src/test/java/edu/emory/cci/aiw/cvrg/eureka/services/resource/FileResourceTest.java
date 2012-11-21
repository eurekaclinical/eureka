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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import junit.framework.Assert;

/**
 * Tests related to the file upload resource.
 *
 * @author hrathod
 *
 */
public class FileResourceTest extends AbstractServiceResourceTest {

	/**
	 * Test if the file upload functionality works.
	 */
	@Test
	public final void testFileUpload() {
		List<User> users = this.getUserList();
		User user = users.get(0);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("/tmp/foo");
		fileUpload.setUserId(user.getId());
		fileUpload.setTimestamp(new Date());

		final WebResource resource = this.resource();
		final WebResource path = resource.path("/api/file/upload");
		ClientResponse response = path.post(
				ClientResponse.class, fileUpload);
		Assert.assertEquals(Status.CREATED, response.getClientResponseStatus());
	}
}
