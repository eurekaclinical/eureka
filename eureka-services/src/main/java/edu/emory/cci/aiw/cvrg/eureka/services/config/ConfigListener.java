package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
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
	private final Injector injector = Guice.createInjector(new ServletModule(),
			new AppModule(),new JpaPersistModule("services-jpa-unit"));
	/**
	 * The persistence service for the application.
	 */
	private PersistService persistService = null;

	@Override
	protected Injector getInjector() {
		return this.injector;
	}

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigListener.class);

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
		try {
			bootstrap.configure();
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
		try {
			ApplicationProperties applicationProperties = this.getInjector()
					.getInstance(ApplicationProperties.class);
			this.jobUpdateThread = new JobUpdateThread(
					applicationProperties.getEtlJobUpdateUrl());
			this.jobUpdateThread.setDaemon(true);
			this.jobUpdateThread.start();
		} catch (KeyManagementException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage(), e);
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
		this.jobUpdateThread.unsetKeepRunning();
		this.jobUpdateThread.interrupt();
		try {
			this.jobUpdateThread.join(20000);
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
