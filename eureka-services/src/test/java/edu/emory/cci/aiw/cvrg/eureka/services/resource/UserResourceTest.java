package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

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
		Assert.assertEquals(3, users.size());
	}

	/**
	 * Test that an activation put request sent to the resource returns a proper
	 * OK response.
	 */
	@Test
	public void testUserActivation() {
		List<User> users = getUserList();
		Assert.assertTrue(users.size() > 0);

		User user = users.get(0);
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
		String organziation = "Emory University";
		String password = "password";
		String verifyPassword = "password";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setLastName(lastName);
		userRequest.setEmail(email);
		userRequest.setVerifyEmail(verifyEmail);
		userRequest.setOrganization(organziation);
		userRequest.setPassword(password);
		userRequest.setVerifyPassword(verifyPassword);

		ClientResponse response = webResource.path("/api/user/add")
				.type(MediaType.APPLICATION_JSON).accept(MediaType.TEXT_PLAIN)
				.post(ClientResponse.class, userRequest);

		Assert.assertEquals(Status.CREATED, response.getClientResponseStatus());

	}

	/**
	 * Test the password changing functionality.
	 */
	@Test
	public void testPasswordChange() {
		WebResource resource = this.resource();
		List<User> users = this.getUserList();
		User user = users.get(0);
		ClientResponse response = resource
				.path("/api/user/passwd/" + user.getId())
				.queryParam("oldPassword", "testpassword")
				.queryParam("newPassword", "newpassword")
				.get(ClientResponse.class);
		Assert.assertEquals(Status.OK, response.getClientResponseStatus());
	}

	/**
	 * Test the "find user by name" functionality in UserResource.
	 */
	@Test
	public void testFindByName() {
		WebResource resource = this.resource();
		List<User> users = this.getUserList();
		User user = users.get(0);
		ClientResponse response = resource.path("/api/user/byname/" + user.getEmail()).accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Assert.assertEquals(Status.OK, response.getClientResponseStatus());

		User responseUser = response.getEntity(User.class);
		Assert.assertEquals(user.getEmail(), responseUser.getEmail());
	}
}
