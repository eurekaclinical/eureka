package edu.emory.cci.aiw.cvrg.eureka.services.test;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import com.google.inject.persist.PersistService;
import edu.emory.cci.aiw.cvrg.eureka.common.test.TestDataProvider;

/**
 *
 * @author arpost
 */
public class AbstractServiceDataTest extends AbstractServiceTest {
	@Override
	protected Class<? extends TestDataProvider> getDataProvider() {
		return Setup.class;
	}
	
	@Override
	protected PersistService getPersistService() {
		return getInstance(PersistService.class);
	}
}
