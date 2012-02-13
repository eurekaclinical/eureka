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
	/**
	 * Make sure we always use the same injector
	 */
	private Injector injector = null;
	/**
	 * The persistence service for the application.
	 */
	private PersistService persistService = null;

	@Override
	synchronized protected Injector getInjector() {
		if (this.injector == null) {
			this.injector = Guice.createInjector(new ServletModule(),
					new AppModule());
		}
		return this.injector;
	}

	/*
	 * @see
	 * com.google.inject.servlet.GuiceServletContextListener#contextInitialized
	 * (javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		this.persistService = this.getInjector().getInstance(
				PersistService.class);
		this.persistService.start();
		Bootstrap bootstrap = this.getInjector().getInstance(Bootstrap.class);
		bootstrap.configure();
		try {
			ApplicationProperties applicationProperties = this.getInjector()
					.getInstance(ApplicationProperties.class);
			this.jobUpdateThread = new JobUpdateThread(
					applicationProperties.getEtlJobUpdateUrl());
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
		this.persistService.stop();
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
