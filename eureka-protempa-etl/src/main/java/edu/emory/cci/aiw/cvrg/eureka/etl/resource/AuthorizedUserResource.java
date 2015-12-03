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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.AuthorizedUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.AuthorizedUserDao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RESTful end-point for {@link UserEntity} related methods.
 *
 * @author hrathod
 */
@Path("/protected/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"admin"})
public class AuthorizedUserResource {

	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedUserResource.class);
	/**
	 * Data access object to work with User objects.
	 */
	private final AuthorizedUserDao userDao;
	
	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 *
	 * @param inUserDao DAO used to access {@link UserEntity} related functionality.
	 */
	@Inject
	public AuthorizedUserResource(AuthorizedUserDao inUserDao) {
		this.userDao = inUserDao;
	}

	/**
	 * Get a list of all users in the system.
	 *
	 * @return A list of {@link UserEntity} objects.
	 */
	@GET
	public List<AuthorizedUser> getUsers() {
		List<AuthorizedUserEntity> all = this.userDao.getAll();
		List<AuthorizedUser> result = new ArrayList<>(all.size());
		for (AuthorizedUserEntity user : all) {
			AuthorizedUser etlUser = new AuthorizedUser();
			etlUser.setId(user.getId());
			etlUser.setUsername(user.getUsername());
			result.add(etlUser);
		}
		return result;
	}

	/**
	 * Get a user by the user's identification number.
	 *
	 * @param inId The identification number for the user to fetch.
	 * @return The user referenced by the identification number.
	 */
	@Path("/{id}")
	@GET
	public AuthorizedUser getUserById(@PathParam("id") Long inId) {
		AuthorizedUserEntity userEntity = this.userDao.retrieve(inId);
		if (userEntity == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
		AuthorizedUser user = new AuthorizedUser();
		user.setId(userEntity.getId());
		user.setUsername(userEntity.getUsername());
		return user;
	}

	/**
	 * Add a new user to the system. The user is activated immediately.
	 *
	 * @param user Object containing all the information about the user
	 * to add.
	 */
	@POST
	public void create(AuthorizedUser user) {
		if (this.userDao.getByUsername(user.getUsername()) != null) {
			throw new HttpStatusException(Response.Status.CONFLICT);
		}
		AuthorizedUserEntity userEntity = new AuthorizedUserEntity();
		userEntity.setUsername(user.getUsername());
		this.userDao.create(userEntity);
	}

	/**
	 * Put an updated user to the system. Unless the user has the admin role,
	 * s/he may only update their own user info.
	 *
	 * @param inUser Object containing all the information about the user to
	 * add.
	 * @return A "Created" response with a link to the user page if successful.
	 */
	@PUT
	public void update(AuthorizedUserEntity inUser) {
		AuthorizedUserEntity userEntity = new AuthorizedUserEntity();
		userEntity.setId(inUser.getId());
		userEntity.setUsername(inUser.getUsername());
		this.userDao.update(inUser);
	}
}
