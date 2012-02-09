package edu.emory.cci.aiw.cvrg.eureka.backend.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    @Path("test")
    @Consumes("application/json")
    public String getJobbbb (@PathParam("jobId") Job job) {

//    	try {
//
//    		
//    		DriverManager.setLogWriter (new java.io.PrintWriter (System.out));
//    		Connection cn = DriverManager.getConnection ("jdbc:oracle:thin:@adrastea.cci.emory.edu:1521:XE" , "cvrg" , "cvrg");
//    		Statement st = cn.createStatement();
//    		ResultSet rs = st.executeQuery ("select * from dual");
//    		while (rs.next()) {
//
//    			System.out.println ("ORA: " + rs.getObject(1));
//    			System.out.flush();
//    		}
//    		rs.close();
//    		st.close();
//    		cn.close();
//    	}
//    	catch (Exception e) {
//
//    		e.printStackTrace();
//    	}
    	//	cast String to long.
    	//	return Job object from 
    	System.out.println ("ETL:getJob");
    	Job j = new Job();
//    	j.setId (2L);
//    	JobEvent je = new JobEvent();
//    	je.setId(0L);
//    	je.setState("QUEUED");
//    	if (j.getJobEvents() == null) {
//
//    		j.setJobEvents(new ArrayList<JobEvent>());
//    	}
//    	j.getJobEvents().add (je);
    	ProtempaDeviceManager.getInstance().qJob (j);
//    	ProtempaDeviceManager.getInstance().job = j;
//		invokeProcessAndReturn (new String[] {"/etc/init.d/protempa" , "start" , "s"});
//    	truncateI2B2();
    	return "right";
    }

	private void truncateI2B2() {

		Connection cn = null;
		Statement st = null;
		try {

			Class.forName ("oracle.jdbc.driver.OracleDriver");
			cn = java.sql.DriverManager.getConnection ("jdbc:oracle:thin:@adrastea.cci.emory.edu:1521/XE" , "i2b2metadata" , "i2b2metadata");
			st = cn.createStatement();
			st.executeQuery ("truncate table cardiovascularregistry");
			cn.commit();
		}
		catch (Exception e) {

			e.printStackTrace();
		}
		finally {

			try {

				st.close();
			}
			catch (Exception e) {

			}
			try {

				cn.close();
			}
			catch (Exception e) {

			}
		}
	}

	private void invokeProcessAndReturn (String... command) {

		try {

			new ProcessBuilder (command).start();
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}


	
	
	
	//	STATUS:
	//
	//		get job by id
	//
	//		getJobs using filter:
	//
	//			userID
	//			status
	//			date range
	//

	
	
	
	
//    @GET
//    @Path("status")
//    @Produces("application/json")
//    public Job getJob (@PathParam("jobId") String jobId) {
//
//    	return ProtempaDeviceManager.getInstance().job;
//    }
//
//    @POST
//    @Path("submit/{conf}")
//    @Consumes("application/json")
//    @Produces("application/json")
//    public Job startJob (@PathParam("jobId") Job job) {
//
//    	//	create Job from Configuration.
//    	//	the Job parameter has no assigned jobId yet...  so persist it
//
//    	job.setJobEvents (new ArrayList<JobEvent>());
//    	JobEvent je = new JobEvent();
//    	je.setTimeStamp (new java.sql.Date (System.currentTimeMillis()));
//    	je.setState ("CREATED");
//    	//job.addJobEvent (je);
//    	//	persist
//    	ProtempaDeviceManager pdm = ProtempaDeviceManager.getInstance();
//    	pdm.qJob (job);
//    	//	job is owned by the backend persistenceManager
//    	return job;
//    }
//
//    @POST
//    @Path("submit/{conf}")
//    @Consumes("application/json")
//    @Produces("application/json")
//    public void submitConfiguration (@PathParam("jobId") Configuration conf) {
//
//    	//	create Configuration
//    	//	save Configuration
//    	ProtempaDeviceManager pdm = ProtempaDeviceManager.getInstance();
//    }
}
