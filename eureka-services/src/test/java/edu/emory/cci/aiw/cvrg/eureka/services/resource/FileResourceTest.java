package edu.emory.cci.aiw.cvrg.eureka.services.resource;

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
public class FileResourceTest extends AbstractResourceTest {

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
	public void testFileUpload() {
		List<User> users = this.getUserList();
		User user = users.get(0);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setLocation("/tmp/foo");
		fileUpload.setUser(user);

		WebResource resource = this.resource();
		ClientResponse response = resource.path("/api/file/upload").post(
				ClientResponse.class, fileUpload);
		Assert.assertEquals(Status.CREATED, response.getClientResponseStatus());
	}
}
