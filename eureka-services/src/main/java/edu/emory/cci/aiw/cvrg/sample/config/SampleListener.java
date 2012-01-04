package edu.emory.cci.aiw.cvrg.sample.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * A sample Guice configuration class, to show set up of the Jersey framework,
 * including package scanning of resources, and authorization via the
 * RolesAllowed filter.
 * 
 * @author hrathod
 * 
 */
public class SampleListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new SampleServletModule());
        return injector;
    }
}
