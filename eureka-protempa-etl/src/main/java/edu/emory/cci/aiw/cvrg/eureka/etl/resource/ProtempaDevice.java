package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.registry.CardiovascularRegistryETL;


public class ProtempaDevice extends Thread {

	private Job job;
	private CardiovascularRegistryETL etl = new CardiovascularRegistryETL();
	//
	//	IMPORTANT:
	//	the synchObj is locked by the internal thread in ProtempaDeviceManager
	//	that calls load()
	//	and is locked by the runnable inside this ProtempaDevice instance.
	//
	private final Object synchObj = new Object();
	private volatile boolean busy = false;
	private volatile boolean failure = false;
	private final JobDao jobDao;

	private class UEH implements Thread.UncaughtExceptionHandler {

		ProtempaDevice pd = null;

		public UEH (ProtempaDevice pd) {

			this.pd = pd;
		}

		public void uncaughtException (Thread thread , Throwable t) {

			pd.tagJob (thread , t);
		}
	}


	public ProtempaDevice (JobDao jobDao) {

		this.jobDao = jobDao;
	}

	void load (Job job) {

    	System.out.println ("ETL: load ol");
		synchronized (synchObj) {

	    	System.out.println ("ETL: load il");
			busy = true;
			this.job = job;
			synchObj.notifyAll();	//	there should only ever be one thread wait()ing, but still...
		}
	}

	@Override
	public void run() {

		synchronized (synchObj) {

			while (true) {

				Job myJob = null;
				try {

			    	System.out.println ("ETL: protempa.wait");
					synchObj.wait();
					myJob = this.job;
			    	System.out.println ("ETL: protempa.go");

			    	myJob.setNewState ("PROCESSING" , null , null);
			    	this.jobDao.save (myJob);

			    	etl.main (new String[] {"them parameters here"});

			    	myJob.setNewState ("DONE" , null , null);
			    	this.jobDao.save (myJob);
				}
				catch (InterruptedException ie) {

			    	System.out.println ("ETL: runner exception " + ie);
			    	this.failure = true;
			    	if (myJob != null) {

			    		myJob.setNewState ("INTERRUPTED" , null , null);
			    		this.jobDao.save (myJob);
			    	}
					return;
				}
				catch (Exception e) {

			    	System.out.println ("ETL: runner exception " + e);
			    	this.failure = true;
			    	if (myJob != null) {

			    		myJob.setNewState ("EXCEPTION" , null , null);
			    		this.jobDao.save (myJob);
			    	}
					return;
				}
				finally {

					busy = false;
				}
			}
		}
	}

	boolean isBusy() {

		return this.busy;
	}

	boolean hasFailed() {

		return this.failure;
	}

	Job getJob() {

		return this.job;
	}

	protected void tagJob (Thread thread , Throwable t) {

		Job myJob = this.job;
		if (myJob != null) {

			//	recordException
		}
	}
}
