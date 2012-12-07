package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

/**
 * @author hrathod
 */
public class EtlClient extends AbstractClient {
	private static final GenericType<List<Job>> JobListType = new
			GenericType<List<Job>>() {

	};
	private final String resourceUrl;

	public EtlClient(String inResourceUrl) {
		this.resourceUrl = inResourceUrl;
	}

	@Override
	String getResourceUrl() {
		return this.resourceUrl;
	}

	public Configuration getConfiguration(Long inUserId) throws
			ClientException {
		final String path = "/api/configuration/get/" + inUserId;
		Configuration result;
		try {
			result = this.getResource().path(path).accept(
					MediaType.APPLICATION_JSON).type(
					MediaType.APPLICATION_JSON).get(Configuration.class);
		} catch (UniformInterfaceException e) {
			throw new ClientException(e.getMessage());
		}
		return result;
	}

	public void submitJob(JobRequest inJobRequest) throws ClientException {
		final String path = "/api/job/submit";
		ClientResponse response = this.getResource().path(path).type
				(MediaType.APPLICATION_JSON).accept(
				MediaType.APPLICATION_JSON).post(
				ClientResponse.class, inJobRequest);
		if (!response.getClientResponseStatus().equals(
				Response.Status.CREATED)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public List<Job> getJobStatus(JobFilter inFilter) {
		final String path = "/api/job/status";
		return this.getResource().queryParam(
				"filter", inFilter.toQueryParam()).accept(
				MediaType.APPLICATION_JSON).type(
				MediaType.APPLICATION_JSON).get(JobListType);
	}

	public void validatePropositions(ValidationRequest inRequest) throws
			ClientException {
		final String path = "/api/validate";
		ClientResponse response = this.getResource().path(path).accept
				(MediaType.APPLICATION_JSON).type(MediaType
				.APPLICATION_JSON).post(ClientResponse.class, inRequest);
		if (!response.getClientResponseStatus().equals(Response.Status.OK)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public PropositionDefinition getPropositionDefinition (Long inUserId,
			String inKey) throws ClientException {
		final String path = "/api/proposition/" + inUserId + "/" + inKey;
		PropositionDefinition result;
		try {
			result = this.getResource().path(path).accept(MediaType
					.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get
					(PropositionDefinition.class);
		} catch (UniformInterfaceException e) {
			if (!ClientResponse.Status.NOT_FOUND.equals(e.getResponse()
					.getClientResponseStatus())) {
				throw new ClientException(e.getMessage());
			} else {
				result = null;
			}
		}
		return result;
	}
}
