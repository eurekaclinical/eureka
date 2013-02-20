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
package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/**
 *
 * @author hrathod
 */
public class ConfigurationError extends Error {

	/**
	 * Used for serialization and de-serialization.
	 */
	private static final long serialVersionUID = 967409802728884898L;

	/**
	 * Create an error with the given string as the error message.
	 *
	 * @param message The error message.
	 */
	ConfigurationError(String message) {
		super(message);
	}

	/**
	 * Creates an error with the given {@link Throwable} as the root cause.
	 *
	 * @param throwable The root cause.
	 */
	ConfigurationError(Throwable throwable) {
		super(throwable);
	}
}
