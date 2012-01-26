package edu.emory.cci.aiw.cvrg;

import org.protempa.Protempa;


public class ProtempaDevice extends Thread {

	private Job job;
	private Protempa protempa;
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


	public ProtempaDevice() {

	}

	boolean dropOffJob (Job job) {

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

	Job getJob() {

		return this.job;
	}

	void kill() {

		this.interrupt();
	}

	@Override
	public void run() {

		synchronized (synchObj) {

			while ( ! isAlive()) {

				try {

					synchObj.wait();
//						protempa.execute (query, resultsHandler);
				}
				catch (InterruptedException ie) {

					return;
				}
				catch (Exception e) {

					//job.pushState (e.getStackTrace());
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
			pd.dropOffJob (new Job());
			Thread.sleep (4096L);
			pd.interrupt();
			Thread.sleep (4096L);
		}
		catch (Exception e) {

			e.printStackTrace();
		}
	}
}
