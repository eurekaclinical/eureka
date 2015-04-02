package edu.emory.cci.aiw.cvrg.eureka.servlet.oauth;

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

import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.PersonNameSplitter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eurekaclinical.scribeupext.profile.EurekaProfile;
import org.scribe.up.credential.OAuthCredential;
import org.scribe.up.provider.OAuthProvider;

/**
 *
 * @author Andrew Post
 */
class RegistrationOAuthCallbackSupport<E extends EurekaProfile> {
	private final OAuthProvider provider;

	RegistrationOAuthCallbackSupport(OAuthProvider provider) {
		this.provider = provider;
	}
	
	E getProfile(HttpServletRequest req) {
		String verifier = req.getParameter(provider.getType());
		OAuthCredential credential = new OAuthCredential(null, null, verifier, provider.getType());

		return (E) provider.getUserProfile(credential);
	}
	
	void setEurekaAttributeFromProfile(HttpServletRequest req) {
		E userProfile = getProfile(req);
		String fullName = userProfile.getDisplayName();
		req.setAttribute("fullName", fullName);
		String firstName = userProfile.getFirstName();
		String lastName = userProfile.getFamilyName();
		
		if ((firstName == null || lastName == null) && fullName != null) {
			PersonNameSplitter splitter
					= new PersonNameSplitter(fullName);
			if (firstName == null) {
				firstName = splitter.getFirstName();
			}
			if (lastName == null) {
				lastName = splitter.getLastName();
			}
		}
		req.setAttribute("firstName", firstName);
		req.setAttribute("lastName", lastName);
		req.setAttribute("email", userProfile.getEmail());
		req.setAttribute("username", userProfile.getUsername());
	}

	void forwardProfileToRegisterPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setEurekaAttributeFromProfile(req);
		req.getRequestDispatcher("/register.jsp").forward(req, resp);
	}
}
