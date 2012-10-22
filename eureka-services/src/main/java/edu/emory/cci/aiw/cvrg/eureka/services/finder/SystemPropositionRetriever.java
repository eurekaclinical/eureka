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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

public class SystemPropositionRetriever implements PropositionRetriever<Long,
	String> {

	private final ServiceProperties applicationProperties;

	@Inject
	public SystemPropositionRetriever (ServiceProperties inProperties) {
		this.applicationProperties = inProperties;
	}

	@Override
	public PropositionWrapper retrieve(Long inUserId, String inKey) {
		PropositionWrapper wrapper = null;

		try {
			String path = 
					UriBuilder.fromUri("/").segment("" + inUserId, inKey).build().toString();
			Client client = CommUtils.getClient();
			WebResource resource =
				client.resource(this.applicationProperties
					.getEtlPropositionGetUrl());
			wrapper =
				resource.path(path).accept(MediaType.APPLICATION_JSON).get
					(PropositionWrapper.class);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return wrapper;
	}
}
