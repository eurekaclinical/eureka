package edu.emory.cci.aiw.cvrg.sample.config;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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

        bind(InjectedMessage.class).to(InjectedMessageImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);

        install(new JpaPersistModule("sample-jpa-unit"));
        filter("/api/*").through(PersistFilter.class);

        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
                "edu.emory.cci.aiw.cvrg.sample.resource");
        params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                RolesAllowedResourceFilterFactory.class.getName());
        serve("/api/*").with(GuiceContainer.class, params);
    }

}
