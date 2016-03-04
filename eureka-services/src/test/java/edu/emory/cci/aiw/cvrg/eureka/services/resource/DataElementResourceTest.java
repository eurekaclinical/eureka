/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;

/**
 * @author hrathod
 */
public class DataElementResourceTest extends AbstractServiceResourceTest {
	private User user;
	private String frequencyKey;
	
	@Before
	public void setupDataElementResourceTest() {
		List<User> users = this.getUserList();
		user = users.get(0);
		frequencyKey = "testThreshold-frequency";
	}

	@Test
	public void testConsecutiveFrequency() {
		TimeUnitDao timeUnitDao = this.getInstance(TimeUnitDao.class);
		TimeUnit timeUnit = 
				timeUnitDao.getByName(Setup.TESTING_TIME_UNIT_NAME);
		FrequencyTypeDao freqTypeDao = getInstance(FrequencyTypeDao.class);
		FrequencyType freqType =
				freqTypeDao.getByName(Setup.TESTING_FREQ_TYPE_NAME);

		DataElementField dataElementField = new DataElementField();
		dataElementField.setDataElementKey("test-low-level");
		dataElementField.setType(DataElement.Type.VALUE_THRESHOLD);
		dataElementField.setHasDuration(Boolean.FALSE);
		dataElementField.setHasPropertyConstraint(Boolean.FALSE);
		dataElementField.setMinDurationUnits(timeUnit.getId());
		dataElementField.setMaxDurationUnits(timeUnit.getId());
		
		Frequency frequency = new Frequency();
		frequency.setKey(frequencyKey);
		frequency.setUserId(user.getId());
		frequency.setDisplayName("testThreshold-frequency");
		frequency.setDescription("testThreshold-frequency");
		frequency.setInSystem(false);
		frequency.setCreated(new Date());
		frequency.setAtLeast(5);
		frequency.setIsConsecutive(Boolean.TRUE);
		frequency.setIsWithin(Boolean.TRUE);
		frequency.setWithinAtLeast(30);
		frequency.setWithinAtLeastUnits(Long.valueOf(1));
		frequency.setWithinAtMost(60);
		frequency.setWithinAtMostUnits(Long.valueOf(1));
		frequency.setFrequencyType(freqType.getId());
		frequency.setDataElement(dataElementField);
				
		ClientResponse response2 = this.resource().path("/api/protected/phenotypes")
				.type(
				MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, frequency);
		Assert.assertEquals(
				ClientResponse.Status.NO_CONTENT,
				response2.getClientResponseStatus());
		
		ClientResponse response3 = this.resource()
				.path("/api/protected/phenotypes/" + user.getId() + "/" + frequencyKey)
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		
		Assert.assertEquals(
				ClientResponse.Status.NO_CONTENT,
				response3.getClientResponseStatus());
		
	}
}
