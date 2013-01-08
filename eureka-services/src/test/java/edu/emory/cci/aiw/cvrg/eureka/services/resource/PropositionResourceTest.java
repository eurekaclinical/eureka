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

import java.util.List;

import javax.ws.rs.core.MediaType;

import junit.framework.Assert;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import java.util.Date;

public class PropositionResourceTest extends AbstractServiceResourceTest {

	private static final Long USER_ID = 1L;
	private static final GenericType<List<DataElement>> elementListType = new GenericType<List<DataElement>>() {
	};

	private List<DataElement> getUserPropositions(Long inUserId) {
		WebResource resource = this.resource();
		return resource.path("/api/proposition/user/list/" + inUserId)
		        .accept(MediaType.APPLICATION_JSON).get(elementListType);
	}

	@Test
	public void userPropositionTest() {
		List<DataElement> elements = this.getUserPropositions(USER_ID);
		Assert.assertTrue(elements.size() > 0);
	}

	@Test
	public void deletePropositionTest() {
		
		DataElementField dataElementField = new DataElementField();
		dataElementField.setDataElementKey("test-low-level");
		dataElementField.setType(DataElementField.Type.VALUE_THRESHOLD);

		Frequency frequency = new Frequency();
		frequency.setKey("testThreshold-frequency");
		frequency.setUserId(USER_ID);
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
		
		Frequency freqResponse = this.resource().path("/api/dataelement/" + USER_ID + "/" + frequency.getKey())
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(Frequency.class);
		
		Assert.assertNotNull(freqResponse);
		
		List<DataElement> beforeDelete = this.getUserPropositions(USER_ID);
		
		ClientResponse response = this
		        .resource()
		        .path("/api/proposition/user/delete/" + USER_ID + "/"
		                + freqResponse.getId()).delete(ClientResponse.class);
		Assert.assertEquals(response.getClientResponseStatus(),
		        ClientResponse.Status.OK);

		List<DataElement> afterDelete = this.getUserPropositions(USER_ID);
		
		Assert.assertEquals(beforeDelete.size() - 2, afterDelete.size());
	}
}
