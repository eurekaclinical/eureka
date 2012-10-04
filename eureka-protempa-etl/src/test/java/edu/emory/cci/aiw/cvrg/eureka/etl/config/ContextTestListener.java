package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;

/**
 *
 * @author hrathod
 */
public class ContextTestListener extends GuiceServletContextListener {

	private final Injector injector = Guice.createInjector(new AppTestModule(),
			new ServletTestModule());
	private PersistService persistService;

	@Override
	protected final Injector getInjector() {
		return this.injector;
	}

	@Override
	public void contextInitialized(ServletContextEvent inServletContextEvent) {
		super.contextInitialized(inServletContextEvent);
		this.persistService = this.getInjector().getInstance(
				PersistService.class);
		this.persistService.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent inServletContextEvent) {
		super.contextDestroyed(inServletContextEvent);
		if (this.persistService != null) {
			this.persistService.stop();
		}
		TaskManager taskManager = this.injector.getInstance(TaskManager.class);
		taskManager.shutdown();
	}
}
