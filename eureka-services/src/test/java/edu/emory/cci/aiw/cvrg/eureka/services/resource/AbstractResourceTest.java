package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

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

}
