package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.ws.rs.core.MediaType;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractResourceTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ContextTestListener;
import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;

/**
 * @author hrathod
 */
abstract class AbstractServiceResourceTest extends AbstractResourceTest {

	/**
	 * Create a test instance using @{link ContextTestListener} and @{link
	 * GuiceFilter}.
	 */
	AbstractServiceResourceTest() {
		super();
	}

	/**
	 * Helper method to get a list of users from the resource.
	 *
	 * @return A list of {@link User} objects, fetched from the {@link UserResource}
	 * service.
	 */
	protected List<User> getUserList() {
		WebResource webResource = this.resource();
		return webResource.path("/api/user/list").type(
				MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).
				get(new GenericType<List<User>>() {
			// Nothing to implement, used to hold returned data.
		});
	}

	@Override
	protected Class<? extends ServletContextListener> getListener() {
		return ContextTestListener.class;
	}

	@Override
	protected Class<? extends Filter> getFilter() {
		return GuiceFilter.class;
	}

	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[]{new AppTestModule()};
	}
}
