/*
 * #%L
 * Eureka Protempa ETL
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;

import edu.emory.cci.aiw.cvrg.eureka.common.test.AbstractResourceTest;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.AppTestModule;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.ContextTestListener;
import edu.emory.cci.aiw.cvrg.eureka.etl.test.Setup;

/**
 * @author hrathod
 */
public abstract class AbstractEtlResourceTest extends AbstractResourceTest {
	
	@Override
	protected final Class<? extends ServletContextListener> getListener() {
		return ContextTestListener.class;
	}

	@Override
	protected final Class<? extends Filter> getFilter() {
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
