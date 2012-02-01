package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;

import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;

/**
 * Configure Guice for non-web application testing.
 * 
 * @author hrathod
 * 
 */
public class AppTestModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new JpaPersistModule("services-jpa-unit"));

		bind(UserDao.class).to(JpaUserDao.class);
		bind(RoleDao.class).to(JpaRoleDao.class);
		bind(FileDao.class).to(JpaFileDao.class);

	}

}
