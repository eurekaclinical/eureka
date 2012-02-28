package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.registry.CardiovascularRegistryETL;


public class ProtempaDevice extends Thread {

	private Long jobId;
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

	void load (Long inJobId) {

		synchronized (synchObj) {

			LOGGER.debug("ProtempaDevice loaded with Job {}" , inJobId);
			busy = true;
			this.jobId = inJobId;
			synchObj.notifyAll();	//	there should only ever be one thread wait()ing, but still...
		}
	}

	@Override
	public void interrupt() {

		super.interrupt();
		// etl.clearProtempa();
	}

	private static int ctr = 0;
	@Override
	public void run() {

		synchronized (synchObj) {

			while (true) {

				Job myJob = null;
				try {

					LOGGER.debug("{} wait." , Thread.currentThread().getName());
					synchObj.wait();
					myJob = this.jobDao.get(this.jobId);
					LOGGER.debug("{} just got a job, id={}" , Thread.currentThread().getName() , myJob.toString());
			    	myJob.setNewState ("PROCESSING" , null , null);
			    	LOGGER.debug("About to save job: {}", myJob.toString());
			    	this.jobDao.save (myJob);

			    	Long userId = myJob.getUserId();
			    	etl.runProtempa ("user" + userId);

//			    	Configuration conf = confDao.get(myJob.getUserId());
//			    	etl.runProtempa ("cvrg");
//			    	if (ctr == 0) {
//
//			    		ctr++;
//				    	etl.runProtempa ("cvrg");
//			    	}
//			    	else if (ctr == 1) {
//
//				    	etl.runProtempa ("3");
//			    	}
//			    	etl.runProtempa (conf.getProtempaSchema());

			    	myJob.setNewState ("DONE" , null , null);
			    	this.jobDao.save (myJob);
				}
				catch (InterruptedException ie) {

					LOGGER.debug("{} interrupted and finished." , Thread.currentThread().getName());
			    	this.failure = true;
			    	if (myJob != null) {

			    		myJob.setNewState ("INTERRUPTED" , null , null);
			    		this.jobDao.save (myJob);
			    	}
					return;
				}
				catch (Exception e) {

					e.printStackTrace();
					LOGGER.debug("{} unknown exception and finished. {}" , Thread.currentThread().getName() , e.getMessage());
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

		return this.jobDao.get(this.jobId);
	}

	protected void tagJob (Thread thread , Throwable t) {

		Job myJob = this.jobDao.get(this.jobId);
		if (myJob != null) {

			//	recordException
		}
	}
}
