package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ContextTestListener;

/**
 * Base class for all the Jersey resource related test classes.
 * 
 * @author hrathod
 * 
 */
abstract class AbstractResourceTest extends JerseyTest {

	/**
	 * Create the context, filters, etc. for the embedded server to test
	 * against.
	 */
	AbstractResourceTest() {
		super((new WebAppDescriptor.Builder())
				.contextListenerClass(ContextTestListener.class)
				.filterClass(GuiceFilter.class).contextPath("/")
				.servletPath("/").build());
	}

	/**
	 * Helper method to get a list of users from the resource.
	 * 
	 * @return A list of {@link User} objects, fetched from the
	 *         {@link UserResource} service.
	 */
	protected List<User> getUserList() {
		WebResource webResource = this.resource();
		List<User> users = webResource.path("/api/user/list")
				.accept(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<User>>() {
					// Nothing to implement, used to hold returned data.
				});
		return users;
	}

}
