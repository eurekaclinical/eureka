package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * Tests related to the file upload resource.
 *
 * @author hrathod
 *
 */
public class FileResourceTest extends AbstractServiceResourceTest {

	/**
	 * Construct the object with the given file data access object.
	 */
	@Inject
	public FileResourceTest() {
		super();
	}

	/**
	 * Test if the file upload functionality works.
	 */
	@Test
	public final void testFileUpload() {
		List<User> users = this.getUserList();
		User user = users.get(0);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("/tmp/foo");
		fileUpload.setUser(user);
		fileUpload.setTimestamp(new Date());

		final WebResource resource = this.resource();
		final WebResource path = resource.path("/api/file/upload");
		ClientResponse response = path.post(
				ClientResponse.class, fileUpload);
		Assert.assertEquals(Status.CREATED, response.getClientResponseStatus());
	}
}
