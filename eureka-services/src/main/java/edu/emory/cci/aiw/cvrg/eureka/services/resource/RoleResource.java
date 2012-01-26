package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;

/**
 * A RESTful end-point for working with {@link Role} objects.
 * 
 * @author hrathod
 * 
 */
@Path("/role")
public class RoleResource {
	/**
	 * The data access object used to work with Role objects in the data store.
	 */
	private final RoleDao roleDao;

	/**
	 * Create a RoleResource object with the given {@link RoleDao}
	 * 
	 * @param inRoleDao The RoleDao object used to work with role objects in the
	 *            data store.
	 */
	@Inject
	public RoleResource(RoleDao inRoleDao) {
		this.roleDao = inRoleDao;
	}

	/**
	 * Get a list of all the roles available in the system.
	 * 
	 * @return A list of {@link Role} objects.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public List<Role> getRoles() {
		List<Role> roles = this.roleDao.getRoles();
		return roles;
	}
}
