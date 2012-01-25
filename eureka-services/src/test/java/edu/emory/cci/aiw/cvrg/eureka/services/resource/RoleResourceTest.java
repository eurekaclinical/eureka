package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ContextTestListener;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.Role;

/**
 * Tests for the {@link RoleResource} class.
 * 
 * @author hrathod
 * 
 */
public class RoleResourceTest extends JerseyTest {

	/**
	 * Default constructor, simply call super().
	 */
	public RoleResourceTest() {
		super((new WebAppDescriptor.Builder())
				.contextListenerClass(ContextTestListener.class)
				.filterClass(GuiceFilter.class).contextPath("/")
				.servletPath("/").build());
	}

	/**
	 * Test that proper number of roles are returned from the resource.
	 */
	@Test
	public void testRoleList() {
		WebResource webResource = this.resource();
		List<Role> roles = webResource.path("/api/role/list").get(
				new GenericType<List<Role>>() {
					// Nothing to implement, used to hold returned data.
				});
		Assert.assertEquals(0, roles.size());
	}
}
