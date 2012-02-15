package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;

@Path("/job")
public class RestInterfaces {

	// the methods here act as the service() method does in a servlet.
	//
	// these are the clients of the singleton ProtempaDeviceManager.
	//
	// tomcat:run

	private final JobDao jobDao;
	private final ConfDao confDao;
	private final ProtempaDeviceManager protempaDeviceManager;

	@Inject
	public RestInterfaces(JobDao jobDao, ConfDao confDao,
			ProtempaDeviceManager protempaDeviceManager) {

		this.jobDao = jobDao;
		this.confDao = confDao;
		this.protempaDeviceManager = protempaDeviceManager;
	}

	@GET
	@Path("test")
	public String getJobbbb() {

		System.out.println("ETL:getJob");
		Job j = new Job();
		j.setJobEvents(new ArrayList<JobEvent>());
		this.protempaDeviceManager.qJob(j);
		return "right";
	}

	//
	// Job submit
	//

	@POST
	@Path("submitJob")
	@Consumes("application/json")
	@Produces("application/json")
	public Job startJob(Job job) {

		// create Job from Configuration.
		// the Job parameter has no assigned jobId yet... so persist it
		this.jobDao.save(job);
		job.setNewState("CREATED", null, null);
		this.jobDao.save(job);
		this.protempaDeviceManager.qJob(job);
		return job;
	}

	//
	// Job status
	//

	@GET
	@Path("status")
	@Produces("application/json")
	public List<Job> getJob(@QueryParam("jobId") Long jobId,
			@QueryParam("userId") Long userId,
			@QueryParam("status") String status,
			@QueryParam("intervalBegin") Date intervalBegin,
			@QueryParam("intervalEnd") Date intervalEnd,
			@Context HttpServletRequest req) {

		JobFilter jobFilter = new JobFilter(jobId, userId, status,
				intervalBegin, intervalEnd);
		return this.jobDao.get(jobFilter);
	}

	//
	// Configuration
	//

	@GET
	@Path("getConf/{userId}")
	@Consumes("application/json")
	@Produces("application/json")
	public Configuration getConfByUserId(@PathParam("userId") Long userId) {
		// return this.confDao.get(userId);
		// TODO: GET RID OF THIS FAKE CONFIGURATION WHEN REAL CONFIGURATIONS
		// ARE AVAILABLE
		Configuration fakeConfiguration = new Configuration();
		fakeConfiguration.setProtempaDatabaseName("XE");
		fakeConfiguration.setProtempaHost("adrastea.cci.emory.edu");
		fakeConfiguration.setProtempaPort(Integer.valueOf(1521));
		fakeConfiguration.setProtempaSchema("cvrg");
		fakeConfiguration.setProtempaPass("cvrg");
		fakeConfiguration.setUserId(userId);
		return fakeConfiguration;
	}

	@POST
	@Path("submitConf")
	@Consumes("application/json")
	public Response submitConfiguration(Configuration conf) {
		this.confDao.save(conf);
		return Response.ok().build();
	}
}
