/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.test;

import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataException;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.DataElementConversionSupport;
import org.junit.After;
import org.junit.Before;
import org.protempa.proposition.value.NominalValue;

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
	
	private DataElementConversionSupport conversionSupport;

	protected Class<? extends TestDataProvider> getDataProvider() {
		return null;
	}

	protected PersistService getPersistService() {
		return null;
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
		persistService = getPersistService();
		if (persistService != null) {
			persistService.start();
		}
		if (getDataProvider() != null) {
			testDataProvider = getInstance(getDataProvider());
			testDataProvider.setUp();
		}
		this.conversionSupport = new DataElementConversionSupport();
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
		if (persistService != null) {
			persistService.stop();
		}
		this.conversionSupport = null;
	}
	
	protected String toPropositionIdWrapped(String dataElementKey) {
		return this.conversionSupport.toPropositionIdWrapped(dataElementKey);
	}
	
	protected String toPropositionIdWrapped(DataElementEntity dataElement) {
		return this.conversionSupport.toPropositionIdWrapped(dataElement);
	}
	
	protected String toPropositionId(DataElementEntity dataElement) {
		return this.conversionSupport.toPropositionId(dataElement);
	}
	
	protected String toPropositionId(String dataElementKey) {
		return this.conversionSupport.toPropositionId(dataElementKey);
	}
	
	protected String asValueString(DataElementEntity dataElement) {
		return this.conversionSupport.asValueString(dataElement);
	}
	
	protected String asValueString(String dataElementKey) {
		return this.conversionSupport.asValueString(dataElementKey);
	}
	
	protected String asValueCompString(DataElementEntity dataElement) {
		return this.conversionSupport.asValueCompString(dataElement);
	}
	
	protected String asValueCompString(String dataElementKey) {
		return this.conversionSupport.asValueCompString(dataElementKey);
	}
	
	protected NominalValue asValue(DataElementEntity dataElement) {
		return this.conversionSupport.asValue(dataElement);
	}
	
	protected NominalValue asValue(String dataElementKey) {
		return this.conversionSupport.asValue(dataElementKey);
	}
}
