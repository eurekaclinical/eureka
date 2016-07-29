/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import com.google.inject.AbstractModule;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.google.inject.TypeLiteral;
import com.google.inject.jndi.JndiIntegration;

import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2Client;
import edu.emory.cci.aiw.cvrg.eureka.services.clients.I2b2RestClient;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthenticationMethodDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaFrequencyTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaPhenotypeEntityDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaRoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaTimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaAuthenticationMethodDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaLocalUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaLoginTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaOAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.JpaUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LocalUserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LoginTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.OAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RelationOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ThresholdsOperatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.TimeUnitDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.ValueComparatorDao;
import edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.email.FreeMarkerEmailSender;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.PropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.util.PasswordGeneratorImpl;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PhenotypeEntityDao;
import org.eurekaclinical.common.comm.clients.WebResourceWrapperFactory;
import org.eurekaclinical.common.comm.clients.cassupport.CasWebResourceWrapperFactory;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.entity.RoleEntity;
import org.eurekaclinical.standardapis.entity.UserEntity;

/**
 * Configure all the non-web related binding for Guice.
 *
 * @author hrathod
 *
 */
class AppModule extends AbstractModule {
	
	AppModule() {
	}

	@Override
	protected void configure() {
		bind(new TypeLiteral<UserDao<? extends UserEntity<? extends RoleEntity>>>() {}).to(JpaUserDao.class);
		bind(edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao.class).to(JpaUserDao.class);
		bind(LocalUserDao.class).to(JpaLocalUserDao.class);
		bind(RoleDao.class).to(JpaRoleDao.class);
		bind(PhenotypeEntityDao.class).to(JpaPhenotypeEntityDao.class);
		bind(TimeUnitDao.class).to(JpaTimeUnitDao.class);
		bind(RelationOperatorDao.class).to(JpaRelationOperatorDao.class);
		bind(ValueComparatorDao.class).to(JpaValueComparatorDao.class);
		bind(ThresholdsOperatorDao.class).to(JpaThresholdsOperatorDao.class);
		bind(FrequencyTypeDao.class).to(JpaFrequencyTypeDao.class);
		bind(ThresholdsOperatorDao.class).to
				(JpaThresholdsOperatorDao.class);
		bind(OAuthProviderDao.class).to(JpaOAuthProviderDao.class);
		bind(AuthenticationMethodDao.class).to(JpaAuthenticationMethodDao.class);
		bind(LoginTypeDao.class).to(JpaLoginTypeDao.class);
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
