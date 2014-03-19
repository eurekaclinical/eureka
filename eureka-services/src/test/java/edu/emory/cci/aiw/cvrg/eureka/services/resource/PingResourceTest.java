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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import org.junit.Assert;

import java.util.List;

/**
 * @author hrathod
 */
public class PingResourceTest extends AbstractServiceResourceTest {

//	@Test
	public void testPing() {
		List<User> users = this.getUserList();
		User user = users.get(0);
		ClientResponse response = this.resource()
				.path("/api/ping/" + user.getId()).post(ClientResponse.class);
		Assert.assertEquals(ClientResponse.Status.NO_CONTENT,
				response.getClientResponseStatus());
	}
}
