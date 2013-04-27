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
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.AbstractWebResourceWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;

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
		if (!HttpMethod.POST.equals(method)) {
			webResource = webResource.path(path);
			webResource = withProxyTicket(webResource);
		} else {
			try {
				String jsessionId = doGetSession();
				path += ";jsessionid=" + jsessionId;
				webResource = webResource.path(path);
			} catch (ClientException ex) {
				Logger.getLogger(CasWebResourceWrapper.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return webResource;
	}

	private String doGetSession() throws ClientException {
		WebResource webResource = getWebResource().path("/api/ping");
		webResource = withProxyTicket(webResource);
		ClientResponse response = webResource.get(ClientResponse.class);
		return extractJSESSIONID(response);
	}

	private static String extractJSESSIONID(ClientResponse resp) throws UniformInterfaceException, ClientHandlerException {
		List<NewCookie> cookies = resp.getCookies();
		String jsessionId = null;
		for (NewCookie newCookie : cookies) {
			if ("JSESSIONID".equals(newCookie.getName())) {
				jsessionId = newCookie.getValue();
				break;
			}
		}
		if (jsessionId == null) {
			List<String> get = resp.getHeaders().get("JSESSIONID");
			if (get != null && !get.isEmpty()) {
				jsessionId = get.get(0);
			}
		}
		return jsessionId;
	}

	private static WebResource withProxyTicket(WebResource webResource) throws ClientException {
		Assertion assertion = AssertionHolder.getAssertion();
		if (assertion != null) {
			AttributePrincipal principal = assertion.getPrincipal();
			String proxyTicket = principal.getProxyTicketFor(
					webResource.getURI().toString());
			if (proxyTicket == null) {
				throw new ClientException(
						ClientResponse.Status.INTERNAL_SERVER_ERROR, 
						"Could not get proxy ticket for service call " + webResource.getURI().toString());
			}
			return webResource.queryParam("ticket", proxyTicket);
		} else {
			return webResource;
		}
	}
}
