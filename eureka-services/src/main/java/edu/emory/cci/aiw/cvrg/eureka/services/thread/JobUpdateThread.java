package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;

/**
 * A thread started at startup time to monitor the status of jobs that have been
 * submitted to the back-end.
 * 
 * @author hrathod
 * 
 */
@Singleton
public class JobUpdateThread extends Thread {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobUpdateThread.class);
	/**
	 * The back-end URL needed to make the REST calls for job updates.
	 */
	private final String backendUrl;
	/**
	 * The secure Jersey client used to make the REST calls.
	 */
	private final Client client;
	/**
	 * Used to indicate whether the job should stop running or continue.
	 */
	private boolean keepRunning;

	/**
	 * Create a job update thread using the given backend url.
	 * 
	 * @param inBackendUrl The backend url used to make the update calls.
	 * @throws NoSuchAlgorithmException Thrown when the secure client can not be
	 *             created properly.
	 * @throws KeyManagementException Thrown when the secure client can not be
	 *             create properly.
	 */
	public JobUpdateThread(String inBackendUrl) throws KeyManagementException,
			NoSuchAlgorithmException {
		this.backendUrl = inBackendUrl;
		this.client = CommUtils.getClient();
		this.keepRunning = true;
	}

	@Override
	public void run() {
		while (this.keepRunning) {
			try {
				List<Job> updatedJobs = this.client.resource(this.backendUrl)
						.accept(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<Job>>() {
							// nothing to implement
						});
				if (updatedJobs != null) {
					for (Job job : updatedJobs) {
						LOGGER.debug("Updated job: {}", job.toString());
					}
					JobCollection.setJobs(updatedJobs);
				} else {
					LOGGER.debug("Job update thread received null");
				}
				Thread.sleep(10000);
			} catch (InterruptedException ie) {
				// do nothing, we just try again.
			}
		}
	}

	/**
	 * @return the keepRunning
	 */
	public boolean isKeepRunning() {
		return this.keepRunning;
	}

	/**
	 * @param inKeepRunning the keepRunning to set
	 */
	public void setKeepRunning(boolean inKeepRunning) {
		this.keepRunning = inKeepRunning;
	}
}
