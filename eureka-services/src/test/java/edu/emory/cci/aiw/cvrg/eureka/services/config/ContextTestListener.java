package edu.emory.cci.aiw.cvrg.eureka.services.config;

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

	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(new ServletTestModule(),
				new AppTestModule());

		PersistService persistService = injector
				.getInstance(PersistService.class);
		persistService.start();

		Bootstrap bootstrap = injector.getInstance(Bootstrap.class);
		bootstrap.configure();

		return injector;
	}

}
