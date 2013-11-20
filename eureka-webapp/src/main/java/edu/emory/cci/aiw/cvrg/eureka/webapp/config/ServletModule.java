package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*
 * #%L
 * Eureka WebApp
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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.config.AbstractServletModule;
import edu.emory.cci.aiw.cvrg.eureka.servlet.AdminManagerServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.CommonsFileUploadServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.DateRangeDataElementServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.ForgotPasswordServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.JobListServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.JobPollServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.LoginServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.LogoutServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.PingServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.RegisterUserServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.UserAcctManagerServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.VerifyUserServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.MessagesFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.PasswordExpiredFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.DeletePropositionServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.EditPropositionServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.EditorHomeServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.ListUserDefinedPropositionChildrenServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.SavePropositionServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.SystemPropositionListServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.UserPropositionListServlet;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ServicesClientProvider;

/**
 *
 * @author hrathod
 */
class ServletModule extends AbstractServletModule {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServletModule.class);
	private static final String PROPERTY_PACKAGE_NAMES = "edu.emory.cci.aiw.cvrg.eureka.webapp.resource;edu.emory.cci.aiw.cvrg.eureka.servlet";
	private static final String REDIRECT_URL = "/protected/password_expiration.jsp";
	private static final String PASSWORD_SAVE_PATH = "/protected/user_acct";
	private static final String CAS_URL = "https://localhost:8443/cas-server";
	private final String contextPath;
	private final WebappProperties properties;

	public ServletModule(WebappProperties inProperties) {
		super();
		this.contextPath = this.getServletContext().getContextPath();
		this.properties = inProperties;
	}

	private void setupPasswordExpiredFilter() {
		bind(PasswordExpiredFilter.class).in(Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("redirect-url", REDIRECT_URL);
		params.put("save-url", PASSWORD_SAVE_PATH);
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		filter("/protected/*").through(PasswordExpiredFilter.class, params);
	}

	private void setupMessageFilter() {
		bind(MessagesFilter.class).in(Singleton.class);
		filter("/*").through(MessagesFilter.class);
	}

	private void setupServlets() {
		bind(RegisterUserServlet.class).in(Singleton.class);
		serve("/register").with(RegisterUserServlet.class);

		bind(ForgotPasswordServlet.class).in(Singleton.class);
		serve("/forgot_password").with(ForgotPasswordServlet.class);

		bind(LogoutServlet.class).in(Singleton.class);
		serve("/logout").with(LogoutServlet.class);

		bind(VerifyUserServlet.class).in(Singleton.class);
		serve("/verify").with(VerifyUserServlet.class);

		bind(CommonsFileUploadServlet.class).in(Singleton.class);
		Map<String, String> uploadParams = new HashMap<>();
		uploadParams.put("dest_dir", this.properties.getUploadDir());
		serve("/protected/upload").with(CommonsFileUploadServlet.class,
				uploadParams);

		bind(LoginServlet.class).in(Singleton.class);
		serve("/protected/login").with(LoginServlet.class);

		bind(JobPollServlet.class).in(Singleton.class);
		serve("/protected/jobpoll").with(JobPollServlet.class);

		bind(JobListServlet.class).in(Singleton.class);
		serve("/protected/jobs").with(JobListServlet.class);

		bind(AdminManagerServlet.class).in(Singleton.class);
		serve("/protected/admin").with(AdminManagerServlet.class);

		bind(UserAcctManagerServlet.class).in(Singleton.class);
		serve("/protected/user_acct").with(UserAcctManagerServlet.class);

		bind(EditorHomeServlet.class).in(Singleton.class);
		serve("/protected/editorhome").with(EditorHomeServlet.class);

		bind(SystemPropositionListServlet.class).in(Singleton.class);
		serve("/protected/systemlist").with(SystemPropositionListServlet.class);

		bind(SavePropositionServlet.class).in(Singleton.class);
		serve("/protected/saveprop").with(SavePropositionServlet.class);

		bind(DeletePropositionServlet.class).in(Singleton.class);
		serve("/protected/deleteprop").with(DeletePropositionServlet.class);

		bind(UserPropositionListServlet.class).in(Singleton.class);
		serve("/protected/userproplist").with(UserPropositionListServlet.class);

		bind(ListUserDefinedPropositionChildrenServlet.class).in(
				Singleton.class);
		serve("/protected/userpropchildren").with(
				ListUserDefinedPropositionChildrenServlet.class);

		bind(EditPropositionServlet.class).in(Singleton.class);
		serve("/protected/editprop").with(EditPropositionServlet.class);

		bind(DateRangeDataElementServlet.class).in(Singleton.class);
		serve("/protected/destinationdataelements").with(
				DateRangeDataElementServlet.class);

		bind(PingServlet.class).in(Singleton.class);
		serve("/protected/ping").with(PingServlet.class);
	}

	private void bindProviders () {
		bind(ServicesClient.class).toProvider(ServicesClientProvider.class);
	}

	@Override
	protected void configureServlets() {
		super.configureServlets();
		this.setupServlets();
		this.bindProviders();
		this.setupPasswordExpiredFilter();
		this.setupMessageFilter();
	}

	@Override
	protected String getContextPath() {
		return this.contextPath;
	}

	@Override
	protected String getPackageNames() {
		return PROPERTY_PACKAGE_NAMES;
	}

	@Override
	protected String getServerName() {
		return this.properties.getServerName();
	}

	@Override
	protected String getCasUrl() {
		return this.properties.getCasUrl();
	}
}