package edu.emory.cci.aiw.cvrg.eureka.servlet;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eurekaclinical.scribeupext.provider.GitHubProvider;
import org.eurekaclinical.scribeupext.provider.GlobusProvider;
import org.eurekaclinical.scribeupext.provider.Google2Provider;
import org.eurekaclinical.scribeupext.provider.SSLTwitterProvider;
import org.scribe.up.session.HttpUserSession;

/**
 *
 * @author Andrew Post
 */
public class ChooseAccountTypeServlet extends HttpServlet {
	private final WebappProperties properties;

	@Inject
	public ChooseAccountTypeServlet(WebappProperties inProperties) {
		this.properties = inProperties;
	}
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean oauthRegistrationEnabled = this.properties.isOAuthRegistrationEnabled();
		req.setAttribute("oauthRegistrationEnabled", oauthRegistrationEnabled);
		
		boolean localAccountRegistrationEnabled = this.properties.isLocalAccountRegistrationEnabled();
		
		String webappUrl = this.properties.getApplicationUrl(req);
		
		if (oauthRegistrationEnabled) {
			HttpUserSession userSession = new HttpUserSession(req);
			String authorizationUrl = null;
			int countEnabled = 0;
			boolean googleAuthEnabled = this.properties.isGoogleOAuthRegistrationEnabled();
			req.setAttribute("googleAuthEnabled", googleAuthEnabled);
			if (googleAuthEnabled) {
				Google2Provider googleProvider = new Google2Provider();
				googleProvider.setKey(this.properties.getGoogleOAuthKey());
				googleProvider.setSecret(this.properties.getGoogleOAuthSecret());
				googleProvider.setCallbackUrl(webappUrl + "registrationoauthgooglecallback");
				authorizationUrl = googleProvider.getAuthorizationUrl(userSession);
				req.setAttribute("Google2ProviderUrl", authorizationUrl);
				countEnabled++;
			}
			
			boolean gitHubAuthEnabled = this.properties.isGitHubOAuthRegistrationEnabled();
			req.setAttribute("gitHubAuthEnabled", gitHubAuthEnabled);
			if (gitHubAuthEnabled) {
				GitHubProvider gitHubProvider = new GitHubProvider();
				gitHubProvider.setKey(this.properties.getGitHubOAuthKey());
				gitHubProvider.setSecret(this.properties.getGitHubOAuthSecret());
				gitHubProvider.setCallbackUrl(webappUrl + "registrationoauthgithubcallback");
				authorizationUrl = gitHubProvider.getAuthorizationUrl(userSession);
				req.setAttribute("GitHubProviderUrl", authorizationUrl);
				countEnabled++;
			}
			
			boolean twitterAuthEnabled = this.properties.isTwitterOAuthRegistrationEnabled();
			req.setAttribute("twitterAuthEnabled", twitterAuthEnabled);
			if (twitterAuthEnabled) {
				SSLTwitterProvider twitterProvider = new SSLTwitterProvider();
				twitterProvider.setKey(this.properties.getTwitterOAuthKey());
				twitterProvider.setSecret(this.properties.getTwitterOAuthSecret());
				twitterProvider.setCallbackUrl(webappUrl + "registrationoauthtwittercallback");
				authorizationUrl = twitterProvider.getAuthorizationUrl(userSession);
				req.setAttribute("SSLTwitterProviderUrl", authorizationUrl);
				countEnabled++;
			}
			
			boolean globusAuthEnabled = this.properties.isGlobusOAuthRegistrationEnabled();
			req.setAttribute("globusAuthEnabled", globusAuthEnabled);
			if (globusAuthEnabled) {
				GlobusProvider globusProvider = new GlobusProvider();
				globusProvider.setKey(this.properties.getGlobusOAuthKey());
				globusProvider.setSecret(this.properties.getGlobusOAuthSecret());
				globusProvider.setCallbackUrl(webappUrl + "registrationoauthglobuscallback");
				authorizationUrl = globusProvider.getAuthorizationUrl(userSession);
				req.setAttribute("GlobusProviderUrl", authorizationUrl);
				countEnabled++;
			}
			
			if (countEnabled == 1 && !localAccountRegistrationEnabled) {
				resp.sendRedirect(authorizationUrl);
				return;
			}
		}
		
		if (localAccountRegistrationEnabled && !oauthRegistrationEnabled) {
			resp.sendRedirect(req.getContextPath() + "/register.jsp");
		} else if (!localAccountRegistrationEnabled && !oauthRegistrationEnabled) {
			throw new ServletException("Registration is disabled");
		} else {
			req.setAttribute("localAccountRegistrationEnabled", localAccountRegistrationEnabled);
			req.getRequestDispatcher("/choose_account_type.jsp").forward(req, resp);
		}
	}
	
}
