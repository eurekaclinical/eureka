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

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.jpa.JpaPersistModule;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.cassupport.CasWebResourceWrapperFactory;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.WebResourceWrapperFactory;

import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.MockI2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaDataElementEntityDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaTimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.DataElementEntityDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaLocalUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaOAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LocalUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.OAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.email.MockEmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.TestPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGeneratorTestImpl;

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
		bind(LocalUserDao.class).to(JpaLocalUserDao.class);
		bind(RoleDao.class).to(JpaRoleDao.class);
		bind(TimeUnitDao.class).to(JpaTimeUnitDao.class);
		bind(ValueComparatorDao.class).to(JpaValueComparatorDao.class);
		bind(FrequencyTypeDao.class).to(JpaFrequencyTypeDao.class);
		bind(RelationOperatorDao.class).to(JpaRelationOperatorDao.class);
		bind(DataElementEntityDao.class).to(JpaDataElementEntityDao.class);
		bind(OAuthProviderDao.class).to(JpaOAuthProviderDao.class);
		bind(EmailSender.class).to(MockEmailSender.class);
		bind(I2b2Client.class).to(MockI2b2Client.class);
		bind(ThresholdsOperatorDao.class).to(JpaThresholdsOperatorDao.class);
		bind(new TypeLiteral<PropositionFinder<String>>(){}).to(TestPropositionFinder.class);
		bind(PasswordGenerator.class).to(PasswordGeneratorTestImpl.class);
		bind(EtlClient.class).to(MockEtlClient.class);
		bind(WebResourceWrapperFactory.class).to(CasWebResourceWrapperFactory.class);
	}
}
