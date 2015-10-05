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


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.*;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailException;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.util.UserRequestToUserEntityVisitor;

/**
 * RESTful end-point for new user registration-related methods.
 *
 * @author hrathod
 */
@Path("/userrequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRequestResource {

	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			UserRequestResource.class);
	/**
	 * Data access object to work with User objects.
	 */
	private final UserDao userDao;
	private final LocalUserDao localUserDao;
	/**
	 * Used to send emails to the user when needed.
	 */
	private final EmailSender emailSender;
	private final UserRequestToUserEntityVisitor visitor;

	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 *
	 * @param inUserDao DAO used to access {@link UserEntity} related
	 * functionality.
	 * @param inLocalUserDao DAO used to access {@link LocalUserEntity} related
	 * functionality.
	 * @param inRoleDao DAO used to access {@link Role} related functionality.
	 * @param inEmailSender Used to send emails to the user when necessary.
	 */
	@Inject
	public UserRequestResource(UserDao inUserDao, LocalUserDao inLocalUserDao,
			RoleDao inRoleDao,
			EmailSender inEmailSender,
			OAuthProviderDao inOAuthProviderDao,
			LoginTypeDao inLoginTypeDao,
			AuthenticationMethodDao inAuthenticationMethodDao) {
		this.userDao = inUserDao;
		this.localUserDao = inLocalUserDao;
		this.emailSender = inEmailSender;
		this.visitor = new UserRequestToUserEntityVisitor(inOAuthProviderDao,
				inRoleDao, inLoginTypeDao, inAuthenticationMethodDao);
	}

	/**
	 * Add a new user to the system.
	 *
	 * @param userRequest Object containing all the information about the user
	 * to add.
	 */
	@POST
	public void addUser(@Context HttpServletRequest inRequest,
			UserRequest userRequest) {
		UserEntity user =
				this.userDao.getByUsername(userRequest.getUsername());
		if (user != null) {
			throw new HttpStatusException(Response.Status.CONFLICT,
					"That username is already taken");
		}
		String[] errors = userRequest.validate();
		if (errors.length == 0) {
			userRequest.accept(visitor);
			UserEntity userEntity = visitor.getUserEntity();
			LOGGER.debug("Saving new user {}", userEntity.getUsername());
			this.userDao.create(userEntity);
			if (userEntity instanceof LocalUserEntity) {
				try {
					LOGGER.debug("Sending email to {}", userEntity.getEmail());
					this.emailSender.sendVerificationMessage(userEntity);
				} catch (EmailException e) {
					LOGGER.error("Error sending email to {}", userEntity.getEmail(), e);
				}
			}
		} else {
			String errorMsg = StringUtils.join(errors, ", ");
			LOGGER.info(
					"Invalid new user request: {}, reason {}", userRequest.getEmail(),
					errorMsg);
			throw new HttpStatusException(
					Response.Status.BAD_REQUEST, errorMsg);
		}
	}

	/**
	 * Mark a local user as verified.
	 *
	 * @param code The verification code to match against local users.
	 */
	@Path("/verify/{code}")
	@PUT
	public void verifyUser(@PathParam("code") final String code) {
		LocalUserEntity user = this.localUserDao.getByVerificationCode(code);
		if (user != null) {
			user.setVerified(true);
			this.localUserDao.update(user);
		} else {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
	}

}
