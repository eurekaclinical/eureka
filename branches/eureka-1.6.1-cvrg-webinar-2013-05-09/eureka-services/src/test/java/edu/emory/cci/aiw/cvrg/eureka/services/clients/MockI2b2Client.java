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
package edu.emory.cci.aiw.cvrg.eureka.services.clients;

import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;

/**
 * @author hrathod
 */
public class MockI2b2Client implements I2b2Client {
	@Override
	public void changePassword(String email, String passwd) throws HttpStatusException {
		// we do nothing here, as we do not actually want to send a rest call
		// anywhere.
	}
}
