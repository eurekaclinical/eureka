package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

public class SystemPropositionRetriever implements PropositionRetriever<Long,
	String> {

	private final ServiceProperties applicationProperties;

	@Inject
	public SystemPropositionRetriever (ServiceProperties inProperties) {
		this.applicationProperties = inProperties;
	}

	@Override
	public PropositionWrapper retrieve(Long inUserId, String inKey) {
		PropositionWrapper wrapper = null;

		try {
			String path = 
					UriBuilder.fromUri("/").segment("" + inUserId, inKey).build().toString();
			Client client = CommUtils.getClient();
			WebResource resource =
				client.resource(this.applicationProperties
					.getEtlPropositionGetUrl());
			wrapper =
				resource.path(path).accept(MediaType.APPLICATION_JSON).get
					(PropositionWrapper.class);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return wrapper;
	}
}
