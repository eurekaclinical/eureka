package edu.emory.cci.aiw.cvrg;

import org.protempa.Protempa;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
//import edu.emory.cci.registry.CardiovascularRegistryETL;


public class ProtempaDevice extends Thread {

	private Job job;
	private Protempa protempa;
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

		synchronized (synchObj) {

			if (busy || ! isAlive()) {

				return false;
			}
			if (protempa == null) {

				setUncaughtExceptionHandler (new UEH (this));
			}
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

			while (isAlive()) {

				try {

					synchObj.wait();
					Long confId = job.getConfigurationId();
					//	etc...
					//
					//	CardiovascularRegistryETL.main (new String[] {"them parameters here"});
					//
					//  jobEvent.state = ...
				}
				catch (InterruptedException ie) {

					//  jobEvent.state
					return;
				}
				catch (Exception e) {

					//  jobEvent.state
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
