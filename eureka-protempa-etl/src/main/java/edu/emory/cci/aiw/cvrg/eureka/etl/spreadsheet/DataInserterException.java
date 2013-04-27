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
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

/**
 * Thrown if there are any issues when inserting data from the input source to
 * the Protempa data source.
 *
 * @author hrathod
 */
public class DataInserterException extends Exception {

	/**
	 * Needed for serializing and de-serializing this class.
	 */
	private static final long serialVersionUID = -1361760705327325126L;

	/**
	 * Create exception with the given message.
	 *
	 * @param message The message for the exception.
	 */
	DataInserterException(String message) {
		super(message);
	}

	/**
	 * Create exception with the given {@link Throwable}r as the root cause.
	 *
	 * @param throwable The root cause.
	 */
	public DataInserterException(Throwable throwable) {
		super(throwable);
	}
}
