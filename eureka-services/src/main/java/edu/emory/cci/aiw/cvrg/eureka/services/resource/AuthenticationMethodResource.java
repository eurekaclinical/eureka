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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.AuthenticationMethod;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthenticationMethodEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthenticationMethodDao;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/authenticationmethod")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationMethodResource {

	private final AuthenticationMethodDao authenticationMethodDao;

	@Inject
	public AuthenticationMethodResource(
			AuthenticationMethodDao inAuthenticationMethodDao) {
		this.authenticationMethodDao = inAuthenticationMethodDao;
	}

	@GET
	@Path("")
	public List<AuthenticationMethod> getAll() {
		List<AuthenticationMethodEntity> authenticationMethodEntities = this.authenticationMethodDao.getAll();
		List<AuthenticationMethod> authenticationMethods = new ArrayList<>(authenticationMethodEntities.size());
		for (AuthenticationMethodEntity authenticationMethodEntity : authenticationMethodEntities) {
			authenticationMethods.add(authenticationMethodEntity.toAuthenticationMethod());
		}
		return authenticationMethods;
	}

	@GET
	@Path("/{id}")
	public AuthenticationMethod get(@PathParam("id") Long inId) {
		AuthenticationMethodEntity authenticationMethodEntity = this.authenticationMethodDao.retrieve(inId);
		if (authenticationMethodEntity == null) {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
		return authenticationMethodEntity.toAuthenticationMethod();
	}

	@GET
	@Path("/byname/{name}")
	public AuthenticationMethod getByName(@PathParam("name") String inName) {
		AuthenticationMethodEntity authenticationMethodEntity = this.authenticationMethodDao.getByName(edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod.valueOf(inName));
		if (authenticationMethodEntity == null) {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
		return authenticationMethodEntity.toAuthenticationMethod();
	}
}
