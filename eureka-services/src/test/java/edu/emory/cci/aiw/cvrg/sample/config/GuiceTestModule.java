package edu.emory.cci.aiw.cvrg.sample.config;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import edu.emory.cci.aiw.cvrg.sample.dao.UserDao;
import edu.emory.cci.aiw.cvrg.sample.dao.UserDaoImpl;
import edu.emory.cci.aiw.cvrg.sample.model.InjectedMessage;
import edu.emory.cci.aiw.cvrg.sample.model.InjectedTestMessage;

/**
 * Configure Guice for non-web application testing.
 * 
 * @author hrathod
 * 
 */
public class GuiceTestModule extends AbstractModule {

    @Override
    protected void configure() {

        install(new JpaPersistModule("sample-jpa-unit"));

        bind(InjectedMessage.class).to(InjectedTestMessage.class);
        bind(UserDao.class).to(UserDaoImpl.class);

    }

}
