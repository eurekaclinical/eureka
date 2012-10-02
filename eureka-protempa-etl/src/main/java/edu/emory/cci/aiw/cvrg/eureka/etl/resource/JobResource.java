package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator
	.PropositionValidatorException;
import org.protempa.ExtendedPropositionDefinition;

@Path("/job")
public class JobResource {
	private static final Logger LOGGER =
		LoggerFactory.getLogger(JobResource.class);
	private final JobDao jobDao;
	private final PropositionValidator propositionValidator;
	private final TaskManager taskManager;
	private final ConfDao confDao;

	@Inject
	public JobResource(JobDao inJobDao, TaskManager inTaskManager,
		PropositionValidator inValidator, ConfDao inConfDao) {
		this.jobDao = inJobDao;
		this.taskManager = inTaskManager;
		this.propositionValidator = inValidator;
		this.confDao = inConfDao;
	}

	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Job> getAll() {
		return this.jobDao.getAll();
	}

	@GET
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Job getJob(@PathParam("jobId") Long inJobId) {
		return this.jobDao.retrieve(inJobId);
	}

	@POST
	@Path("/submit")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response startJob(JobRequest inJobRequest) {
		Response response;
		Job job = inJobRequest.getJob();
		List<PropositionWrapper> wrappers =
			inJobRequest.getPropositionWrappers();
		Configuration configuration = this.confDao.getByUserId(job.getUserId
			());
		propositionValidator.setConfiguration(configuration);
		propositionValidator.setPropositions(wrappers);
		boolean valid;
		try {
			valid = propositionValidator.validate();
		} catch (PropositionValidatorException e) {
			LOGGER.error(e.getMessage(),e);
			valid = false;
		}

		if (valid) {
			List<PropositionDefinition> definitions = this.unwrapAll
				(wrappers);
			LOGGER.debug("Created {} definitions", definitions.size());
			job.setNewState("CREATED", null, null);
			LOGGER.debug("Request to start new Job {}", job.getId());
			this.jobDao.create(job);
			this.taskManager.queueTask(job.getId(), definitions);
			response = Response.created(URI.create("/" + job.getId()))
				.build();
		} else {
			job.setNewState("FAILED",null,null);
			this.jobDao.create(job);
			for (String message : propositionValidator.getMessages()) {
				LOGGER.error(message);
			}
			response =
				Response.status(Response.Status.BAD_REQUEST).entity
					(propositionValidator.getMessages()).build();
		}
		return response;
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Job> getJobStatus(@QueryParam("filter") JobFilter inFilter) {

		LOGGER.debug("Request for job status");
		List<Job> jobs = this.jobDao.getWithFilter(inFilter);
		for (Job job : jobs) {
			this.jobDao.refresh(job);
			LOGGER.debug("Returning job {} with status {}", job.getId(),
				job.getCurrentState());
		}
		return jobs;
	}

	private List<PropositionDefinition> unwrapAll(List<PropositionWrapper>
		inWrappers) {
		List<PropositionDefinition> definitions =
			new ArrayList<PropositionDefinition>();
		if (inWrappers != null) {
			for (PropositionWrapper wrapper : inWrappers) {
				definitions.add(unwrap(wrapper));
			}
		}
		return definitions;
	}

	private PropositionDefinition unwrap(PropositionWrapper inWrapper) {
		PropositionDefinition definition;
		String[] allTargets = getTargets(inWrapper.getChildren());

		String idStr = String.valueOf(inWrapper.getId().longValue());
		if (inWrapper.getType() == PropositionWrapper.Type.AND) {
			HighLevelAbstractionDefinition d =
				new HighLevelAbstractionDefinition(idStr);
			d.setAbbreviatedDisplayName(inWrapper.getAbbrevDisplayName());
			d.setDisplayName(inWrapper.getDisplayName());
                        for (String target : allTargets) {
                            d.add(new ExtendedPropositionDefinition(target));
                        }
			definition = d;
		} else {
			EventDefinition e = new EventDefinition(idStr);
			e.setAbbreviatedDisplayName(inWrapper.getAbbrevDisplayName());
			e.setDisplayName(inWrapper.getDisplayName());
			e.setInverseIsA(allTargets);
			definition = e;
		}

		return definition;
	}

	private String[] getTargets(List<PropositionWrapper> children) {
		int size = children.size();
		String[] result = new String[size];
		int counter = 0;
		for (PropositionWrapper wrapper : children) {
			String id;
			if (wrapper.isInSystem()) {
				id = wrapper.getKey();
			} else {
				id = String.valueOf(wrapper.getId().longValue());
			}
			result[counter++] = id;
		}
		return result;
	}
}
