package edu.emory.cci.aiw.cvrg;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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

	private List<ProtempaDevice> devices = new ArrayList<ProtempaDevice> (1<<4);
	private volatile boolean alive = false;


	ProtempaDeviceManager() {

		init();
	}

	//	lifecycle

	public void contextInitialized (ServletContextEvent sce) {

		//	add proper reference to config file.
	}

	public void contextDestroyed (ServletContextEvent sce) {

		//	add proper reference to config file.
		this.shutdown();
	}

	void init() {

		for (int i=0 ; i<8 ; i++) {

			ProtempaDevice pd = new ProtempaDevice();
			pd.setDaemon (true);
			pd.start();
			devices.add (pd);
		}
	}

	void shutdown() {

		for (int i=0 ; i<8 ; i++) {

			devices.get(i).kill();
			//	any jobs being processes by Protempa will have there
			//	state recorded by the Protempa thread.
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	long queueJob (Job job) {

		//	if alive, transactionally place in Job/JobEvent tables.
		//	
		//
		//
		//
		return -1L;
	}

	boolean killJob (long jobId) {

		//	interrupt the Thread.
		//	create new ProtempaDevice.

		ProtempaDevice pd = null;
		synchronized (devices) {

			for (int i=0 ; i<8 ; i++) {

				pd = devices.get(i);
				if (pd.getJob() == null) {

					continue;
				}
				if (pd.getJob().getId() == jobId) {

					pd.kill();
					pd = new ProtempaDevice();
					pd.start();
					devices.set (i , pd);
					return true;
				}
			}
			return false;
		}
	}
}
