package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.stanford.smi.protege.util.Assert;

/**
 * @author hrathod
 */
public class PropositionResourceTest extends AbstractEtlResourceTest {

	private static final Long USER_ID = Long.valueOf(1);

	@Test
	public void testValidation() throws Exception {
		WebResource webResource = this.resource();
		List<PropositionWrapper> propositions =
			new ArrayList<PropositionWrapper>();

		ValidationRequest validationRequest = new ValidationRequest();
		validationRequest.setUserId(USER_ID);
		validationRequest.setPropositions(propositions);

		ClientResponse response =
			webResource.path("/api/proposition/validate").type(
				MediaType.APPLICATION_JSON).post(ClientResponse.class,
				validationRequest);

		Assert.assertEquals(ClientResponse.Status.OK,
			response.getClientResponseStatus());
	}
}
