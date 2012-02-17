package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

/**
 * Sets up the environment for testing, by bootstrapping the data store with
 * certain items to test against.
 * 
 * @author hrathod
 * 
 */
public class Setup {

	/**
	 * The role DAO class.
	 */
	private final RoleDao roleDao;
	/**
	 * The user DAO class.
	 */
	private final UserDao userDao;
	/**
	 * The file upload DAO class.
	 */
	private final FileDao fileDao;

	/**
	 * Create a Bootstrap class with an EntityManager.
	 * 
	 * @param inRoleDao The data access object to be used to work with user
	 *            objects in the data store.
	 * @param inUserDao The data access object to be used to work with role
	 *            objects in the data store.
	 * @param inFileDao The data access object to be used to work with file
	 *            upload objects in the data store.
	 * 
	 * @param inEntityManager
	 */
	@Inject
	Setup(final RoleDao inRoleDao, final UserDao inUserDao,
			final FileDao inFileDao) {
		this.roleDao = inRoleDao;
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
	}

	/**
	 * The work method of the class, adds some roles and users to the data
	 * store.
	 * 
	 * @throws NoSuchAlgorithmException Thrown when the configuration can not be
	 *             sent to the ETL backend properly.
	 * @throws KeyManagementException Thrown when the configuration can not be
	 *             sent to the ETL backend properly.
	 */
	void configure() throws KeyManagementException, NoSuchAlgorithmException {
		addDefaultRoles();
		addDefaultUsers();
		addDefaultFileUploads();
	}

	/**
	 * Add the default roles to the data store.
	 */
	private void addDefaultRoles() {
		for (Role role : Setup.createRoles()) {
			this.roleDao.save(role);
		}
	}

	/**
	 * Add the default users to the data store.
	 * 
	 * @throws NoSuchAlgorithmException Propagated from the createUsers()
	 *             method.
	 */
	private void addDefaultUsers() throws NoSuchAlgorithmException {
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getRoles()) {
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}

		for (User user : Setup.createUsers(defaultRoles)) {
			this.userDao.save(user);
		}
	}

	/**
	 * Add the default file uploads to the data store, using the first user
	 * found.
	 */
	private void addDefaultFileUploads() {
		List<User> users = this.userDao.getUsers();
		User user = users.get(0);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("/tmp/foo");
		fileUpload.setUser(user);
		user.addFileUpload(fileUpload);
		this.userDao.save(user);
		this.fileDao.save(fileUpload);
	}

	/**
	 * Create the users to be added to the data store.
	 * 
	 * @param defaultRoles The default roles to be assigned to each newly
	 *            created user.
	 * @param fileUploads The file uploads to attach to the user.
	 * 
	 * @return List of users to be added.
	 * @throws NoSuchAlgorithmException Thrown when a password can not be hashed
	 *             properly.
	 */
	private static List<User> createUsers(final List<Role> defaultRoles)
			throws NoSuchAlgorithmException {
		List<User> users = new ArrayList<User>();
		User user = new User();
		user.setActive(true);
		user.setVerified(true);
		user.setSuperUser(true);
		user.setEmail("super.user@emory.edu");
		user.setFirstName("Super");
		user.setLastName("User");
		user.setOrganization("Emory University");
		user.setPassword(StringUtil.md5("testpassword"));
		user.setLastLogin(Calendar.getInstance().getTime());
		user.setRoles(defaultRoles);
		users.add(user);
		return users;
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
