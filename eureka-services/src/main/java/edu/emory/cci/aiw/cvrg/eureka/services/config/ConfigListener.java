package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Set up the Guice dependency injection engine. Uses two modules:
 * {@link ServletModule} for web related configuration, and {@link AppModule}
 * for non-web related configuration.
 * 
 * @author hrathod
 * 
 */
public class ConfigListener extends GuiceServletContextListener {

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
}
