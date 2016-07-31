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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.common.util.StringUtil;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.eurekaclinical.eureka.client.comm.LocalUser;
import org.eurekaclinical.eureka.client.comm.LocalUserRequest;
import org.eurekaclinical.eureka.client.comm.PasswordChangeRequest;
import org.eurekaclinical.eureka.client.comm.User;

import static org.junit.Assert.assertEquals;

/**
 * Test cases related to the {@link UserResource} class.
 *
 * @author hrathod
 *
 */
public class UserResourceTest extends AbstractServiceResourceTest {

	/**
	 * Simply call super().
	 */
	public UserResourceTest() {
		super();
	}

	/**
	 * Test that proper number of users are returned from the resource.
	 */
	@Test
	public void testUserList() {
		List<User> users = this.getUserList();
		assertEquals(2, users.size());
	}
	
	/**
	 * Test that a new user request sent to the resource returns a proper OK
	 * response.
	 */
	@Test
	public void testUserRequest() {
		WebResource webResource = this.resource();

		String email = "test@emory.edu";
		String verifyEmail = "test@emory.edu";
		String firstName = "Joe";
		String lastName = "Schmoe";
		String organization = "Emory University";
		String password = "password";
		String verifyPassword = "password";
		String title = "Software Engineer";
		String department = "CCI";

		LocalUserRequest userRequest = new LocalUserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setLastName(lastName);
		userRequest.setUsername(email);
		userRequest.setEmail(email);
		userRequest.setVerifyEmail(verifyEmail);
		userRequest.setOrganization(organization);
		userRequest.setPassword(password);
		userRequest.setVerifyPassword(verifyPassword);
		userRequest.setDepartment(department);
		userRequest.setTitle(title);

		ClientResponse response = webResource.path("/api/userrequests")
				.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, userRequest);

		assertEquals(Status.CREATED, response.getClientResponseStatus());

	}

	/**
	 * Test the password changing functionality.
	 */
	@Test
	public void testPasswordChange() {
		WebResource resource = this.resource();
		PasswordChangeRequest request = new PasswordChangeRequest();
		request.setOldPassword("testpassword");
		request.setNewPassword("newPassword");
		ClientResponse response = resource
				.path("/api/protected/users/passwordchange")
				.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, request);
		assertEquals(Status.NO_CONTENT, response.getClientResponseStatus());
	}
	
	@Test
	public void testResetPassword() throws NoSuchAlgorithmException {
		PasswordGenerator generator = this.getInstance(PasswordGenerator
				.class);
		List<User> users;
		LocalUser user;

		WebResource resource = this.resource();
		users = this.getUserList();
		user = (LocalUser) users.get(0);
		ClientResponse response = resource
				.path("/api/passwordreset/" + user.getEmail())
				.post(ClientResponse.class);
		assertEquals(Status.NO_CONTENT, response.getClientResponseStatus());

		users = this.getUserList();
		user = (LocalUser) users.get(0);
		assertEquals(StringUtil.md5(generator.generatePassword()),
				user.getPassword());
	}

	/**
	 * Test the "find user by name" functionality in UserResource.
	 */
	@Test
	public void testFindByName() {
		WebResource resource = this.resource();
		List<User> users = this.getUserList();
		User user = users.get(0);
		ClientResponse response = 
				resource.path("/api/protected/users/me")
						.accept(MediaType.APPLICATION_JSON)
						.get(ClientResponse.class);
		assertEquals(Status.OK, response.getClientResponseStatus());

		User responseUser = response.getEntity(User.class);
		assertEquals(user.getEmail(), responseUser.getEmail());
	}
}
