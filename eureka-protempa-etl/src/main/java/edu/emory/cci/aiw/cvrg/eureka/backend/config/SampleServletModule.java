package edu.emory.cci.aiw.cvrg.eureka.backend.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

//import edu.emory.cci.aiw.cvrg.eureka.services.config.JobUpdateThread;
import edu.emory.cci.aiw.cvrg.sample.dao.UserDao;
import edu.emory.cci.aiw.cvrg.sample.dao.UserDaoImpl;
import edu.emory.cci.aiw.cvrg.sample.model.InjectedMessage;
import edu.emory.cci.aiw.cvrg.sample.model.InjectedMessageImpl;

/**
 * @author hrathod
 * 
 */
public class SampleServletModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {

        bind(UserDao.class).to(UserDaoImpl.class);

        install(new JpaPersistModule("backend-jpa-unit"));
        filter("/api/*").through(PersistFilter.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
                "edu.emory.cci.aiw.cvrg.eureka.backend.resource");
        params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                RolesAllowedResourceFilterFactory.class.getName());
        serve("/api/*").with(GuiceContainer.class, params);
    }
}
