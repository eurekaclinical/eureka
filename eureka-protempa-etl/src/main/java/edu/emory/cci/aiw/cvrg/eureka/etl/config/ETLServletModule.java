package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JpaJobDao;

/**
 * @author hrathod
 * 
 */
public class ETLServletModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {

    	bind (JobDao.class).to(JpaJobDao.class);
    	bind (ConfDao.class).to(JpaConfDao.class);

        install(new JpaPersistModule("backend-jpa-unit"));        

//        filter("/api/*").through(PersistFilter.class);

        Map<String, String> params = new HashMap<String, String>();
		params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES,
                "edu.emory.cci.aiw.cvrg.eureka.etl.resource");
        params.put(PackagesResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
                RolesAllowedResourceFilterFactory.class.getName());

        serve("/api/*").with(GuiceContainer.class, params);
    }
}
