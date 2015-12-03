package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.cassupport;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import javax.ws.rs.core.MultivaluedMap;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.AbstractWebResourceWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;

/**
 *
 * @author Andrew Post
 */
public class CasWebResourceWrapper extends AbstractWebResourceWrapper {

	CasWebResourceWrapper(WebResource inWebResource) {
		super(inWebResource);
	}

	@Override
	public WebResource rewritten(String path, String method, MultivaluedMap<String, String> queryParams) throws ClientException{
		WebResource webResource = getWebResource();
		if (queryParams != null) {
			webResource = webResource.queryParams(queryParams);
		}
		webResource = webResource.path(path);
		webResource = withProxyTicket(webResource);
		return webResource;
	}

	private static WebResource withProxyTicket(WebResource webResource) throws ClientException {
		Assertion assertion = AssertionHolder.getAssertion();
		if (assertion != null) {
			AttributePrincipal principal = assertion.getPrincipal();
			String proxyTicket = principal.getProxyTicketFor(
					webResource.getURI().toString());
			if (proxyTicket == null) {
				throw new ClientException(
						ClientResponse.Status.UNAUTHORIZED, 
						"Could not get proxy ticket for service call " + webResource.getURI().toString());
			}
			return webResource.queryParam("ticket", proxyTicket);
		} else {
			return webResource;
		}
	}
}
