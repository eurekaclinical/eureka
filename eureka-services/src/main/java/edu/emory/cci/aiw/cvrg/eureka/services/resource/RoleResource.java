/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import javax.annotation.security.RolesAllowed;

/**
 * A RESTful end-point for working with {@link Role} objects.
 *
 * @author hrathod
 *
 */
@Path("/protected/role")
@RolesAllowed({"admin"})
public class RoleResource {
	/**
	 * The data access object used to work with Role objects in the data store.
	 */
	private final RoleDao roleDao;

	/**
	 * Create a RoleResource object with the given {@link RoleDao}
	 *
	 * @param inRoleDao The RoleDao object used to work with role objects in the
	 *            data store.
	 */
	@Inject
	public RoleResource(RoleDao inRoleDao) {
		this.roleDao = inRoleDao;
	}

	/**
	 * Get a role by the role's identification number.
	 *
	 * @param inId The identification number for the role to fetch.
	 * @return The role referenced by the identification number.
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Role getRole(@PathParam("id") Long inId) {
		return this.roleDao.retrieve(inId);
	}

	/**
	 * Get a list of all the roles available in the system.
	 *
	 * @return A list of {@link Role} objects.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public List<Role> getRoles() {
		return this.roleDao.getAll();
	}
}
