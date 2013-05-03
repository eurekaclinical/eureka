/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.test;

import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;
import org.junit.After;
import org.junit.Before;

public class AbstractServiceTest extends AbstractTest {

	/**
	 * Instance of a class that can set up and tear down test data.
	 */
	private TestDataProvider testDataProvider;
	/**
	 * Instance of a persistence service, to be used to store test data and run
	 * queries against.
	 */
	private PersistService persistService;

	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}

	@Override
	protected Module[] getModules() {
		return new Module[]{new AppTestModule()};
	}

	/**
	 * Runs before each test is executed. Sets up the persistence service, and
	 * then create the necessary data for the test.
	 *
	 * @throws TestDataException Thrown if the test data can not be inserted
	 * properly.
	 */
	@Before
	public void beforeAbstractServiceTest() throws TestDataException {
		persistService = getInstance(PersistService.class);
		persistService.start();
		if (getDataProvider() != null) {
			testDataProvider = getInstance(getDataProvider());
			testDataProvider.setUp();
		}
	}

	/**
	 * Runs after each test is executed. Removes any test data, and then shuts
	 * down the persistence service.
	 *
	 * @throws TestDataException Thrown if the test data can not be cleanly
	 * removed.
	 */
	@After
	public void afterAbstractServiceTest() throws TestDataException {
		if (testDataProvider != null) {
			testDataProvider.tearDown();
		}
		persistService.stop();
		testDataProvider = null;
		persistService = null;
	}
}
