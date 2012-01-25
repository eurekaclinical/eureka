package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.Role;

/**
 * Tests for the {@link RoleResource} class.
 * 
 * @author hrathod
 * 
 */
public class RoleResourceTest extends AbstractResourceTest {

	/**
	 * Simply call super()
	 */
	public RoleResourceTest() {
		super();
	}

	/**
	 * Test that proper number of roles are returned from the resource.
	 */
	@Test
	public void testRoleList() {
		WebResource webResource = this.resource();
		List<Role> roles = webResource.path("/api/role/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Role>>() {
					// Nothing to implement, used to hold returned data.
				});
		Assert.assertEquals(2, roles.size());
	}
}
