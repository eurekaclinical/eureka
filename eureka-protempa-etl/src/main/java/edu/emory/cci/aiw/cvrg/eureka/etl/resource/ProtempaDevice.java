package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
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
	private final ConfDao confDao;
	private final JobDao jobDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtempaDevice.class);

	private class UEH implements Thread.UncaughtExceptionHandler {

		ProtempaDevice pd = null;

		public UEH (ProtempaDevice pd) {

			this.pd = pd;
		}

		public void uncaughtException (Thread thread , Throwable t) {

			pd.tagJob (thread , t);
		}
	}


	public ProtempaDevice (JobDao jobDao , ConfDao confDao) {

		this.jobDao = jobDao;
		this.confDao = confDao;
	}

	void load (Job job) {

		synchronized (synchObj) {

			LOGGER.debug("ProtempaDevice loaded with Job {1}" , job.getId());
			busy = true;
			this.job = job;
			synchObj.notifyAll();	//	there should only ever be one thread wait()ing, but still...
		}
	}

	@Override
	public void interrupt() {

		super.interrupt();
		// etl.clearProtempa();
	}

	@Override
	public void run() {

		synchronized (synchObj) {

			while (true) {

				Job myJob = null;
				try {

					LOGGER.debug("{1} wait." , Thread.currentThread().getName());
					synchObj.wait();
					myJob = this.jobDao.get(this.job.getId());
					LOGGER.debug("{1} just got a job, id= {2}" , Thread.currentThread().getName() , myJob.getId());
			    	myJob.setNewState ("PROCESSING" , null , null);
			    	this.jobDao.save (myJob);

//			    	Configuration conf = confDao.get(myJob.getUserId());
			    	etl.runProtempa ("cvrg");
//			    	etl.runProtempa (conf.getProtempaSchema());

			    	myJob.setNewState ("DONE" , null , null);
			    	this.jobDao.save (myJob);
				}
				catch (InterruptedException ie) {

					LOGGER.debug("{1} interrupted and finished." , Thread.currentThread().getName());
			    	this.failure = true;
			    	if (myJob != null) {

			    		myJob.setNewState ("INTERRUPTED" , null , null);
			    		this.jobDao.save (myJob);
			    	}
					return;
				}
				catch (Exception e) {

					e.printStackTrace();
					LOGGER.debug("{1} unknown exception and finished. {2}" , Thread.currentThread().getName() , e.getMessage());
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
