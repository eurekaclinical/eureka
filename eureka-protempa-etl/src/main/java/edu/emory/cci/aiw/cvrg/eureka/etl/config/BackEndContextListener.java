package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.ETLServletModule;

public class BackEndContextListener extends GuiceServletContextListener {

	    @Override
	    protected Injector getInjector() {
	        Injector injector = Guice.createInjector(new ETLServletModule());
	        return injector;
	    }

		@Override
		public void contextDestroyed(ServletContextEvent servletContextEvent) {
			// TODO Auto-generated method stub
			super.contextDestroyed(servletContextEvent);
		}

		@Override
		public void contextInitialized(ServletContextEvent servletContextEvent) {
			// TODO Auto-generated method stub
			super.contextInitialized(servletContextEvent);

			servletContextEvent.getServletContext().setAttribute("", new Object());
		
		}

	    

}

