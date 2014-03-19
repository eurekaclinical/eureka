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


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.services.util.UserRequestToUserEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LocalUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.OAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailException;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import org.apache.commons.lang3.StringUtils;

/**
 * RESTful end-point for new user registration-related methods.
 *
 * @author hrathod
 */
@Path("/userrequest")
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
	 * Data access object to work with Role objects.
	 */
	private final RoleDao roleDao;
	/**
	 * Used to send emails to the user when needed.
	 */
	private final EmailSender emailSender;
	/**
	 * An i2b2 client to change the password remotely.
	 */
	private final I2b2Client i2b2Client;
	/**
	 * Used to generate random passwords for the reset password functionality.
	 */
	private final PasswordGenerator passwordGenerator;
	private final ServiceProperties serviceProperties;
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
			EmailSender inEmailSender, I2b2Client inClient,
			PasswordGenerator inPasswordGenerator,
			ServiceProperties serviceProperties,
			OAuthProviderDao inOAuthProviderDao) {
		this.userDao = inUserDao;
		this.localUserDao = inLocalUserDao;
		this.roleDao = inRoleDao;
		this.emailSender = inEmailSender;
		this.i2b2Client = inClient;
		this.passwordGenerator = inPasswordGenerator;
		this.serviceProperties = serviceProperties;
		this.visitor = new UserRequestToUserEntityVisitor(inOAuthProviderDao, inRoleDao);
	}

	/**
	 * Add a new user to the system.
	 *
	 * @param userRequest Object containing all the information about the user
	 * to add.
	 */
	@Path("/new")
	@POST
	public void addUser(@Context HttpServletRequest inRequest,
			UserRequest userRequest) {
		if (inRequest.getRemoteUser() != null) {
			if (!inRequest.getRemoteUser().equals(userRequest.getUsername())) {
				throw new HttpStatusException(Response.Status.BAD_REQUEST, 
						"Cannot request a user other than yourself");
			} else if (userRequest instanceof LocalUserRequest) {
				throw new HttpStatusException(Response.Status.BAD_REQUEST, 
						"Cannot request a local user while already logged in");
			}
		} else {
			if (!(userRequest instanceof LocalUserRequest)) {
				throw new HttpStatusException(Response.Status.BAD_REQUEST, 
						"Only local user requests are permitted if you are not already authenticated");
			}
		}
		UserEntity user = this.userDao.getByName(userRequest.getUsername());
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
					"Invalid new user request: {}, reason {}", userRequest,
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
