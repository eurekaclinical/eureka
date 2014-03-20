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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LoginType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LoginTypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LoginTypeDao;
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
@Path("/protected/logintypes")
@Produces(MediaType.APPLICATION_JSON)
public class LoginTypeResource {

	private final LoginTypeDao loginTypeDao;

	@Inject
	public LoginTypeResource(
			LoginTypeDao thresholdsOpDao) {
		this.loginTypeDao = thresholdsOpDao;
	}

	@GET
	@Path("")
	public List<LoginType> getAll() {
		List<LoginTypeEntity> loginTypeEntities = this.loginTypeDao.getAll();
		List<LoginType> loginTypes = new ArrayList<>(loginTypeEntities.size());
		for (LoginTypeEntity loginTypeEntity : loginTypeEntities) {
			loginTypes.add(loginTypeEntity.toLoginType());
		}
		return loginTypes;
	}

	@GET
	@Path("/{id}")
	public LoginType get(@PathParam("id") Long inId) {
		LoginTypeEntity loginTypeEntity = this.loginTypeDao.retrieve(inId);
		if (loginTypeEntity == null) {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
		return loginTypeEntity.toLoginType();
	}

	@GET
	@Path("/byname/{name}")
	public LoginType getByName(@PathParam("name") String inName) {
		LoginTypeEntity loginTypeEntity = this.loginTypeDao.getByName(edu.emory.cci.aiw.cvrg.eureka.common.authentication.LoginType.valueOf(inName));
		if (loginTypeEntity == null) {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
		return loginTypeEntity.toLoginType();
	}
}
