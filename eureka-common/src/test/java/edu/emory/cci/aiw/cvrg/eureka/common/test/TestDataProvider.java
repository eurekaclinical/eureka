/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.test;

/**
 * Sets up the data needed to run unit or resource tests for the module.
 *
 * @author hrathod
 */
public interface TestDataProvider {

	/**
	 * Set up the data source for the test with needed data.
	 *
	 * @throws TestDataException If the data can not be provided properly.
	 */
	public void setUp() throws TestDataException;

	/**
	 * Tear down the data source for the test.
	 *
	 * @throws TestDataException If the data can not be cleaned up properly.
	 */
	public void tearDown() throws TestDataException;
}
