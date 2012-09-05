package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

import junit.framework.Assert;

public class PropositionResourceTest extends AbstractServiceResourceTest {

	private static final Long USER_ID = 1L;

	@Test
	public void userPropositionTest() {
		GenericType<List<PropositionWrapper>> wrapperListType =
			new GenericType<List<PropositionWrapper>>() {
			};
		WebResource resource = this.resource();
		List<PropositionWrapper> wrappers =
			resource.path("/api/proposition/user/list/" + USER_ID).accept
				(MediaType.APPLICATION_JSON).get(wrapperListType);
		Assert.assertTrue(wrappers.size() > 0);
	}
}
