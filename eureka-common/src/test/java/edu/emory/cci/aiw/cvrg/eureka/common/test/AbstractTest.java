/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.test;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.PersistService;

/**
 *
 * @author hrathod
 */
public abstract class AbstractTest {

	/**
	 * Guice injector to be used to fetch an instance of the persistence
	 * service, and the test data setup class.
	 */
	private final Injector injector;
	/**
	 * Instance of a class that can set up and tear down test data.
	 */
	private TestDataProvider testDataProvider;
	/**
	 * Instance of a persistence service, to be used to store test data and run
	 * queries against.
	 */
	private PersistService persistService;

	/**
	 * Creates sets up the Guice injector using the {@link #getModules()}
	 * method.
	 */
	public AbstractTest() {
		super();
		this.injector = Guice.createInjector(this.getModules());
	}

	/**
	 * Returns an instance of the given class, using the Guice Injector.
	 *
	 * @param <T> The class from which to create the instance.
	 * @param className The name of the class.
	 * @return An instance of the named class.
	 */
	protected final <T> T getInstance(Class<T> className) {
		return this.injector.getInstance(className);
	}

	/**
	 * Returns the class to be used to set up and tear down data for the test in
	 * the data store.
	 *
	 * @return Class name of the data provider.
	 */
	protected abstract Class<? extends TestDataProvider> getDataProvider();

	/**
	 * Returns the Guice modules used to set up the Injector for the test.
	 *
	 * @return The Guice modules used to configure the Injector.
	 */
	protected abstract Module[] getModules();

	/**
	 * Runs before each test is executed. Sets up the persistence service, and
	 * then create the necessary data for the test.
	 *
	 * @throws TestDataException Thrown if the test data can not be inserted
	 * properly.
	 */
	@Before
	public void beforeTest() throws TestDataException {
		this.persistService = this.injector.getInstance(PersistService.class);
		this.persistService.start();
		this.testDataProvider = this.injector.getInstance(this.getDataProvider());
		this.testDataProvider.setUp();
	}

	/**
	 * Runs after each test is executed. Removes any test data, and then shuts
	 * down the persistence service.
	 *
	 * @throws TestDataException Thrown if the test data can not be cleanly
	 * removed.
	 */
	@After
	public void afterTest() throws TestDataException {
		this.testDataProvider.tearDown();
		this.persistService.stop();
		this.testDataProvider = null;
		this.persistService = null;
	}
}
