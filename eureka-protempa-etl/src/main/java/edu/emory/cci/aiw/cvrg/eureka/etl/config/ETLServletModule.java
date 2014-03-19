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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;


import edu.emory.cci.aiw.cvrg.eureka.common.config.AbstractJerseyServletModule;

/**
 * A Guice configuration module for setting up the web infrastructure and
 * binding appropriate implementations to interfaces.
 * 
 * @author hrathod
 * 
 */
public class ETLServletModule extends AbstractJerseyServletModule {

	private static final String PACKAGE_NAMES = "edu.emory.cci.aiw.cvrg.eureka.etl.resource;edu.emory.cci.aiw.cvrg.eureka.common.json";

	public ETLServletModule(EtlProperties inProperties) {
		super(inProperties, PACKAGE_NAMES);
	}

}
