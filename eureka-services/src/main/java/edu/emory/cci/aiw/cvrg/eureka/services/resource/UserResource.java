package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * RESTful end-point for {@link User} related methods.
 * 
 * @author hrathod
 * 
 */
@Path("/user")
public class UserResource {

	/**
	 * Data access object to work with User objects.
	 */
	private final UserDao userDao;

	/**
	 * Data access object to work with Role objects.
	 */
	private final RoleDao roleDao;

	/**
	 * Create a UserResource object with a User DAO and a Role DAO.
	 * 
	 * @param inUserDao DAO used to access {@link User} related functionality.
	 * @param inRoleDao DAO used to access {@link Role} related functionality.
	 */
	@Inject
	public UserResource(UserDao inUserDao, RoleDao inRoleDao) {
		this.userDao = inUserDao;
		this.roleDao = inRoleDao;
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
		return this.userDao.getUsers();
	}

	/**
	 * Get a user by the user's identification number.
	 * 
	 * @param inId The identification number for the user to fetch.
	 * @return The user referenced by the identification number.
	 * @throws ServletException Thrown if the identification number string can
	 *             not be properly converted to a {@link Long}.
	 */
	@Path("/byid/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserById(@PathParam("id") String inId)
			throws ServletException {
		User user;
		try {
			Long id = Long.valueOf(inId);
			user = this.userDao.get(id);
		} catch (NumberFormatException nfe) {
			throw new ServletException(nfe);
		}
		return user;
	}

	/**
	 * Get a user using the username.
	 * 
	 * @param inName The of the user to fetch.
	 * @return The user corresponding to the given name.
	 * @throws ServletException Thrown if the given name does not match any user
	 *             in the system.
	 */
	@Path("/byname/{name}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getUserByName(@PathParam("name") String inName)
			throws ServletException {
		User user = this.userDao.get(inName);
		if (user == null) {
			throw new ServletException("Invalid user name");
		}
		return user;
	}

	/**
	 * Add a new user to the system.
	 * 
	 * @param userRequest Object containing all the information about the user
	 *            to add.
	 * @return A "Bad Request" error if the user does not pass validation, a
	 *         "Created" response with a link to the user page if successful.
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response addUser(final UserRequest userRequest) {
		Response response = null;
		if (validateUserRequest(userRequest)) {
			User user = new User();
			user.setEmail(userRequest.getEmail());
			user.setFirstName(userRequest.getFirstName());
			user.setLastName(userRequest.getLastName());
			user.setOrganization(userRequest.getOrganization());
			user.setPassword(userRequest.getPassword());
			user.setRoles(this.getDefaultRoles());
			this.userDao.save(user);
			response = Response.created(URI.create("/" + user.getId())).build();
		} else {
			response = Response.status(Status.BAD_REQUEST)
					.entity("Invalid user request.").build();
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
	 */
	@Path("/passwd/{id}")
	@GET
	public Response changePassword(@PathParam("id") final String inId,
			@QueryParam("oldPassword") final String oldPassword,
			@QueryParam("newPassword") final String newPassword) {

		Response response;
		try {
			Long userId = Long.valueOf(inId);
			User user = this.userDao.get(userId);
			if (user.getPassword().equals(oldPassword)) {
				user.setPassword(newPassword);
				this.userDao.save(user);
				response = Response.ok().build();
			} else {
				response = Response.status(Status.BAD_REQUEST)
						.entity("Password mismatch").build();
			}
		} catch (NumberFormatException nfe) {
			response = Response.status(Status.BAD_REQUEST)
					.entity("Invalid User ID").build();
		}
		return response;
	}

	/**
	 * Put an updated user to the system.
	 * 
	 * @param user Object containing all the information about the user to add.
	 * @return A "Created" response with a link to the user page if successful.
	 * 
	 */
	@Path("/put")
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response putUser(final User user) {
		Response response = null;
		User updateUser = this.userDao.get(user.getId());
		List<Role> roles = user.getRoles();
		List<Role> updatedRoles = new ArrayList<Role>();
		for (Role r : roles) {
			Role updatedRole = this.roleDao.getRoleById(r.getId());
			updatedRoles.add(updatedRole);
		}

		updateUser.setRoles(updatedRoles);
		updateUser.setActive(user.isActive());

		if (this.validateUpdatedUser(updateUser)) {
			this.userDao.save(updateUser);
			response = Response.created(URI.create("/" + updateUser.getId()))
					.build();
		} else {
			response = Response.notModified("Invalid user").build();
		}
		return response;
	}

	/**
	 * Mark a user as verified.
	 * 
	 * @param inId The unique identifier for the user to be verified.
	 * @return An HTTP OK response if the user is modified, or a server error if
	 *         the user can not be modified properly.
	 */
	@Path("/verify/{id}")
	@PUT
	public Response verifyUser(@PathParam("id") final String inId) {
		Response response = Response.ok().build();
		Long id = null;

		// try to convert the ID string to a numeric value.
		try {
			id = Long.valueOf(inId);
		} catch (NumberFormatException nfe) {
			response = Response.status(Status.BAD_REQUEST).entity("Invalid ID")
					.build();
		}

		if (id != null) {
			User user = this.userDao.get(id);
			user.setVerified(true);
			this.userDao.save(user);
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
	public Response activateUser(@PathParam("id") final String inId) {
		Response response = Response.ok().build();
		Long id = null;

		// try to convert the ID string to a numeric value.
		try {
			id = Long.valueOf(inId);
		} catch (NumberFormatException nfe) {
			response = Response.status(Status.BAD_REQUEST).entity("Invalid ID")
					.build();
		}

		if (id != null) {
			User user = this.userDao.get(id);
			user.setActive(true);
			this.userDao.save(user);
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
	private static boolean validateUserRequest(UserRequest userRequest) {
		boolean result = true;

		// make sure the email addresses are not null, and match each other
		if ((userRequest.getEmail() == null)
				|| (userRequest.getVerifyEmail() == null)
				|| (!userRequest.getEmail()
						.equals(userRequest.getVerifyEmail()))) {
			result = false;
		}

		// make sure the passwords are not null, and match each other
		if ((userRequest.getPassword() == null)
				|| (userRequest.getVerifyPassword() == null)
				|| (!userRequest.getPassword().equals(
						userRequest.getVerifyPassword()))) {
			result = false;
		}

		return result;
	}

	/**
	 * Validate that a user being updated does not violate any rules.
	 * 
	 * @param user The user to be validate.
	 * @return True if the user is valid, false otherwise.
	 */
	private boolean validateUpdatedUser(User user) {
		boolean result = true;
		// a super user can not be de-activated or stripped of admin rights
		if (user.isSuperUser()) {
			if (user.isActive() == false) {
				result = false;
			}
			Role adminRole = this.roleDao.getRoleByName("admin");
			if (!user.getRoles().contains(adminRole)) {
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
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}
		return defaultRoles;
	}
}
