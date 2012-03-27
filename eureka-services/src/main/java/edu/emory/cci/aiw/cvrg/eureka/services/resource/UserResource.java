package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailException;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

/**
 * RESTful end-point for {@link User} related methods.
 * 
 * @author hrathod
 * 
 */
@Path("/user")
public class UserResource {

	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserResource.class);
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
	 * And validation errors that we may have encountered while validating a new
	 * user request, or a user update.
	 */
	private String validationError;

	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 * 
	 * @param inUserDao DAO used to access {@link User} related functionality.
	 * @param inRoleDao DAO used to access {@link Role} related functionality.
	 * @param inEmailSender Used to send emails to the user when necessary.
	 */
	@Inject
	public UserResource(UserDao inUserDao, RoleDao inRoleDao,
			EmailSender inEmailSender) {
		this.userDao = inUserDao;
		this.roleDao = inRoleDao;
		this.emailSender = inEmailSender;
	}

	/**
	 * Get a list of all users in the system.
	 * 
	 * @return A list of {@link User} objects.
	 */
	@Path("/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers() {
		List<User> users = this.userDao.getUsers();
		for (User user : users) {
			this.userDao.refresh(user);
		}
		LOGGER.debug("Returning list of users");
		return users;
	}

	/**
	 * Get a user by the user's identification number.
	 * 
	 * @param inId The identification number for the user to fetch.
	 * @return The user referenced by the identification number.
	 */
	@Path("/byid/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserById(@PathParam("id") Long inId) {
		User user = this.userDao.getById(inId);
		this.userDao.refresh(user);
		LOGGER.debug("Returning user for ID {}: {}", inId, user);
		return user;
	}

	/**
	 * Get a user using the username.
	 * 
	 * @param inName The of the user to fetch.
	 * @return The user corresponding to the given name.
	 */
	@Path("/byname/{name}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getUserByName(@PathParam("name") String inName) {
		User user = this.userDao.getByName(inName);
		this.userDao.refresh(user);
		LOGGER.debug("Returning user for name {}: {}", inName, user);
		return user;
	}

	/**
	 * Add a new user to the system.
	 * 
	 * @param userRequest Object containing all the information about the user
	 *            to add.
	 * @return A "Bad Request" error if the user does not pass validation, a
	 *         "Created" response with a link to the user page if successful.
	 * @throws ServletException Thrown when a password can not be properly
	 *             hashed.
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUser(final UserRequest userRequest)
			throws ServletException {

		try {
			String temp = StringUtil.md5(userRequest.getPassword());
			String temp2 = StringUtil.md5(userRequest.getVerifyPassword());
			userRequest.setPassword(temp);
			userRequest.setVerifyPassword(temp2);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error(e1.getMessage(), e1);
			throw new ServletException(e1);
		}

		Response response;
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
			this.userDao.save(user);
			try {
				LOGGER.debug("Sending email to {}", user.getEmail());
				this.emailSender.sendVerificationMessage(user);
			} catch (EmailException e) {
				LOGGER.error("Error sending email to {}", user.getEmail(), e);
			}
			response = Response.created(URI.create("/" + user.getId())).build();
		} else {
			LOGGER.info("Invalid new user request: {}, reason {}", userRequest,
					this.validationError);
			response = Response.status(Status.BAD_REQUEST)
					.entity(this.validationError).build();
		}
		return response;
	}

	/**
	 * Change a user's password.
	 * 
	 * @param inId The unique identifier for the user.
	 * @param oldPassword The old password for the user.
	 * @param newPassword The new password for the user.
	 * @return {@link Status#OK} if the password update is successful,
	 *         {@link Status#BAD_REQUEST} if the current password does not
	 *         match, or if the user does not exist.
	 * @throws ServletException Thrown when a password cannot be properly
	 *             hashed.
	 */
	@Path("/passwd/{id}")
	@GET
	public Response changePassword(@PathParam("id") final Long inId,
			@QueryParam("oldPassword") final String oldPassword,
			@QueryParam("newPassword") final String newPassword)
			throws ServletException {

		Response response;
		User user = this.userDao.getById(inId);
		String oldPasswordHash;
		String newPasswordHash;
		try {
			oldPasswordHash = StringUtil.md5(oldPassword);
			newPasswordHash = StringUtil.md5(newPassword);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServletException(e);
		}
		if (user.getPassword().equals(oldPasswordHash)) {
			user.setPassword(newPasswordHash);
			this.userDao.save(user);
			try {
				this.emailSender.sendPasswordChangeMessage(user);
			} catch (EmailException ee) {
				LOGGER.error(ee.getMessage(), ee);
			}
			response = Response.ok().build();
		} else {
			response = Response.status(Status.BAD_REQUEST)
					.entity("Password mismatch").build();
		}
		return response;
	}

	/**
	 * Put an updated user to the system.
	 * 
	 * @param inUser Object containing all the information about the user to
	 *            add.
	 * @return A "Created" response with a link to the user page if successful.
	 * 
	 */
	@Path("/put")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response putUser(final User inUser) {
		LOGGER.debug("Received updated user: {}", inUser);
		Response response;
		User currentUser = this.userDao.getById(inUser.getId());
		this.userDao.refresh(currentUser);
		List<Role> roles = inUser.getRoles();
		List<Role> updatedRoles = new ArrayList<Role>();
		for (Role r : roles) {
			Role updatedRole = this.roleDao.getRoleById(r.getId());
			updatedRoles.add(updatedRole);
		}

		currentUser.setRoles(updatedRoles);
		currentUser.setActive(inUser.isActive());
		currentUser.setLastLogin(inUser.getLastLogin());

		if (this.validateUpdatedUser(currentUser)) {
			LOGGER.debug("Saving updated user: {}", currentUser);
			this.userDao.save(currentUser);

			boolean activation = (!currentUser.isActive())
					&& (inUser.isActive());
			if (activation) {
				try {
					this.emailSender.sendActivationMessage(currentUser);
				} catch (EmailException ee) {
					LOGGER.error(ee.getMessage(), ee);
				}
			}
			response = Response.created(URI.create("/" + currentUser.getId()))
					.build();
		} else {
			response = Response.notModified(this.validationError).build();
		}
		return response;
	}

	/**
	 * Mark a user as verified.
	 * 
	 * @param code The verification code to match against users.
	 * @return An HTTP OK response if the user is modified, or a server error if
	 *         the user can not be modified properly.
	 */
	@Path("/verify/{code}")
	@PUT
	public Response verifyUser(@PathParam("code") final String code) {
		Response response = Response.ok().build();
		User user = this.userDao.getByVerificationCode(code);
		if (user != null) {
			user.setVerified(true);
			this.userDao.save(user);
		} else {
			response = Response.notModified("Invalid user").build();
		}
		return response;
	}

	/**
	 * Mark a user as active.
	 * 
	 * @param inId the unique identifier of the user to be made active.
	 * @return An HTTP OK response if the modification is completed normall, or
	 *         a server error if the user cannot be modified properly.
	 */
	@Path("/activate/{id}")
	@PUT
	public Response activateUser(@PathParam("id") final Long inId) {
		Response response = Response.ok().build();
		User user = this.userDao.getById(inId);
		if (user != null) {
			user.setActive(true);
			this.userDao.save(user);
		} else {
			response = Response.status(Status.BAD_REQUEST).entity("Invalid ID")
					.build();
		}
		return response;
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
			if ((userRequest.getEmail() == null)
					|| (userRequest.getVerifyEmail() == null)
					|| (!userRequest.getEmail().equals(
							userRequest.getVerifyEmail()))) {
				this.validationError = "Mismatched usernames";
				result = false;
			}

			// make sure the passwords are not null, and match each other
			if ((userRequest.getPassword() == null)
					|| (userRequest.getVerifyPassword() == null)
					|| (!userRequest.getPassword().equals(
							userRequest.getVerifyPassword()))) {
				this.validationError = "Mismatched passwords";
				result = false;
			}
		} else {
			this.validationError = "Username already exists";
			result = false;
		}

		return result;
	}

	/**
	 * Validate that a user being updated does not violate any rules.
	 * 
	 * @param updatedUser The user to be validate.
	 * @return True if the user is valid, false otherwise.
	 */
	private boolean validateUpdatedUser(User updatedUser) {
		boolean result = true;

		// get the user as they currently exist in the data store.
		User currentUser = this.userDao.getById(updatedUser.getId());
		List<Role> currentRoles = currentUser.getRoles();

		// the roles to check
		Role superUserRole = this.roleDao.getRoleByName("superuser");
		Role adminRole = this.roleDao.getRoleByName("admin");

		// a super user can not be stripped of admin rights, or be de-activated.
		if (currentRoles.contains(superUserRole)) {
			if (!updatedUser.isActive()) {
				this.validationError = "Superuser can not be de-activated";
				result = false;
			}
			if (!updatedUser.getRoles().contains(adminRole)) {
				this.validationError = "Superuser can not lose admin rights";
				result = false;
			}
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
		for (Role role : this.roleDao.getRoles()) {
			if (Boolean.TRUE.equals(role.isDefaultRole())) {
				defaultRoles.add(role);
			}
		}
		return defaultRoles;
	}
}
