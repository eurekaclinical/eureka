/*
 * #%L
 * Eureka Common
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


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author hrathod
 */
public abstract class AbstractTest {

	/**
	 * Guice injector to be used to fetch an instance of the persistence
	 * service, and the test data setup class.
	 */
	private Injector injector;

	/**
	 * Creates sets up the Guice injector using the {@link #getModules()}
	 * method.
	 */
	public AbstractTest() {
		super();
	}

	/**
	 * Returns an instance of the given class, using the Guice Injector.
	 *
	 * @param <T> The class from which to create the instance.
	 * @param className The name of the class.
	 * @return An instance of the named class.
	 */
	protected final <T> T getInstance(Class<T> className) {
		return injector.getInstance(className);
	}

		

	/**
	 * Returns the Guice modules used to set up the Injector for the test.
	 *
	 * @return The Guice modules used to configure the Injector.
	 */
	protected abstract Module[] getModules();

	/**
	 * Runs before each test is executed. Sets up Guice.
	 */
	@Before
	public void beforeAbstractTest() {
		if (injector == null) {
			injector = Guice.createInjector(this.getModules());
		}
	}
}
