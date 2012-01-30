package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * Operations related to a data file (upload, status, etc)
 * 
 * @author hrathod
 * 
 */
@Path("/file")
public class FileResource {

	/**
	 * The data access object used to work with file upload objects in the data
	 * store.
	 */
	private final FileDao fileDao;

	/**
	 * The data access object used to work with user objects in the data store.
	 */
	private final UserDao userDao;

	/**
	 * Create an object with the give data access object.
	 * 
	 * @param inFileDao The data access object used to communicate with
	 *            {@link FileDao} objects in the data store.
	 * @param inUserDao The data access object used to fetch information about
	 *            {@link User} objects in the data store.
	 */
	@Inject
	public FileResource(FileDao inFileDao, UserDao inUserDao) {
		this.fileDao = inFileDao;
		this.userDao = inUserDao;
	}

	/**
	 * Add a new uploaded file.
	 * 
	 * @param fileUpload The file upload to add.
	 * @return A {@link Status#CREATED} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 */
	@Path("/upload")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FileUpload fileUpload) {
		User user = this.userDao.get(fileUpload.getUser().getId());
		fileUpload.setUser(user);
		this.fileDao.save(fileUpload);
		Response response = Response.created(
				URI.create("/" + fileUpload.getId())).build();
		return response;
	}

	/**
	 * Get an uploaded file referred to by the given unique identifier.
	 * 
	 * @param inId The unique identifier for the file to be fetched.
	 * @return The uploaded file information
	 * @throws ServletException Thrown if the given unique identifier is
	 *             invalid.
	 */
	@Path("/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public FileUpload getFile(@PathParam("id") final String inId)
			throws ServletException {
		Long id;
		try {
			id = Long.valueOf(inId);
		} catch (NumberFormatException nfe) {
			throw new ServletException(nfe);
		}

		FileUpload fileUpload = this.fileDao.get(id);
		if (fileUpload == null) {
			throw new ServletException("Invalid ID");
		}
		return fileUpload;
	}
}
