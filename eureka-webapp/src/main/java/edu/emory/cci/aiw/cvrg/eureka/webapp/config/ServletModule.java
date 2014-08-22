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


import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.config.AbstractServletModule;
import edu.emory.cci.aiw.cvrg.eureka.servlet.*;
import edu.emory.cci.aiw.cvrg.eureka.servlet.cohort.CohortHomeServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.cohort.DeleteCohortServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.cohort.EditCohortServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.cohort.SaveCohortServlet;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.HaveUserRecordFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.MessagesFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.PasswordExpiredFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.UserInfoFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.*;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author hrathod
 */
class ServletModule extends AbstractServletModule {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServletModule.class);
	private static final String CONTAINER_PATH = "/site/*";
	private static final String CONTAINER_PROTECTED_PATH = "/protected/*";
	private static final String PASSWORD_EXPIRED_REDIRECT_URL = "/protected/password_expiration.jsp";
	private static final String PASSWORD_SAVE_PATH = "/protected/user_acct";
	private static final String NO_USER_RECORD_REDIRECT_URL = "/register.jsp";
	private static final String LOGOUT_PATH = "/logout";
	
	private final WebappProperties properties;

	public ServletModule(WebappProperties inProperties) {
		super(inProperties, CONTAINER_PATH, CONTAINER_PROTECTED_PATH, 
				LOGOUT_PATH);
		this.properties = inProperties;
	}
	
	private void setupMessageFilter() {
		bind(MessagesFilter.class).in(Singleton.class);
		filter("/*").through(MessagesFilter.class);
	}
	
	private void setupHaveUserRecordFilter() {
		bind(HaveUserRecordFilter.class).in(Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("redirect-url", NO_USER_RECORD_REDIRECT_URL);
		filter(CONTAINER_PROTECTED_PATH).through(HaveUserRecordFilter.class,
				params);
	}

	private void setupUserInfoFilter () {
		bind(UserInfoFilter.class).in(Singleton.class);
		filter("/*").through(UserInfoFilter.class);
	}

	private void setupPasswordExpiredFilter() {
		bind(PasswordExpiredFilter.class).in(Singleton.class);
		Map<String, String> params = new HashMap<>();
		params.put("redirect-url", PASSWORD_EXPIRED_REDIRECT_URL);
		params.put("save-url", PASSWORD_SAVE_PATH);
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		filter(CONTAINER_PROTECTED_PATH).through(
				PasswordExpiredFilter.class, params);
	}
	
	@Override
	protected void setupFilters() {
		this.setupMessageFilter();
		this.setupUserInfoFilter();
		this.setupHaveUserRecordFilter();
		this.setupPasswordExpiredFilter();
	}
	
	@Override
	protected void setupServlets() {
		bind(RegisterUserServlet.class).in(Singleton.class);
		serve("/register").with(RegisterUserServlet.class);

		bind(ForgotPasswordServlet.class).in(Singleton.class);
		serve("/forgot_password").with(ForgotPasswordServlet.class);

		bind(LogoutServlet.class).in(Singleton.class);
		serve(LOGOUT_PATH).with(LogoutServlet.class);

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

        bind(SearchSystemPropositionServlet.class).in(Singleton.class);
        serve("/protected/searchsystemlist").with(SearchSystemPropositionServlet.class);
		
		bind(CohortHomeServlet.class).in(Singleton.class);
		serve("/protected/cohorthome").with(CohortHomeServlet.class);

		bind(EditCohortServlet.class).in(Singleton.class);
		serve("/protected/editcohort").with(EditCohortServlet.class);
		
		bind(SaveCohortServlet.class).in(Singleton.class);
		serve("/protected/savecohort").with(SaveCohortServlet.class);

		bind(DeleteCohortServlet.class).in(Singleton.class);
		serve("/protected/deletecohort").with(DeleteCohortServlet.class);
	}

}
