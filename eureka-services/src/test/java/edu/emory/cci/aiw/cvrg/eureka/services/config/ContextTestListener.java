package edu.emory.cci.aiw.cvrg.eureka.services.config;

import edu.emory.cci.aiw.cvrg.eureka.services.test.Setup;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Create a Guice injector for testing.
 *
 * @author hrathod
 *
 */
public class ContextTestListener extends GuiceServletContextListener {


	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContextTestListener.class);
	/**
	 * Make sure we always use the same injector.
	 */
	private final Injector injector = Guice.createInjector(
			new ServletTestModule(), new AppTestModule());
	/**
	 * The persistence service for the application.
	 */
	private PersistService persistService = null;

	@Override
	protected Injector getInjector() {
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
	}
}
