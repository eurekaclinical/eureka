package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.AbstractModule;

import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * Configure all the non-web related binding for Guice.
 * 
 * @author hrathod
 * 
 */
public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserDao.class).to(JpaUserDao.class);
		bind(RoleDao.class).to(JpaRoleDao.class);
	}

}
