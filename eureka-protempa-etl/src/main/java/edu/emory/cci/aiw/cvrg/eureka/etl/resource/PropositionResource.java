package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValidationRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.etl.ksb.PropositionFinderException;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorException;

/**
 * @author hrathod
 */
@Path("/proposition")
public class PropositionResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropositionResource.class);
	private final PropositionValidator propositionValidator;
	private final ConfDao confDao;

	@Inject
	public PropositionResource(PropositionValidator inValidator,
			ConfDao inConfDao) {
		this.propositionValidator = inValidator;
		this.confDao = inConfDao;
	}

	@POST
	@Path("/validate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response validatePropositions(ValidationRequest inRequest) {

		boolean result;
		Configuration configuration = this.confDao.getByUserId(
				inRequest.getUserId());
		if (configuration != null) {
			try {
				propositionValidator.setConfiguration(configuration);
				propositionValidator.setPropositions(inRequest.getPropositions());
				propositionValidator.setTargetProposition(
						inRequest.getTargetProposition());
				result = propositionValidator.validate();
			} catch (PropositionValidatorException e) {
				LOGGER.error(e.getMessage(), e);
				result = false;
			}
		} else {
			result = false;
			LOGGER.error("No Protempa configuration found for user "
					+ inRequest.getUserId());
		}

		Response response;
		if (result) {
			response = Response.ok().build();
		} else {
			response = Response.status(Response.Status.NOT_ACCEPTABLE)
					.entity(propositionValidator.getMessages()).build();
		}
		return response;
	}

	@GET
	@Path("/{userId}/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public PropositionWrapper getProposition(@PathParam(
		"userId") Long inUserId, @PathParam("key") String inKey) {

		PropositionWrapper wrapper = null;
		try {
			Configuration configuration = this.confDao.getByUserId(inUserId);
			if (configuration != null) {
				PropositionFinder propositionFinder =
						new PropositionFinder(configuration);
				PropositionDefinition definition =
						propositionFinder.find(inKey);
				if (definition != null) {
					wrapper = this.getInfo(definition, false, propositionFinder);
				} else {
					LOGGER.info("No proposition with id " + inKey);
				}
			} else {
				LOGGER.error("No Protempa configuration found for user "
						+ inUserId);
			}
		} catch (PropositionFinderException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return wrapper;
	}

	private PropositionWrapper getInfo(PropositionDefinition inDefinition,
			boolean summarize, PropositionFinder inPropositionFinder)
			throws
			PropositionFinderException {

		PropositionWrapper wrapper = new PropositionWrapper();
		wrapper.setKey(inDefinition.getId());
		wrapper.setInSystem(true);
		wrapper.setAbbrevDisplayName(
				inDefinition.getAbbreviatedDisplayName());
		wrapper.setDisplayName(inDefinition.getDisplayName());
		wrapper.setSummarized(summarize);
		wrapper.setParent(inDefinition.getChildren().length > 0);

		if (!summarize) {
			List<PropositionWrapper> children = new ArrayList<PropositionWrapper>(
					inDefinition.getChildren().length);
			for (String key : inDefinition.getChildren()) {
				children.add(
						this.getInfo(
						inPropositionFinder.find(
						key), true, inPropositionFinder));
			}
			wrapper.setChildren(children);
		}
		return wrapper;
	}
}
