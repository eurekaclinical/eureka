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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

import junit.framework.Assert;

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
		List<UserInfo> users = this.getUserList();
		Assert.assertEquals(3, users.size());
	}
	
	@Test
	public void getOptions () {
		System.out.println(
				this.resource().path("/api/user/list").options(String.class));
	}

	/**
	 * Test that an activation put request sent to the resource returns a proper
	 * OK response.
	 */
	@Test
	public void testUserActivation() {
		List<UserInfo> users = getUserList();
		Assert.assertTrue(users.size() > 0);

		UserInfo user = users.get(0);
		Long id = user.getId();
		WebResource webResource = this.resource();
		ClientResponse response = webResource.path(
				"/api/user/activate/" + String.valueOf(id.longValue())).put(
				ClientResponse.class);

		Assert.assertTrue(response.getClientResponseStatus() == Status.OK);

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

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setLastName(lastName);
		userRequest.setEmail(email);
		userRequest.setVerifyEmail(verifyEmail);
		userRequest.setOrganization(organization);
		userRequest.setPassword(password);
		userRequest.setVerifyPassword(verifyPassword);

		ClientResponse response = webResource.path("/api/user")
				.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, userRequest);

		Assert.assertEquals(Status.NO_CONTENT, response.getClientResponseStatus());

	}

	/**
	 * Test the password changing functionality.
	 */
	@Test
	public void testPasswordChange() {
		WebResource resource = this.resource();
		List<UserInfo> users = this.getUserList();
		UserInfo user = users.get(0);
		ClientResponse response = resource
				.path("/api/user/passwd/" + user.getId())
				.queryParam("oldPassword", "testpassword")
				.queryParam("newPassword", "newpassword")
				.put(ClientResponse.class);
		Assert.assertEquals(Status.NO_CONTENT, response.getClientResponseStatus());
	}
	
	@Test
	public void testResetPassword() throws NoSuchAlgorithmException {
		PasswordGenerator generator = this.getInstance(PasswordGenerator
				.class);
		List<UserInfo> users;
		UserInfo user;

		WebResource resource = this.resource();
		users = this.getUserList();
		user = users.get(0);
		ClientResponse response = resource
				.path("/api/user/pwreset/" + user.getEmail())
				.put(ClientResponse.class);
		Assert.assertEquals(Status.NO_CONTENT, response.getClientResponseStatus());

		users = this.getUserList();
		user = users.get(0);
		Assert.assertEquals(StringUtil.md5(generator.generatePassword()),
				user.getPassword());
	}

	/**
	 * Test the "find user by name" functionality in UserResource.
	 */
	@Test
	public void testFindByName() {
		WebResource resource = this.resource();
		List<UserInfo> users = this.getUserList();
		UserInfo user = users.get(0);
		ClientResponse response = resource.path("/api/user/byname/" + user.getEmail()).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Assert.assertEquals(Status.OK, response.getClientResponseStatus());

		UserInfo responseUser = response.getEntity(UserInfo.class);
		Assert.assertEquals(user.getEmail(), responseUser.getEmail());
	}
}
