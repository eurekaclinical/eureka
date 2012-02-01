package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobUpdateThread;

/**
 * Set up the Guice dependency injection engine. Uses two modules:
 * {@link ServletModule} for web related configuration, and {@link AppModule}
 * for non-web related configuration.
 * 
 * @author hrathod
 * 
 */
public class ConfigListener extends GuiceServletContextListener {

	/**
	 * A thread that updates the status of all jobs.
	 */
	private JobUpdateThread jobUpdateThread;

	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(new ServletModule(),
				new AppModule());

		// Start the persist service, so we can bootstrap the data. If
		// bootstrapping is not needed, it is better to enable the
		// PersisteFilter in the ServletModule class.
		PersistService persistService = injector
				.getInstance(PersistService.class);
		persistService.start();

		Bootstrap bootstrap = injector.getInstance(Bootstrap.class);
		bootstrap.configure();

		return injector;
	}

	/*
	 * @see
	 * com.google.inject.servlet.GuiceServletContextListener#contextInitialized
	 * (javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		String backendUrl = inServletContextEvent.getServletContext()
				.getInitParameter("backend-url");
		try {
			this.jobUpdateThread = new JobUpdateThread(backendUrl);
			this.jobUpdateThread.setDaemon(true);
			this.jobUpdateThread.start();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @see
	 * com.google.inject.servlet.GuiceServletContextListener#contextDestroyed
	 * (javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent inServletContextEvent) {
		super.contextDestroyed(inServletContextEvent);
		this.jobUpdateThread.setKeepRunning(false);
		this.jobUpdateThread.interrupt();
		try {
			this.jobUpdateThread.join(20000);
		} catch (InterruptedException e) {
			// do nothing, we're about to shut down.
			e.printStackTrace();
		}
	}
}
