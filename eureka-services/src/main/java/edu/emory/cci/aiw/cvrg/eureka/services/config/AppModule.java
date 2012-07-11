package edu.emory.cci.aiw.cvrg.eureka.services.config;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.inject.AbstractModule;
import com.google.inject.jndi.JndiIntegration;

import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaPropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
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
class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(UserDao.class).to(JpaUserDao.class);
		bind(RoleDao.class).to(JpaRoleDao.class);
		bind(FileDao.class).to(JpaFileDao.class);
		bind(PropositionDao.class).to(JpaPropositionDao.class);
		bind(EmailSender.class).to(FreeMarkerEmailSender.class);
		bind(Context.class).to(InitialContext.class);
		bind(Session.class).toProvider(
				JndiIntegration.fromJndi(Session.class,
						"java:comp/env/mail/Session"));
	}

}
