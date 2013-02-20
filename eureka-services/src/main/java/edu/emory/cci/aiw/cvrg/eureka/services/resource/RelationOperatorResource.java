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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;

/**
 * @author hrathod
 */
@Path("/relationop")
@Produces(MediaType.APPLICATION_JSON)
public class RelationOperatorResource {

	private final RelationOperatorDao relationOperatorDao;

	@Inject
	public RelationOperatorResource (RelationOperatorDao
		inRelationOperatorDao) {
		this.relationOperatorDao = inRelationOperatorDao;
	}

	@GET
	@Path("/list")
	public List<RelationOperator> getAll () {
		return this.relationOperatorDao.getAll();
	}

	@GET
	@Path("/{id}")
	public RelationOperator get (@PathParam("id") Long inId) {
		return this.relationOperatorDao.retrieve(inId);
	}

	@GET
	@Path("/byname/{name}")
	public RelationOperator getByName (@PathParam("name") String inName) {
		return this.relationOperatorDao.getByName(inName);
	}
}
