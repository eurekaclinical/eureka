package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.AbstractModule;

import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.email.FreeMarkerEmailSender;

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
		bind(FileDao.class).to(JpaFileDao.class);
		bind(EmailSender.class).to(FreeMarkerEmailSender.class);
	}

}
