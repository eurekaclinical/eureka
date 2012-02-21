package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.util.StringUtil;

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
	 * @param inFileDao The data access object to be used to work with file
	 *            upload objects in the data store.
	 * @param inApplicationProperties The access used to get the application
	 *            configuration properties.
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
	 * 
	 * @throws NoSuchAlgorithmException Thrown when the configuration can not be
	 *             sent to the ETL backend properly.
	 * @throws KeyManagementException Thrown when the configuration can not be
	 *             sent to the ETL backend properly.
	 */
	void configure() throws KeyManagementException, NoSuchAlgorithmException {
		addDefaultRoles();
		addDefaultUsers();
	}

	/**
	 * Add the default roles to the data store.
	 */
	private void addDefaultRoles() {
		for (Role role : Bootstrap.createRoles()) {
			this.roleDao.save(role);
		}
	}

	/**
	 * Add the default users to the data store.
	 * 
	 * @throws NoSuchAlgorithmException Propagated from createUsers().
	 */
	private void addDefaultUsers() throws NoSuchAlgorithmException {
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getRoles()) {
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}

		for (User user : this.createUsers(defaultRoles)) {
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
	 * @throws NoSuchAlgorithmException If the password can not be hashed
	 *             properly.
	 */
	private List<User> createUsers(final List<Role> defaultRoles)
			throws NoSuchAlgorithmException {
		List<User> users = new ArrayList<User>();

		List<Role> adminRoles = new ArrayList<Role>(defaultRoles);
		adminRoles.add(this.roleDao.getRoleByName("admin"));
		
		List<Role> superRoles = new ArrayList<Role>(adminRoles);
		superRoles.add(this.roleDao.getRoleByName("superuser"));

		User regularUser = new User();
		regularUser.setActive(true);
		regularUser.setVerified(true);
		regularUser.setEmail("user@emory.edu");
		regularUser.setFirstName("Regular");
		regularUser.setLastName("User");
		regularUser.setOrganization("Emory University");
		regularUser.setPassword(StringUtil.md5("testpassword"));
		regularUser.setLastLogin(Calendar.getInstance().getTime());
		regularUser.setRoles(defaultRoles);

		User adminUser = new User();
		adminUser.setActive(true);
		adminUser.setVerified(true);
		adminUser.setEmail("admin.user@emory.edu");
		adminUser.setFirstName("Admin");
		adminUser.setLastName("User");
		adminUser.setOrganization("Emory University");
		adminUser.setPassword(StringUtil.md5("testpassword"));
		adminUser.setLastLogin(Calendar.getInstance().getTime());
		adminUser.setRoles(adminRoles);
		
		User superUser = new User();
		superUser.setActive(true);
		superUser.setVerified(true);
		superUser.setEmail("super.user@emory.edu");
		superUser.setFirstName("Super");
		superUser.setLastName("User");
		superUser.setOrganization("Emory University");
		superUser.setPassword(StringUtil.md5("testpassword"));
		superUser.setLastLogin(Calendar.getInstance().getTime());
		superUser.setRoles(superRoles);

		users.add(regularUser);
		users.add(adminUser);
		users.add(superUser);
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
		
		Role role3 = new Role();
		role3.setDefaultRole(Boolean.FALSE);
		role3.setName("superuser");

		roles.add(role1);
		roles.add(role2);
		roles.add(role3);
		return roles;
	}
}
