package edu.emory.cci.aiw.cvrg.eureka.services.config;

import javax.servlet.ServletContextEvent;

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
