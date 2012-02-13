package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.servlet.GuiceServletContextListener;

public class BackEndContextListener extends GuiceServletContextListener {

	private final Injector injector = Guice.createInjector(new ETLServletModule());
	private PersistService ps;


	@Override
    protected Injector getInjector() {

        return injector;
    }

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextDestroyed(servletContextEvent);
		this.ps.stop();
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		super.contextInitialized(servletContextEvent);
		ps = getInjector().getInstance(PersistService.class);
		ps.start();
		servletContextEvent.getServletContext().setAttribute("", new Object());	
	}
}

