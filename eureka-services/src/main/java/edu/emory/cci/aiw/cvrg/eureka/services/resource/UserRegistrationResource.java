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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailException;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

/**
 * RESTful end-point for new user registration-related methods.
 *
 * @author hrathod
 */
@Path("/userreg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRegistrationResource {

	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			UserRegistrationResource.class);
	/**
	 * Data access object to work with User objects.
	 */
	private final UserDao userDao;
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
	/**
	 * And validation errors that we may have encountered while validating a new
	 * user request, or a user update.
	 */
	private String validationError;
	private final ServiceProperties serviceProperties;

	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 *
	 * @param inUserDao DAO used to access {@link User} related functionality.
	 * @param inRoleDao DAO used to access {@link Role} related functionality.
	 * @param inEmailSender Used to send emails to the user when necessary.
	 */
	@Inject
	public UserRegistrationResource(UserDao inUserDao, RoleDao inRoleDao,
			EmailSender inEmailSender, I2b2Client inClient,
			PasswordGenerator inPasswordGenerator,
			ServiceProperties serviceProperties) {
		this.userDao = inUserDao;
		this.roleDao = inRoleDao;
		this.emailSender = inEmailSender;
		this.i2b2Client = inClient;
		this.passwordGenerator = inPasswordGenerator;
		this.serviceProperties = serviceProperties;
	}

	/**
	 * Add a new user to the system.
	 *
	 * @param userRequest Object containing all the information about the user
	 * to add.
	 */
	@Path("/new")
	@POST
	public void addUser(final UserRequest userRequest) {
		try {
			String temp = StringUtil.md5(userRequest.getPassword());
			String temp2 = StringUtil.md5(userRequest.getVerifyPassword());
			userRequest.setPassword(temp);
			userRequest.setVerifyPassword(temp2);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error(e1.getMessage(), e1);
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, e1);
		}

		if (validateUserRequest(userRequest)) {
			User user = new User();
			user.setEmail(userRequest.getEmail());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setOrganization(userRequest.getOrganization());
			user.setRoles(this.getDefaultRoles());
			user.setPassword(userRequest.getPassword());
			user.setVerificationCode(UUID.randomUUID().toString());
			LOGGER.debug("Saving new user {}", user.getEmail());
			this.userDao.create(user);
			try {
				LOGGER.debug("Sending email to {}", user.getEmail());
				this.emailSender.sendVerificationMessage(user);
			} catch (EmailException e) {
				LOGGER.error("Error sending email to {}", user.getEmail(), e);
			}
		} else {
			LOGGER.info(
					"Invalid new user request: {}, reason {}", userRequest,
					this.validationError);
			throw new HttpStatusException(
					Response.Status.PRECONDITION_FAILED, this.validationError);
		}
	}

	/**
	 * Mark a user as verified.
	 *
	 * @param code The verification code to match against users.
	 */
	@Path("/verify/{code}")
	@PUT
	public void verifyUser(@PathParam("code") final String code) {
		User user = this.userDao.getByVerificationCode(code);
		if (user != null) {
			user.setVerified(true);
			this.userDao.update(user);
		} else {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		}
	}

	/**
	 * Reset the given user's password. The password is randomly generated.
	 *
	 * @param inUsername The username for the user whose password should be
	 * reset.
	 * @throws HttpStatusException if the password can not be reset properly.
	 */
	@Path("/resetpassword/{username}")
	@PUT
	public void resetPassword(@PathParam("username") final String inUsername) {
		User user = this.userDao.getByName(inUsername);
		LOGGER.debug("Resetting user: {}", user);
		if (user == null) {
			throw new HttpStatusException(
					Response.Status.NOT_FOUND, 
					"We could not find this email address in our records."
					+ " Please check the email address or contact " + 
					this.serviceProperties.getSupportEmail() + " for help.");

		} else {
			String passwordHash;
			String password = this.passwordGenerator.generatePassword();
			try {
				passwordHash = StringUtil.md5(password);
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage(), e);
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, e);
			}

			user.setPassword(passwordHash);
			user.setPasswordExpiration(Calendar.getInstance().getTime());
			this.i2b2Client.changePassword(user.getEmail(), password);
			this.userDao.update(user);
			try {
				this.emailSender.sendPasswordResetMessage(user, password);
			} catch (EmailException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		LOGGER.debug("Reset user to: {}", user);
	}

	/**
	 * Validate a {@link UserRequest} object. Two rules are implemented: 1) The
	 * email addresses in the two email fields must match, and 2) The passwords
	 * in the two password fields must match.
	 *
	 * @param userRequest The {@link UserRequest} object to validate.
	 * @return True if the request is valid, and false otherwise.
	 */
	private boolean validateUserRequest(UserRequest userRequest) {
		boolean result = true;

		// make sure a user with the same email address does not exist.
		User user = this.userDao.getByName(userRequest.getEmail());
		if (user == null) {
			// make sure the email addresses are not null, and match each other
			if ((userRequest.getEmail() == null) || (userRequest
					.getVerifyEmail() == null) || (!userRequest.getEmail()
					.equals(
					userRequest.getVerifyEmail()))) {
				this.validationError = "Mismatched usernames";
				result = false;
			}

			// make sure the passwords are not null, and match each other
			if ((userRequest.getPassword() == null) || (userRequest
					.getVerifyPassword() == null) || (!userRequest.getPassword().equals(
					userRequest.getVerifyPassword()))) {
				this.validationError = "Mismatched passwords";
				result = false;
			}
		} else {
			this.validationError = "Email address already exists";
			result = false;
		}

		return result;
	}

	/**
	 * Get a set of default roles to be added to a newly created user.
	 *
	 * @return A list of default roles.
	 */
	private List<Role> getDefaultRoles() {
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getAll()) {
			if (Boolean.TRUE.equals(role.isDefaultRole())) {
				defaultRoles.add(role);
			}
		}
		return defaultRoles;
	}
}
