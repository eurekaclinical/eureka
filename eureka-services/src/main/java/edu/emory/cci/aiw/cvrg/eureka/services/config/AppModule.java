/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.jndi.JndiIntegration;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.cassupport.CasWebResourceWrapperFactory;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.WebResourceWrapperFactory;

import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2RestClient;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaPropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaTimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.email.FreeMarkerEmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGeneratorImpl;

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
		bind(PropositionDao.class).to(JpaPropositionDao.class);
		bind(TimeUnitDao.class).to(JpaTimeUnitDao.class);
		bind(RelationOperatorDao.class).to(JpaRelationOperatorDao.class);
		bind(ValueComparatorDao.class).to(JpaValueComparatorDao.class);
		bind(ThresholdsOperatorDao.class).to(JpaThresholdsOperatorDao.class);
		bind(FrequencyTypeDao.class).to(JpaFrequencyTypeDao.class);
		bind(ThresholdsOperatorDao.class).to
				(JpaThresholdsOperatorDao.class);
		bind(new TypeLiteral<PropositionFinder<
				String>>(){}).to(SystemPropositionFinder.class);
		bind(EmailSender.class).to(FreeMarkerEmailSender.class);
		bind(I2b2Client.class).to(I2b2RestClient.class);
		bind(Context.class).to(InitialContext.class);
		bind(Session.class).toProvider(
				JndiIntegration.fromJndi(Session.class,
						"java:comp/env/mail/Session"));
		bind(PasswordGenerator.class).to(PasswordGeneratorImpl.class);
		bind(EtlClient.class).to(EtlClientImpl.class);
		bind(WebResourceWrapperFactory.class).to(CasWebResourceWrapperFactory.class);
	}

}
