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
package edu.emory.cci.aiw.cvrg.eureka.services.thread;

/**
 * An exception to be thrown when a task can not be completed successfully.
 * 
 * @author hrathod
 * 
 */
class TaskException extends Exception {
	/**
	 * Used for serializing/deserializing.
	 */
	private static final long serialVersionUID = 1467398371312226421L;

	/**
	 * Creates an exception with the given error message.
	 * 
	 * @param message The message to associate with the exception.
	 */
	TaskException(String message) {
		super(message);
	}

	/**
	 * Creates an exception with the given Throwable as the root cause.
	 * 
	 * @param throwable The root cause.
	 */
	public TaskException(Throwable throwable) {
		super(throwable);
	}
}
