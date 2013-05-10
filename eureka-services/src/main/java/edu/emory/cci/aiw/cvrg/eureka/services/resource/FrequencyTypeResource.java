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

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import javax.ws.rs.core.Response;

/**
 * @author hrathod
 */
@Path("/protected/frequencytype")
@Produces(MediaType.APPLICATION_JSON)
public class FrequencyTypeResource {

	private final FrequencyTypeDao frequencyTypeDao;

	@Inject
	public FrequencyTypeResource (FrequencyTypeDao inFrequencyTypeDao) {
		this.frequencyTypeDao = inFrequencyTypeDao;
	}

	@GET
	@Path("/list")
	public List<FrequencyType> getAll () {
		return this.frequencyTypeDao.getAll();
	}
	
	@GET
	@Path("/listasc")
	public List<FrequencyType> getAllAsc () {
		return this.frequencyTypeDao.getAllAsc();
	}

	@GET
	@Path("/{id}")
	public FrequencyType get (@PathParam("id") Long inId) {
		FrequencyType result = this.frequencyTypeDao.retrieve(inId);
		if (result == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		return result;
	}
	
	@GET
	@Path("/byname/{name}")
	public FrequencyType getByName(@PathParam("name") String inName) {
		FrequencyType result = this.frequencyTypeDao.getByName(inName);
		if (result == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		return result;
	}
	
	@GET
	@Path("/default")
	public FrequencyType getDefault() {
		FrequencyType result = this.frequencyTypeDao.getDefault();
		if (result == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		return result;
	}
}
