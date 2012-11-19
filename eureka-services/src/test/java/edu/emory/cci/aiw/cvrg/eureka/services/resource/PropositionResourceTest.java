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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;

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
		List<DataElement> elements = this.getUserPropositions(USER_ID);
		DataElement target = elements.get(0);
		ClientResponse response = this
		        .resource()
		        .path("/api/proposition/user/delete/" + USER_ID + "/"
		                + target.getId()).delete(ClientResponse.class);
		Assert.assertEquals(response.getClientResponseStatus(),
		        ClientResponse.Status.OK);

		List<DataElement> afterDelete = this.getUserPropositions(USER_ID);
		Assert.assertEquals(elements.size() - 1, afterDelete.size());
	}
}
