package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
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
	 * The file upload DAO class.
	 */
	private final FileDao fileDao;
	/**
	 * The application level configuration object.
	 */
	private final ApplicationProperties applicationProperties;

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
	Bootstrap(final RoleDao inRoleDao, final UserDao inUserDao,
			final FileDao inFileDao,
			ApplicationProperties inApplicationProperties) {
		this.roleDao = inRoleDao;
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
		this.applicationProperties = inApplicationProperties;
	}

	/**
	 * The work method of the class, adds some roles and users to the data
	 * store.
	 * @throws NoSuchAlgorithmException Thrown when the configuration can not be sent to the ETL backend properly.
	 * @throws KeyManagementException Thrown when the configuration can not be sent to the ETL backend properly.
	 */
	void configure() throws KeyManagementException, NoSuchAlgorithmException {
		addDefaultRoles();
		addDefaultUsers();
		addDefaultFileUploads();
		sendConfiguration();
	}

	/**
	 * Bootstrap the back-end with a fake configuration.
	 * 
	 * @throws NoSuchAlgorithmException Thrown when the client can not be
	 *             created properly.
	 * @throws KeyManagementException Thrown whent the client can not be created
	 *             properly.
	 */
	private void sendConfiguration() throws KeyManagementException,
			NoSuchAlgorithmException {
		Client client = CommUtils.getClient();
		User user = this.userDao.getUsers().get(0);
		Configuration fakeConfiguration = new Configuration();
		fakeConfiguration.setProtempaDatabaseName("XE");
		fakeConfiguration.setProtempaHost("adrastea.cci.emory.edu");
		fakeConfiguration.setProtempaPort(Integer.valueOf(1521));
		fakeConfiguration.setProtempaSchema("cvrg");
		fakeConfiguration.setProtempaPass("cvrg");
		fakeConfiguration.setUserId(user.getId());

		ClientResponse response = client
				.resource(this.applicationProperties.getEtlConfSubmitUrl())
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, fakeConfiguration);
		System.out.println("CONFIGURATION SUBMISSION: "
				+ response.getClientResponseStatus());
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
	 */
	private void addDefaultUsers() {
		List<Role> defaultRoles = new ArrayList<Role>();
		for (Role role : this.roleDao.getRoles()) {
			if (role.isDefaultRole() == Boolean.TRUE) {
				defaultRoles.add(role);
			}
		}

		for (User user : Bootstrap.createUsers(defaultRoles)) {
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
	 */
	private static List<User> createUsers(final List<Role> defaultRoles) {
		List<User> users = new ArrayList<User>();
		User user = new User();
		user.setActive(true);
		user.setVerified(true);
		user.setSuperUser(true);
		user.setEmail("super.user@emory.edu");
		user.setFirstName("Super");
		user.setLastName("User");
		user.setOrganization("Emory University");
		user.setPassword("testpassword");
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
