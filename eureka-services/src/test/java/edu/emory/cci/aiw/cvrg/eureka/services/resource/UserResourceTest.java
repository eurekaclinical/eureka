package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.User;

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
	public void testRoleList() {
		WebResource webResource = this.resource();
		List<User> users = webResource.path("/api/user/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<User>>() {
					// Nothing to implement, used to hold returned data.
				});
		Assert.assertEquals(1, users.size());
	}

}
