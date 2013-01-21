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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;

/**
 * @author hrathod
 */
public class DataElementResourceTest extends AbstractServiceResourceTest {

	@Test
	public void testConsecutiveFrequency() {
		List<User> users = this.getUserList();
		User user = users.get(0);

		TimeUnitDao timeUnitDao = this.getInstance(TimeUnitDao.class);
		TimeUnit timeUnit = 
				timeUnitDao.getByName(Setup.TESTING_TIME_UNIT_NAME);

		DataElementField dataElementField = new DataElementField();
		dataElementField.setDataElementKey("test-low-level");
		dataElementField.setType(DataElementField.Type.VALUE_THRESHOLD);
		dataElementField.setHasDuration(Boolean.FALSE);
		dataElementField.setHasPropertyConstraint(Boolean.FALSE);
		dataElementField.setMinDurationUnits(timeUnit.getId());
		dataElementField.setMaxDurationUnits(timeUnit.getId());

		Frequency frequency = new Frequency();
		frequency.setKey("testThreshold-frequency");
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
		frequency.setDataElement(dataElementField);
				
		ClientResponse response2 = this.resource().path("/api/dataelement")
				.type(
				MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, frequency);

		Assert.assertEquals(
				ClientResponse.Status.NO_CONTENT,
				response2.getClientResponseStatus());
		
		ClientResponse response3 = this.resource().path("/api/dataelement/" + user.getId() + "/" + frequency.getKey()).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		
		Assert.assertEquals(
				ClientResponse.Status.NO_CONTENT,
				response3.getClientResponseStatus());
	}
}
