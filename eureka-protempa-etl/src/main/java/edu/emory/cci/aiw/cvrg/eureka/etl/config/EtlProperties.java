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

import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.props.ApplicationProperties;

/**
 * Contains methods to fetch configuration information for the application.
 *
 * @author hrathod
 *
 */
@Singleton
public class EtlProperties extends ApplicationProperties {

	/**
	 * Gets the size of the thread pool created to run Protempa tasks.
	 *
	 * @return The size of the thread pool.
	 */
	public int getTaskThreadPoolSize() {
		return this.getIntValue("eureka.etl.threadpool.size", 4);
	}
}
