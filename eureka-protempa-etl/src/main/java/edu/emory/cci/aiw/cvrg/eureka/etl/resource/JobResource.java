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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;

@Path("/job")
public class JobResource {
	private static final Logger LOGGER =
			LoggerFactory.getLogger(JobResource.class);
	private final JobDao jobDao;
	private final ConfDao confDao;
	private final ProtempaDeviceManager protempaDeviceManager;

	@Inject
	public JobResource(JobDao inJobDao,
			ConfDao inConfDao,
			ProtempaDeviceManager inProtempaDeviceManager) {
		this.confDao = inConfDao;
		this.jobDao = inJobDao;
		this.protempaDeviceManager = inProtempaDeviceManager;
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
		Job job = inJobRequest.getJob();
		List<PropositionDefinition> definitions =
				this.unwrapAll(inJobRequest.getPropositionWrappers());
		LOGGER.debug("Created {} definitions", definitions.size());
		job.setNewState("CREATED", null, null);
		LOGGER.debug("Request to start new Job {}", job.getId());
		this.jobDao.create(job);
		this.protempaDeviceManager.qJob(job);
		return Response.created(URI.create("/" + job.getId())).build();
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

	private List<PropositionDefinition> unwrapAll (List<PropositionWrapper> inWrappers) {
		List<PropositionDefinition> definitions = new ArrayList
				<PropositionDefinition>(inWrappers.size());
		for (PropositionWrapper wrapper : inWrappers) {
			definitions.add(unwrap(wrapper));
		}
		return definitions;
	}

	private PropositionDefinition unwrap(PropositionWrapper inWrapper) {
		PropositionDefinition definition;
		String[] allTargets = getTargets(inWrapper.getSystemTargets(),
				inWrapper.getUserTargets());

		if (inWrapper.getType() == PropositionWrapper.Type.AND) {
			HighLevelAbstractionDefinition d =
					new HighLevelAbstractionDefinition(inWrapper.getId());
			d.setAbbreviatedDisplayName(inWrapper.getAbbrevDisplayName());
			d.setDisplayName(inWrapper.getDisplayName());
			d.setInverseIsA(allTargets);
			definition = d;
		} else {
			EventDefinition e = new EventDefinition(inWrapper.getId());
			e.setAbbreviatedDisplayName(inWrapper.getAbbrevDisplayName());
			e.setDisplayName(inWrapper.getDisplayName());
			e.setInverseIsA(allTargets);
			definition = e;
		}

		return definition;
	}

	private String[] getTargets(List<String> systemTargets,
			List<Long> userTargets) {
		int size = systemTargets.size() + userTargets.size();
		String[] result = new String[size];
		int counter = 0;
		for (String s : systemTargets) {
			result[counter++] = s;
		}
		for (Long l : userTargets) {
			result[counter++] = String.valueOf(l.longValue());
		}
		return result;
	}
}
