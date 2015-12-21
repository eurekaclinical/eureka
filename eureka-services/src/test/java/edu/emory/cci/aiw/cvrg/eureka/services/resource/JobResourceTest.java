/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import org.junit.Test;

/**
 * Test cases for the job resource.
 *
 * @author hrathod
 *
 */
public class JobResourceTest extends AbstractServiceResourceTest {

	/**
	 * A test to check whether submitting a job via the JobResource class works.
	 */
	@Test
	public void testJobSubmission() {
		// TODO: activate this test when the back-end URLs are available.
		// WebResource resource = this.resource();
		// FileUpload fileUpload = new FileUpload();
		// fileUpload.setLocation("/home/hrathod/Eureka_Presentation/sample.xlsx");
		// List<User> users = this.getUserList();
		// User user = users.get(0);
		// fileUpload.setUser(user);
		// ClientResponse response = resource.path("/api/job/add").post(
		// ClientResponse.class, fileUpload);
		// System.out.println(response.getClientResponseStatus());
		// Client client = Client.create();
		// WebResource resource = client
		// .resource("http://localhost:8080/eureka-services/api/job/add");
		// ClientResponse response = resource.post(ClientResponse.class,
		// fileUpload);
		// System.out.println(response.getClientResponseStatus());
	}
}
