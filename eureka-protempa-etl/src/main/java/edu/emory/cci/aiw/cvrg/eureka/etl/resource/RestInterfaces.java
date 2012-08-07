package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
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
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RestInterfaces.class);

    @Inject
    public RestInterfaces(JobDao jobDao, ConfDao confDao,
            ProtempaDeviceManager protempaDeviceManager) {

        this.jobDao = jobDao;
        this.confDao = confDao;
        this.protempaDeviceManager = protempaDeviceManager;
        LOGGER.debug("Instantiate RestInterfaces");
    }

    @GET
    @Path("test")
    public String getJob() {

        Job job = new Job();

        this.jobDao.create(job);
        LOGGER.debug("Request to start new Job {0}", job.getId());
        job.setNewState("CREATED", null, null);
        this.jobDao.update(job);
        this.protempaDeviceManager.qJob(job);

        Job boj = new Job();

        this.jobDao.create(boj);
        LOGGER.debug("Request to start new Job {0}", boj.getId());
        boj.setNewState("CREATED", null, null);
        this.jobDao.update(boj);
        this.protempaDeviceManager.qJob(boj);

        return "right";
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Job> getAll() {
        return this.jobDao.getAll();
    }

    //
    // Job submit
    //
    @POST
    @Path("submitJob")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Job startJob(Job job) {
        job.setNewState("CREATED", null, null);
        LOGGER.debug("Request to start new Job {}", job.getId());
        this.jobDao.create(job);
        this.protempaDeviceManager.qJob(job);
        return job;
    }

    //
    // Job status
    //
    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Job> getJob(@QueryParam("filter") JobFilter inFilter) {

        LOGGER.debug("Request for job status");
        List<Job> jobs = this.jobDao.getWithFilter(inFilter);
        for (Job job : jobs) {
            this.jobDao.refresh(job);
            LOGGER.debug("Returning job {} with status {}", job.getId(),
                    job.getCurrentState());
        }
        return jobs;
    }

    //
    // Configuration
    //
    @GET
    @Path("getConf/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Configuration getConfByUserId(@PathParam("userId") Long userId) {
        LOGGER.debug("Configuration request for user {}", userId);
        return this.confDao.getByUserId(userId);
    }

    @POST
    @Path("submitConf")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitConfiguration(Configuration conf) {
        this.confDao.create(conf);
        LOGGER.debug("Request to save Configuration");
        return Response.ok().build();
    }
}
