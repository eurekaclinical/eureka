package edu.emory.cci.aiw.cvrg;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


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
	//	

	public Object queueJob (Job job) {

		return null;
	}

	public Object getJob (long jobId) {

		return null;
	}

	public Object getAllJobsByUser (long userId) {

		return null;
	}

	public Object getAllJobs() {

		return null;
	}

	public Object killJob (long jobId) {

		return null;
	}

	public Object killAllJobsByUser (long userId) {

		return null;
	}

	public Object killAllJobs() {

		return null;
	}





    @GET
    @Path("status/{jobId}")
    @Produces("application/json")
    public Job getJobStatus (@PathParam("jobId") String jobId) {

    	return null;
    }


    @POST
    @Path("status/{jobId}")
    @Consumes("application/json")
    public void getJobStatus (@PathParam("jobId") Job jobId) {


    }
    
    
    
    @GET
    @Path("/jobstatus/{userId}")
    @Produces("application/json")
    public String getJobStatusByUser (@PathParam("userId") String userId) {

    	return "a collection of Job objects for this userId, else err";
    }

    @DELETE
    @Path("/jobAction/{jobId}")
    @Produces("application/json")
    public String deleteJob (@PathParam("jobId") String jobId) {

    	return "";
    }
}
