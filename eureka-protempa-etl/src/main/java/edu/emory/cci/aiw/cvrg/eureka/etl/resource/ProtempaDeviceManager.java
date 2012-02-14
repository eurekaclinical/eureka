package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

//	this is a singleton that should be bound to the equivalent of an
//	application-scope context variable.  this is written with the
//	assumption that there is only one instance in the entire distributed
//	application of this singleton.
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

	private List<Job> jobQ = new ArrayList<Job> (1<<4);
	private List<ProtempaDevice> devices = new ArrayList<ProtempaDevice>(4);
	private final JobDao jobDao;
	private Thread jobLoaderThread;

	private class JobLoader implements Runnable {

		public void run() {

			while (true) {

				try {

			    	System.out.println ("ETL: JobLoader loop");
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
									protempaDevice.load (j);
								}
								break;
							}
						}
						for (int i=0 ; i<devices.size() ; i++) {

							ProtempaDevice protempaDevice = devices.get(i);
							if (protempaDevice.hasFailed()) {

								ProtempaDevice pd = new ProtempaDevice (jobDao);
								pd.setDaemon (true);
								pd.setName ("ProtempaDevice-" + i);
								pd.start();

								ProtempaDevice broken = devices.set (i , pd);
								Job unknownStateJob = broken.getJob();
								unknownStateJob.setNewState ("FAILED" , "job recovered from a broken protempa" , null);
								jobDao.save (unknownStateJob);
							}
						}
					}
				}
				catch (InterruptedException ie) {

					return;
				}
				catch (Exception e) {

			    	System.out.println ("ETL: JobLoader exception " + e);
					e.printStackTrace();
					return;
				}
			}
		}

		private Job getNextJob() {

			//	synchronized jobQ access via client context.
			//	don't use without synch'ing on jobQ.
			Job ret = null;
			Iterator<Job> itr = jobQ.iterator();
			while (itr.hasNext()) {

				ret = itr.next();
				itr.remove();
				if (ret != null) {

					return ret;
				}
			}
			return ret;
		}
	}


	@Inject
	public ProtempaDeviceManager (JobDao jobDao) {

    	System.out.println ("ETL: new ProtempaDeviceManager");
    	this.jobDao = jobDao;
		init();
	}

	public void contextInitialized (ServletContextEvent sce) {

		//	do nothing....  managed construction
	}

	public void contextDestroyed (ServletContextEvent sce) {

		this.shutdown();
	}

	private void init() {

		for (int i=0 ; i<4 ; i++) {

	    	System.out.println ("ETL: create new ProtempaDevice");
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
		for (int i=0 ; i<8 ; i++) {

			devices.get(i).interrupt();
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	void qJob (Job job) {

    	System.out.println ("ETL: qJob ol");
		synchronized (jobQ) {

	    	System.out.println ("ETL: qJob il");
			jobQ.add (job);
		}
	}
}
