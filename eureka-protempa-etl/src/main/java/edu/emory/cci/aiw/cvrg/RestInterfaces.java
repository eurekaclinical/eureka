package edu.emory.cci.aiw.cvrg;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;

@Path("/job")
public class RestInterfaces {

	//	the methods here act as the service() method does
	//	in a servlet.  
	//	
	//	don't know what form the data has in the 'request'	
	//	or the 'response'. are these http variables ?  or
	//	are these marshalled as data objects ?
	//	
	//	nothing here should be concerned with the database.
	//	
	//	these are the clients of the singleton ProtempaDeviceManager.
	//	
	//	tomcat:run

    @GET
    @Path("status/{jobId}")
    @Produces("application/json")
    public Job getJob (@PathParam("jobId") String jobId) {

    	//	cast String to long.
    	//	return Job object from 
    	return null;
    }

    @POST
    @Path("submit/{conf}")
    @Consumes("application/json")
    @Produces("application/json")
    public Job startJob (@PathParam("jobId") Job job) {

    	//	create Job from Configuration.
    	//	the Job parameter has no assigned jobId yet...  so persist it

    	job.setJobEvents (new ArrayList<JobEvent>());
    	JobEvent je = new JobEvent();
    	je.setTimeStamp (new java.sql.Date (System.currentTimeMillis()));
    	je.setState ("CREATED");
    	//job.addJobEvent (je);
    	//	persist
    	ProtempaDeviceManager pdm = ProtempaDeviceManager.getInstance();
    	pdm.qJob (job);
    	//	job is owned by the backend persistenceManager
    	return job;
    }

    @POST
    @Path("submit/{conf}")
    @Consumes("application/json")
    @Produces("application/json")
    public void submitConfiguration (@PathParam("jobId") Configuration conf) {

    	//	create Configuration
    	//	save Configuration
    	ProtempaDeviceManager pdm = ProtempaDeviceManager.getInstance();
    }
}
