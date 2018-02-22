/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * @author hrathod
 */
@Transactional
@Path("/protected/frequencytypes")
@Produces(MediaType.APPLICATION_JSON)
public class FrequencyTypeResource {

	private final FrequencyTypeDao frequencyTypeDao;

	@Inject
	public FrequencyTypeResource (FrequencyTypeDao inFrequencyTypeDao) {
		this.frequencyTypeDao = inFrequencyTypeDao;
	}
	
	@GET
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
}
