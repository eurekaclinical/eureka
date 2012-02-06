package edu.emory.cci.aiw.cvrg.eureka.backend.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;

//	this is a singleton that should be bound to the equivalent of an
//	application-scope context variable.  this is written with the
//	assumption that there is only one instance in the entire distributed
//	application of this singleton.
//
//	this object should be the server only to the REST connection methods
//	in the servlet equivalent.
//
//	for the dependency injection mechanism:
//
//		the database schema for checkpointing job status.
//		the database where all protempa schema be.
//		the location of the i2b2 database.
//
//

public class ProtempaDeviceManager implements ServletContextListener {

	//
	//	IMPORTANT:
	//	jobQ is a synch point. it is locked by a Request thread or by
	//	the JobLoader thread (internal to this singleton).
	//
	private List<Job> jobQ = new ArrayList<Job> (1<<4);
	private List<ProtempaDevice> devices = new ArrayList<ProtempaDevice> (1<<4);
	public Job job;

	private static volatile ProtempaDeviceManager ME = null;

	private class JobLoader implements Runnable {

		public void run() {

			while (true) {

				try {

			    	System.out.println ("ETL: JobLoader loop");
					Thread.sleep (10000L);
					synchronized (jobQ) {

//						for (ProtempaDevice proD : devices) {
//
//							
//						}
						Iterator<Job> itr = jobQ.iterator();
						while (itr.hasNext()) {

					    	System.out.println ("ETL: match job to protempa");
							Job x = itr.next();
							devices.get(0).load (x);
							itr.remove();
						}
					}
				}
				catch (Exception e) {

			    	System.out.println ("ETL: JobLoader exception " + e);
					e.printStackTrace();
				}
			}
		}
	}

	private ProtempaDeviceManager() {

    	System.out.println ("ETL: new ProtempaDeviceManager");
		init();
	}

	//	lifecycle
	public void contextInitialized (ServletContextEvent sce) {

	}

	public void contextDestroyed (ServletContextEvent sce) {

		this.shutdown();
	}


	void init() {

		for (int i=0 ; i<8 ; i++) {

	    	System.out.println ("ETL: create new ProtempaDevice");
			ProtempaDevice pd = new ProtempaDevice();
			pd.setDaemon (true);
			pd.setName ("ProtempaDevice-" + i);
			pd.start();
			devices.add (pd);
		}
		Thread t = new Thread (new JobLoader());
		t.setDaemon (true);
		t.setName ("ETL-JobLoader");
		t.start();
	}

	void shutdown() {

		//	interrupt loader, then

		for (int i=0 ; i<8 ; i++) {

			devices.get(i).kill();
			//	any jobs being processes by Protempa will have there
			//	state recorded by the Protempa thread.
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	static ProtempaDeviceManager getInstance() {

    	System.out.println ("ETL:getInstance");
		if (ME != null) {

			return ME;
		}
		else {

			synchronized (ProtempaDeviceManager.class) {

				if (ME == null) {

					ME = new ProtempaDeviceManager();
				}
				return ME;
			}
		}
	}

	private Job getOldestReadyJob() {

		synchronized (jobQ) {	// at the service of the JobLoader ONLY

			for (Job j : jobQ) {

			}
		}
		return null;
	}
	
	boolean qJob (Job job) {

    	System.out.println ("ETL: qJob ol");
		synchronized (jobQ) {

	    	System.out.println ("ETL: qJob il");
			jobQ.add (job);
		}
		return true;
	}

	boolean killJob (long jobId) {

		//	interrupt the Thread.
		//	create new ProtempaDevice.
		return false;
	}
}
