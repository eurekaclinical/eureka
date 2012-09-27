package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PropositionUtil;

/**
 * REST Web Service
 *
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private final PropositionDao propositionDao;
	private final UserDao userDao;
	private final ServiceProperties applicationProperties;
	private final SystemPropositionFinder systemPropositionFinder;

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger
		(PropositionResource.class);

	/**
	 * Creates a new instance of PropositionResource
	 */
	@Inject
	public PropositionResource(PropositionDao inPropositionDao,
		UserDao inUserDao, ServiceProperties inApplicationProperties,
		SystemPropositionFinder inFinder) {
		this.propositionDao = inPropositionDao;
		this.userDao = inUserDao;
		this.applicationProperties = inApplicationProperties;
		this.systemPropositionFinder = inFinder;
	}

	@GET
	@Path("/system/{userId}/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionWrapper> getSystemPropositions(@PathParam(
		"userId") Long inUserId) {
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();
		wrappers.add(
			this.fetchSystemProposition(
				inUserId, "ICD9:Procedures"));
		wrappers.add(this.fetchSystemProposition(inUserId, "ICD9:Diagnoses"));
		wrappers.add(this.fetchSystemProposition(inUserId, "ICD9:E-codes"));
		wrappers.add(this.fetchSystemProposition(inUserId, "ICD9:V-codes"));
		return wrappers;
	}

	@GET
	@Path("/system/{userId}/{propKey}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getSystemProposition(@PathParam(
		"userId") Long inUserId, @PathParam("propKey") String inKey) {
		return fetchSystemProposition(inUserId, inKey);
	}

	@POST
	@Path("/system/{userId}/batch")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionWrapper> batchSystemPropositions(@PathParam
		("userId") Long inUserId, List<String> inIdList) {
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>(inIdList.size());
		for (String id : inIdList) {
			wrappers.add(this.fetchSystemProposition(inUserId, id));
		}
		return wrappers;
	}

	@GET
	@Path("/user/list/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PropositionWrapper> getUserPropositions(@PathParam(
		"userId") Long inUserId) {
		List<PropositionWrapper> result = new ArrayList<PropositionWrapper>();
		for (Proposition p : this.propositionDao.getByUserId(inUserId)) {
			result.add(PropositionUtil.wrap(p, false));
		}
		return result;
	}

	@POST
	@Path("/user/validate/{userId}")
	public Response validateProposition(@PathParam(
		"userId") Long inUserId, PropositionWrapper inWrapper) {
		Response result;
		List<Proposition> propositions = this.propositionDao.getByUserId
			(inUserId);
		List<PropositionWrapper> wrappers = new
			ArrayList<PropositionWrapper>();

		for (Proposition proposition : propositions) {
			wrappers.add(PropositionUtil.wrap(proposition, false));
		}

		ValidationRequest validationRequest = new ValidationRequest();
		validationRequest.setUserId(inUserId);
		validationRequest.setPropositions(wrappers);
		validationRequest.setTargetProposition(inWrapper);

		try {
			Client client = CommUtils.getClient();
			WebResource resource = client.resource(
				this.applicationProperties.getEtlPropositionValidationUrl());
			ClientResponse response = resource.type(
				MediaType.APPLICATION_JSON).post(
				ClientResponse.class, validationRequest);
			result = Response.status(
				response.getClientResponseStatus().getStatusCode()).build();
		} catch (KeyManagementException e) {
			LOGGER.error(e.getMessage(), e);
			result = Response.serverError().build();
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
			result = Response.serverError().build();
		}

		return result;
	}

	@GET
	@Path("/user/get/{propId}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getUserProposition(@PathParam(
		"propId") Long inPropositionId) {
		PropositionWrapper wrapper = null;
		Proposition proposition = this.propositionDao.retrieve
			(inPropositionId);
		if (proposition != null) {
			if (proposition.isInSystem()) {
				wrapper = this.fetchSystemProposition(
					proposition.getUserId(), proposition.getKey());
				wrapper.setCreated(proposition.getCreated());
				wrapper.setLastModified(proposition.getLastModified());
			} else {
				wrapper = PropositionUtil.wrap(proposition, false);
			}
		}
		return wrapper;
	}

	@DELETE
	@Path("/user/delete/{prodId}")
	public Response deleteUserPropositions(@PathParam("propId") Long
		inPropositionId) {
		// the response to return;
		Response response = Response.ok().build();

		// fetch the proposition for the given ID
		Proposition target = this.propositionDao.retrieve(inPropositionId);
		if (target == null) {
			// if the target is not found, return a NOT_FOUND response
			response = Response.status(Response.Status.NOT_FOUND).build();
		} else {
			// now get the rest of the propositions for the user
			List<Proposition> others = this.propositionDao.getByUserId(
				target.getUserId());

			// now loop through and make sure that the given proposition is
			// not used in the definition of any of the other propositions.
			boolean error = false;
			for (Proposition proposition : others) {
				if (proposition.getId().equals(target.getUserId())) {
					continue;
				} else {
					List<Proposition> targets = new ArrayList<Proposition>();
					if (proposition.getAbstractedFrom() != null) {
						targets.addAll(proposition.getAbstractedFrom());
					}
					if (proposition.getInverseIsA() != null) {
						targets.addAll(proposition.getInverseIsA());
					}
					for (Proposition p : targets) {
						if (p.getId().equals(target.getId())) {
							response = Response.notModified(
								p.getAbbrevDisplayName() + " contains a " +
									"reference to " + target
									.getAbbrevDisplayName()).build();
							break;
						}
					}
				}
				if (error) {
					break;
				}
			}
			if (! error) {
				this.propositionDao.remove(target);
			}
		}
		return response;
	}

	@PUT
	@Path("/user/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateProposition(PropositionWrapper inWrapper) {
		if (inWrapper.getUserId() != null && inWrapper.getId() != null) {
			Proposition proposition = PropositionUtil.unwrap(
				inWrapper, this.propositionDao);
			proposition.setLastModified(new Date());
			this.propositionDao.update(proposition);
		} else {
			throw new IllegalArgumentException(
				"Both the user ID and the " + "proposition ID must be " +
					"provided.");
		}
	}

	@POST
	@Path("/user/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertProposition(PropositionWrapper inWrapper) {
		if (inWrapper.getUserId() != null) {
			Proposition proposition = PropositionUtil.unwrap(
				inWrapper, this.propositionDao);
			Date now = new Date();
			proposition.setCreated(now);
			proposition.setLastModified(now);
			this.propositionDao.create(proposition);
		} else {
			throw new IllegalArgumentException("User ID must be provided.");
		}
	}

	private PropositionWrapper fetchSystemProposition(Long inUserId,
		String inKey) {
		return this.systemPropositionFinder.find(inUserId, inKey);
	}

}
