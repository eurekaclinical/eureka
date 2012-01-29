package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserRequest;

/**
 * Test cases related to the {@link UserResource} class.
 * 
 * @author hrathod
 * 
 */
public class UserResourceTest extends AbstractResourceTest {

	/**
	 * Simply call super();
	 */
	public UserResourceTest() {
		super();
	}

	/**
	 * Test that proper number of users are returned from the resource.
	 */
	@Test
	public void testUserList() {
		List<User> users = getUserList();
		Assert.assertEquals(1, users.size());
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
	 * Helper method to get a list of users from the resource.
	 * 
	 * @return A list of {@link User} objects, fetched from the
	 *         {@link UserResource} service.
	 */
	private List<User> getUserList() {
		WebResource webResource = this.resource();
		List<User> users = webResource.path("/api/user/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<User>>() {
					// Nothing to implement, used to hold returned data.
				});
		return users;
	}
	
	/**
	 * Test that a new user request sent to the resource returns a proper
	 * OK response.
	 */
	@Test
	public void testUserRequest() {
		WebResource webResource = this.resource();

		String email 		= "test@emory.edu";
		String verifyEmail 	= "test@emory.edu";
		String firstName 	= "Joe";
		String lastName 	= "Schmoe";
		String organziation ="Emory University";
		String password 	= "password";
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
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, userRequest);

		Assert.assertTrue(response.getClientResponseStatus() == Status.OK);

	}


}
