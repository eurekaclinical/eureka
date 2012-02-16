package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

//	this is a singleton that should be bound to the equivalent of an
//	application-scope context variable.  this is written with the
//	assumption that there is only one instance in the entire distributed
//	application of this singleton.
//
//	if the application context gets evicted, all current processing
//	crashes.
//
//	this object should be the server only to the REST connection methods
//	in the servlet equivalent.

@Singleton
public class ProtempaDeviceManager implements ServletContextListener {

	//
	//	IMPORTANT:
	//	jobQ is a synch point. it is locked by a Request thread or by
	//	the JobLoader thread (internal to this singleton).
	//

	private static final int qLen = 4;
	private List<Job> jobQ = new ArrayList<Job> (1<<4);
	private List<ProtempaDevice> devices = new ArrayList<ProtempaDevice>(qLen);
	private final JobDao jobDao;
	private Thread jobLoaderThread;
	private static final Logger LOGGER = LoggerFactory.getLogger(ProtempaDeviceManager.class);

	private class JobLoader implements Runnable {

		public void run() {

			while (true) {

				try {

					LOGGER.debug("JobLoader event loop sleep");
					Thread.sleep (16000L);
					synchronized (jobQ) {

						//	load zero or one ProtempaDevice per sleep cycle. there
						//	is a race condition in Protempa concerning System.Properties use.
						for (ProtempaDevice protempaDevice : devices) {

							if ( ! protempaDevice.isBusy() && ! protempaDevice.hasFailed()) {

								Job j = getNextJob();
								if (j != null) {

									//	this is the last i'll ever see of the job, unless it is
									//	recovered from a failed ProtempaDevice.
									LOGGER.debug("JobLoader found a job to do {0}" , j.getId());
									protempaDevice.load (j);
								}
								break;
							}
						}
						for (int i=0 ; i<devices.size() ; i++) {

							ProtempaDevice protempaDevice = devices.get(i);
							if (protempaDevice.hasFailed()) {

								LOGGER.debug("JobLoader found a broken ProtempaDevice. Replacing it with a new one.");
								ProtempaDevice pd = new ProtempaDevice (jobDao);
								pd.setDaemon (true);
								pd.setName ("ProtempaDevice-" + i);
								pd.start();

								ProtempaDevice broken = devices.set (i , pd);
								Job unknownStateJob = broken.getJob();
								if (unknownStateJob != null) {

									LOGGER.debug("JobLoader recovered a job from a broken ProtempaDevice.");
									Job nJob = jobDao.get (unknownStateJob.getId());
									nJob.setNewState ("FAILED" , "job recovered from a broken protempa" , null);
									jobDao.save (nJob);
								}
							}
						}
					}
				}
				catch (InterruptedException ie) {

					LOGGER.debug("JobLoader interrupted.");
					return;
				}
				catch (Exception e) {

					LOGGER.debug("JobLoader exception. " + e.getMessage());
					return;
				}
			}
		}

		private Job getNextJob() {

			//	synchronized jobQ access via client context.
			//	don't use without synch'ing on jobQ.
			Iterator<Job> itr = jobQ.iterator();
			while (itr.hasNext()) {

				Job j = itr.next();
				itr.remove();
				return j;
			}
			return null;
		}
	}


	@Inject
	public ProtempaDeviceManager (JobDao jobDao) {

		LOGGER.debug("ProtempaDeviceManager Singleton created.");
    	this.jobDao = jobDao;
		init();
	}

	public void contextInitialized (ServletContextEvent sce) {

		//	do nothing....  managed construction
	}

	public void contextDestroyed (ServletContextEvent sce) {

		//	nothing is calling contextDestroyed currently
		this.shutdown();
	}

	private void init() {

		for (int i=0 ; i<qLen ; i++) {

			ProtempaDevice pd = new ProtempaDevice (jobDao);
			pd.setDaemon (true);
			pd.setName ("ProtempaDevice-" + i);
			pd.start();
			devices.add (pd);
		}

		jobLoaderThread = new Thread (new JobLoader());
		jobLoaderThread.setDaemon (true);
		jobLoaderThread.setName ("ETL-JobLoader");
		jobLoaderThread.start();
	}

	private void shutdown() {

		jobLoaderThread.interrupt();
		for (int i=0 ; i<qLen ; i++) {

			try {

				devices.get(i).interrupt();
				//	consider if there be jobs running and what to do.
				//	also, if protempa is doing io on an uniterruptable
				//	channel then this will hang. then the behaviour comes
				//	down to whether the thread invoking this shutdown() is
				//	a daemon thread.
			}
			catch (Exception ignore) {

			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	void qJob (Job job) {

		synchronized (jobQ) {

			LOGGER.debug("ProtempaDeviceManager qJob {0}" , job.getId());
			jobQ.add (job);
		}
	}
}
