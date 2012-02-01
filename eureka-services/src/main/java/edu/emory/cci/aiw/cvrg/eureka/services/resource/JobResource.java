package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;

/**
 * REST operations related to jobs submitted by the user.
 * 
 * @author hrathod
 * 
 */
@Path("/job")
public class JobResource {

	/**
	 * The User data access object to retrievei information about the current
	 * user.
	 */
	private final UserDao userDao;

	/**
	 * Construct a new job resource with the given job update thread.
	 * 
	 * @param inUserDao The data access object used to fetch information about
	 *            users.
	 */
	@Inject
	public JobResource(UserDao inUserDao) {
		this.userDao = inUserDao;
	}

	/**
	 * Get a list of jobs associated with the logged-in user.
	 * 
	 * @param securityContext The context that provides the user principal
	 *            currently logged in.
	 * 
	 * @return A list of {@link Job} objects associated with the user.
	 * @throws ServletException Thrown if the user ID is not valid
	 */
	@Path("/list")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Job> getJobsByUser(@Context SecurityContext securityContext)
			throws ServletException {

		Principal userPrincipal = securityContext.getUserPrincipal();
		if (userPrincipal == null) {
			throw new ServletException("Bad User ID");
		}

		String name = userPrincipal.getName();
		User user = this.userDao.get(name);
		List<Job> allJobs = JobCollection.getJobs();
		List<Job> result = new ArrayList<Job>();
		for (Job job : allJobs) {
			if (job.getUserId() == user.getId()) {
				result.add(job);
			}
		}

		return result;
	}
}
