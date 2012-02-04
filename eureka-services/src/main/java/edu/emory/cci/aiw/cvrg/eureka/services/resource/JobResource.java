package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobSubmissionThread;

/**
 * REST operations related to jobs submitted by the user.
 * 
 * @author hrathod
 * 
 */
@Path("/job")
public class JobResource {

	/**
	 * The User data access object to retrieve information about the current
	 * user.
	 */
	private final UserDao userDao;
	/**
	 * The data access object used to work with file upload objects in the data
	 * store.
	 */
	private final FileDao fileDao;

	/**
	 * Construct a new job resource with the given job update thread.
	 * 
	 * @param inUserDao The data access object used to fetch information about
	 *            users.
	 * @param inFileDao The data access object used to fetch and store
	 *            information about uploaded files.
	 */
	@Inject
	public JobResource(UserDao inUserDao, FileDao inFileDao) {
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
	}

	/**
	 * Create a new job (by uploading a new file)
	 * 
	 * @param fileUpload The file upload to add.
	 * @param servletContext The servlet context, used for fetching context
	 *            parameters out of the web.xml configuration file.
	 * @return A {@link Status#OK} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 * @throws ServletException
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FileUpload fileUpload,
			@Context ServletContext servletContext) throws ServletException {
		String configUrl = servletContext
				.getInitParameter("backend-config-url");
		String jobSubmitUrl = servletContext
				.getInitParameter("backend-submission-url");

		configUrl += "/" + fileUpload.getUser().getId();

		try {
			JobSubmissionThread jobSubmissionThread = new JobSubmissionThread(
					fileUpload, this.fileDao, configUrl, jobSubmitUrl);
			jobSubmissionThread.start();
		} catch (KeyManagementException e) {
			throw new ServletException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
		return Response.ok().build();
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
