package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

import junit.framework.Assert;

public class PropositionResourceTest extends AbstractServiceResourceTest {

	private static final Long USER_ID = 1L;
	private static final GenericType<List<PropositionWrapper>>
		wrapperListType = new GenericType<List<PropositionWrapper>>(){};

	private List<PropositionWrapper> getUserPropositions(Long inUserId) {
		WebResource resource = this.resource();
		return resource.path("/api/proposition/user/list/" + inUserId).accept
			(MediaType.APPLICATION_JSON).get(wrapperListType);
	}

	@Test
	public void userPropositionTest() {
		List<PropositionWrapper> wrappers = this.getUserPropositions(USER_ID);
		Assert.assertTrue(wrappers.size() > 0);
	}

	@Test
	public void deletePropositionTest () {
		List<PropositionWrapper> wrappers = this.getUserPropositions(USER_ID);
		PropositionWrapper target = wrappers.get(0);
		ClientResponse response = this.resource().path
			("/api/proposition/user/delete/" + USER_ID + "/" + target.getId()
			).delete(ClientResponse.class);
		Assert.assertEquals(response.getClientResponseStatus(),
			ClientResponse.Status.OK);

		List<PropositionWrapper> afterDelete = this.getUserPropositions
			(USER_ID);
		Assert.assertEquals(wrappers.size() - 1, afterDelete.size());
	}
}
