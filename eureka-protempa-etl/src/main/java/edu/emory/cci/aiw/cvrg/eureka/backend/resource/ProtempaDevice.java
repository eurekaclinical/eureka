package edu.emory.cci.aiw.cvrg.eureka.backend.resource;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.registry.CardiovascularRegistryETL;


public class ProtempaDevice extends Thread {

	private Job job;
//	private Protempa protempa;
	private CardiovascularRegistryETL etl = new CardiovascularRegistryETL();

	//
	//	IMPORTANT:
	//	the synchObj is locked by the internal thread in ProtempaDeviceManager
	//	that calls load()
	//	and is locked by the runnable inside this ProtempaDevice instance.
	//
	private final Object synchObj = new Object();
	private volatile boolean busy = false;

	private class UEH implements Thread.UncaughtExceptionHandler {

		ProtempaDevice pd = null;

		public UEH (ProtempaDevice pd) {

			this.pd = pd;
		}

		public void uncaughtException (Thread thread , Throwable t) {

			pd.tagJob (thread , t);
		}
	}


	ProtempaDevice() {

		//	create a Protempa
	}

	boolean load (Job job) {

    	System.out.println ("ETL: load ol");
		synchronized (synchObj) {

	    	System.out.println ("ETL: load il");
//			if (busy || ! isAlive()) {
//
//				return false;
//			}
//			if (protempa == null) {
//
//				setUncaughtExceptionHandler (new UEH (this));
//			}
			busy = true;
			this.job = job;
			synchObj.notifyAll();	//	there should only ever be one thread wait()ing, but still...
			return true;
		}
	}

	void kill() {

		this.interrupt();
	}

	@Override
	public void run() {

		synchronized (synchObj) {

			while (true) {
//			while (isAlive()) {

				try {

			    	System.out.println ("ETL: protempa.wait");
					synchObj.wait();
			    	System.out.println ("ETL: protempa.go");
//			    	JobEvent je = new JobEvent();
//			    	je.setState("RUNNING");
//			    	job.getJobEvents().add (je);
//					Long confId = job.getConfigurationId();
					
					//	etc...
					//
					etl.main (new String[] {"them parameters here"});
//			    	je = new JobEvent();
//			    	je.setState("FINISHED");
//			    	job.getJobEvents().add (je);
					//
					//  jobEvent.state = ...
				}
				catch (InterruptedException ie) {

					//  jobEvent.state
			    	System.out.println ("ETL: runner exc " + ie);
//			    	JobEvent je = new JobEvent();
//			    	je.setState("EXCEPTION");
//			    	job.getJobEvents().add (je);
					return;
				}
				catch (Exception e) {

					//  jobEvent.state
					e.printStackTrace();
			    	System.out.println ("ETL: runner exc " + e);
//			    	JobEvent je = new JobEvent();
//			    	je.setState("EXCEPTION");
//			    	job.getJobEvents().add (je);
					return;
				}
				finally {

					busy = false;
				}
			}
		}
	}

	protected void tagJob (Thread thread , Throwable t) {

		Job myJob = this.job;
		if (myJob != null) {

			//	recordException
		}
	}

	public static void main (String[] argv) {

		try {

			ProtempaDevice pd = new ProtempaDevice();

			pd.setDaemon (false);	//	setDaemon to true for deployment inside tomcat
			pd.start();
			System.out.println ("started");
			System.out.flush();
			pd.load (new Job());
			Thread.sleep (4096L);
			pd.interrupt();
			Thread.sleep (4096L);
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}
}
