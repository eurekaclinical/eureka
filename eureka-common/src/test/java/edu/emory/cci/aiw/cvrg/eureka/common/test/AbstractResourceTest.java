/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.test;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import com.sun.jersey.test.framework.WebAppDescriptor.Builder;

import edu.emory.cci.aiw.cvrg.eureka.common.json.ObjectMapperProvider;

/**
 * Base class for all the Jersey resource related test classes.
 *
 * @author hrathod
 */
public abstract class AbstractResourceTest extends JerseyTest {

	/**
	 * An instance of Guice Injector, used to create other necessary objects for
	 * the test.
	 */
	private final Injector injector;
	/**
	 * An instance of the data provider class.
	 */
	private TestDataProvider dataProvider;
	/**
	 * The persist service set up to set up and tear down test data.
	 */
	private PersistService persistService;

	/**
	 * Create the context, filters, etc. for the embedded server to test
	 * against.
	 */
	protected AbstractResourceTest() {
		super();
		this.injector = Guice.createInjector(this.getModules());
	}

	@Override
	protected AppDescriptor configure() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
				Boolean.TRUE);
		clientConfig.getClasses().add(ObjectMapperProvider.class);
		 ;
		
		Builder builder = (new WebAppDescriptor.Builder())
				.contextListenerClass(getListener())
				.filterClass(getFilter())
				.clientConfig(clientConfig);
		return builder.build();
	}

	/**
	 * Runs any test set up code before the actual test is run. The data for the
	 * test is created from this method.
	 *
	 * @throws Exception Thrown if there are errors while creating test
	 * data, or propagating exception from super.setUp().
	 */
	@Override
	public final void setUp() throws Exception {
		super.setUp();
		this.persistService = this.injector.getInstance(PersistService.class);
		this.persistService.start();

		this.dataProvider = this.injector.getInstance(this.getDataProvider());
		this.dataProvider.setUp();
	}

	/**
	 * Tears down any scaffolding for the test after the actual test is
	 * finished. The data for the test is destroyed from this method.
	 *
	 * @throws Exception Thrown if there are errors while cleaning up test data,
	 * or propagating exception from super.tearDown().
	 */
	@Override
	public final void tearDown() throws Exception {
		this.dataProvider.tearDown();
		this.persistService.stop();
		super.tearDown();
	}

	/**
	 * Returns an instance of the given class, using the Guice Injector.
	 *
	 * @param <T> The generic class from which to create the instance.
	 * @param className The name of the class.
	 * @return An instance of the named class.
	 */
	protected final <T> T getInstance(Class<T> className) {
		return this.injector.getInstance(className);
	}

	/**
	 * Returns the context listener to use when configuring an AppDescriptor for
	 * the test.
	 *
	 * @return A servlet context listener for the test.
	 */
	protected abstract Class<? extends ServletContextListener> getListener();

	/**
	 * Returns the filter to use when configuring an AppDescriptor for the test.
	 *
	 * @return A filter for the test.
	 */
	protected abstract Class<? extends Filter> getFilter();

	/**
	 * Returns the class used to set up the data for the test.
	 *
	 * @return A data provider for the test.
	 */
	protected abstract Class<? extends TestDataProvider> getDataProvider();

	/**
	 * Returns a set of Google Guice modules used to configure the injector for
	 * the test.
	 *
	 * @return An array of Modules.
	 */
	protected abstract Module[] getModules();

	@Override
	public WebResource resource() {
		WebResource resource = super.resource();
		resource.addFilter(new HTTPBasicAuthFilter("test", "test"));
		return resource;
	}
	
	
}
