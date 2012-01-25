package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
        return Guice.createInjector(new ServletTestModule(),
                new AppTestModule());
    }

}
