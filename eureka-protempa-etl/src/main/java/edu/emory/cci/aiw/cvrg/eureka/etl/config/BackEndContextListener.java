package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Loaded up on application initialization, sets up the application with Guice
 * injector and any other bootup processes.
 * 
 * @author hrathod
 * 
 */
public class BackEndContextListener extends GuiceServletContextListener {

	/**
	 * The Guice injector used to fetch instances of dependency injection
	 * enabled classes.
	 */
	private final Injector injector = Guice
			.createInjector(new ETLServletModule());
	/**
	 * The JPA persistence service, used to interact with the data store.
	 */
	private PersistService ps;

	@Override
	protected Injector getInjector() {
		return this.injector;
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		super.contextDestroyed(servletContextEvent);
		this.ps.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		this.ps = this.getInjector().getInstance(PersistService.class);
		this.ps.start();
		servletContextEvent.getServletContext().setAttribute("", new Object());
	}
}
