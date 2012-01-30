package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * To be used for Demo purposes only. Bootstraps the data store with some test
 * items so the integration between the web application and the services layer
 * can be tested more fully.
 * 
 * @author hrathod
 * 
 */
public class Bootstrap {

	/**
	 * The role DAO class.
	 */
	private final RoleDao roleDao;
	/**
	 * The user DAO class.
	 */
	private final UserDao userDao;

	/**
	 * Create a Bootstrap class with an EntityManager.
	 * 
	 * @param inRoleDao The data access object to be used to work with user
	 *            objects in the data store.
	 * @param inUserDao The data access object to be used to work with role
	 *            objects in the data store.
	 * 
	 * @param inEntityManager
	 */
	@Inject
	Bootstrap(final RoleDao inRoleDao, final UserDao inUserDao) {
		this.roleDao = inRoleDao;
		this.userDao = inUserDao;
	}

	/**
	 * The work method of the class, adds some roles and users to the data
	 * store.
	 */
	void configure() {
		addDefaultRoles();
		addDefaultUsers();
	}

	/**
	 * Add the default roles to the data store.
	 */
	void addDefaultRoles() {
		for (Role role : Bootstrap.createRoles()) {
			this.roleDao.save(role);
		}
	}

	/**
	 * Add the default users to the data store.
	 */
	void addDefaultUsers() {
		List<FileUpload> fileUploads = createFileUploads();
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getRoles()) {
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}

		for (User user : Bootstrap.createUsers(defaultRoles, fileUploads)) {
			this.userDao.save(user);
		}
	}

	/**
	 * Create the users to be added to the data store.
	 * 
	 * @param defaultRoles The default roles to be assigned to each newly
	 *            created user.
	 * @param fileUploads The file uploads to attach to the user.
	 * 
	 * @return List of users to be added.
	 */
	private static List<User> createUsers(final List<Role> defaultRoles,
			final List<FileUpload> fileUploads) {
		List<User> users = new ArrayList<User>();
		User user = new User();
		user.setActive(true);
		user.setVerified(true);
		user.setEmail("test.user@emory.edu");
		user.setFirstName("Test");
		user.setLastName("User");
		user.setOrganization("Emory University");
		user.setPassword("testpassword");
		user.setLastLogin(Calendar.getInstance().getTime());
		user.setRoles(defaultRoles);
		user.setFileUploads(fileUploads);
		users.add(user);
		return users;
	}

	/**
	 * Create the file uploads for the user
	 * 
	 * @return The list of file uploads
	 */
	private static List<FileUpload> createFileUploads() {
		List<FileUpload> fileUploads = new ArrayList<FileUpload>();
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("/tmp/foo");
		fileUploads.add(fileUpload);
		return fileUploads;
	}

	/**
	 * Create the roles to be added to the data store.
	 * 
	 * @return List of roles to be added.
	 */
	private static List<Role> createRoles() {
		List<Role> roles = new ArrayList<Role>();

		Role role1 = new Role();
		role1.setDefaultRole(Boolean.TRUE);
		role1.setName("researcher");

		Role role2 = new Role();
		role2.setDefaultRole(Boolean.FALSE);
		role2.setName("admin");

		roles.add(role1);
		roles.add(role2);
		return roles;
	}
}
