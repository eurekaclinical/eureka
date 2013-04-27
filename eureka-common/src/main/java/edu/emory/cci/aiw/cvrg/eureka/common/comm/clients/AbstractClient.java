package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import edu.emory.cci.aiw.cvrg.eureka.common.json.ObjectMapperProvider;

/**
 *
 * @author Andrew Post
 */
public abstract class AbstractClient {
	
	protected AbstractClient() {
		
	}
	
	protected Client getRestClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
		//ApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
		//clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);
		clientConfig.getFeatures().put(
				JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		clientConfig.getClasses().add(ObjectMapperProvider.class);
		return Client.create(clientConfig);
		//return ApacheHttpClient.create(clientConfig);
	}

	public WebResource getResource() {
		return this.getRestClient().resource(this.getResourceUrl());
	}

	protected abstract String getResourceUrl();
}
