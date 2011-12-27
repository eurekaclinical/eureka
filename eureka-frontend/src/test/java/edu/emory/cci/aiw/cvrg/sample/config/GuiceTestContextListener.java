package edu.emory.cci.aiw.cvrg.sample.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Create a Guice injector for testing.
 * 
 * @author hrathod
 * 
 */
public class GuiceTestContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletTestModule(),
                new GuiceTestModule());
    }

}
