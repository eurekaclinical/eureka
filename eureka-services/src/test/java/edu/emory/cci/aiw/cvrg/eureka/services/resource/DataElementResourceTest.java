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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ResultThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

import junit.framework.Assert;

/**
 * @author hrathod
 *         System.out.println(results);
 */
public class DataElementResourceTest extends AbstractServiceResourceTest {

	@Test
	public void testConsecutiveFrequency() {
		List<User> users = this.getUserList();
		User user = users.get(0);

		ValueThreshold value = new ValueThreshold();
		List<ValueThreshold> values = new ArrayList<ValueThreshold>();
		value.setDataElementKey("Encounter");
		value.setUpperValue(5);
		value.setUpperUnits("days");
		values.add(value);

		ResultThresholds thresholds = new ResultThresholds();
		thresholds.setName("testThreshold-threshold");
		thresholds.setThresholdsOperator(
				ResultThresholds.ThresholdsOperator.ANY);
		thresholds.setAbbrevDisplayName("testThreshold-threshold");
		thresholds.setKey("testThreshold-threshold");
		thresholds.setCreated(new Date());
		thresholds.setInSystem(false);
		thresholds.setDisplayName("testThreshold-threshold");
		thresholds.setUserId(user.getId());
		thresholds.setValueThresholds(values);

		ClientResponse response1 = this.resource().path("/api/dataelement")
				.type(MediaType.APPLICATION_JSON).accept(
				MediaType.APPLICATION_JSON).post(
				ClientResponse.class, thresholds);
		Assert.assertEquals(
				ClientResponse.Status.NO_CONTENT, 
				response1.getClientResponseStatus());
		
		ResultThresholds testThreshold = this.resource().path(
				"/api/dataelement/" + user.getId() + "/" + thresholds.getKey
						()).accept(MediaType.APPLICATION_JSON).get
				(ResultThresholds.class);
		Assert.assertNotNull(testThreshold);

		DataElementField dataElementField = new DataElementField();
		dataElementField.setDataElementAbbrevDisplayName(
				thresholds.getAbbrevDisplayName());
		dataElementField.setDataElementDisplayName(
				thresholds.getDisplayName());
		dataElementField.setDataElementKey(thresholds.getKey());

		Frequency frequency = new Frequency();
		frequency.setUserId(user.getId());
		frequency.setDisplayName("testThreshold-frequency");
		frequency.setAbbrevDisplayName("testThreshold-frequency");
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
	}
}
