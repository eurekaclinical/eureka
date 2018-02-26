/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.test;

import com.google.inject.Module;
import com.google.inject.persist.PersistService;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PhenotypeConversionSupport;
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
	
	private PhenotypeConversionSupport conversionSupport;

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
		this.conversionSupport = new PhenotypeConversionSupport();
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
	
	protected String toPropositionIdWrapped(String phenotypeKey) {
		return this.conversionSupport.toPropositionIdWrapped(phenotypeKey);
	}
	
	protected String toPropositionIdWrapped(PhenotypeEntity phenotype) {
		return this.conversionSupport.toPropositionIdWrapped(phenotype);
	}
	
	protected String toPropositionId(PhenotypeEntity phenotype) {
		return this.conversionSupport.toPropositionId(phenotype);
	}
	
	protected String toPropositionId(String phenotypeKey) {
		return this.conversionSupport.toPropositionId(phenotypeKey);
	}
	
	protected String asValueString(PhenotypeEntity phenotype) {
		return this.conversionSupport.asValueString(phenotype);
	}
	
	protected String asValueString(String phenotypeKey) {
		return this.conversionSupport.asValueString(phenotypeKey);
	}
	
	protected String asValueCompString(PhenotypeEntity phenotype) {
		return this.conversionSupport.asValueCompString(phenotype);
	}
	
	protected String asValueCompString(String phenotypeKey) {
		return this.conversionSupport.asValueCompString(phenotypeKey);
	}
	
	protected NominalValue asValue(PhenotypeEntity phenotype) {
		return this.conversionSupport.asValue(phenotype);
	}
	
	protected NominalValue asValue(String phenotypeKey) {
		return this.conversionSupport.asValue(phenotypeKey);
	}
}
