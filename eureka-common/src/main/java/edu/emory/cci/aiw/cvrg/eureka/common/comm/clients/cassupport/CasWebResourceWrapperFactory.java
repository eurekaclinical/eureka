package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.cassupport;

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

import com.sun.jersey.api.client.WebResource;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.WebResourceWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.WebResourceWrapperFactory;

/**
 *
 * @author Andrew Post
 */
public class CasWebResourceWrapperFactory implements WebResourceWrapperFactory{

	@Override
	public WebResourceWrapper getInstance(WebResource webResource) {
		return new CasWebResourceWrapper(webResource);
	}
	
}
