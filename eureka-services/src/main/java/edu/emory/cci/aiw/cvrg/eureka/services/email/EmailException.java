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
package edu.emory.cci.aiw.cvrg.eureka.services.email;

/**
 * To be thrown when there are any errors in initializing an email sender, or in
 * sending the email.
 * 
 * @author hrathod
 * 
 */
public class EmailException extends Exception {
	/**
	 * Used when serializing.
	 */
	private static final long serialVersionUID = 8115982466783627054L;

	/**
	 * Create an exception with the given message.
	 * 
	 * @param message The message for the exception.
	 */
	public EmailException(final String message) {
		super(message);
	}

	/**
	 * Create an exception with the given Throwable as the root cause.
	 * 
	 * @param throwable The root cause.
	 */
	public EmailException(Throwable throwable) {
		super(throwable);
	}
}
