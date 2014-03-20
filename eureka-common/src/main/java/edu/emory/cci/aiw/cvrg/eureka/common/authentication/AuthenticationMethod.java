package edu.emory.cci.aiw.cvrg.eureka.common.authentication;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.LdapUser;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LdapUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUser;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.OAuthUser;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.OAuthUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
/**
 *
 * @author Andrew Post
 */
public enum AuthenticationMethod {

	LOCAL(LoginType.INTERNAL),
	LDAP(LoginType.INTERNAL),
	OAUTH(LoginType.PROVIDER);

	public static AuthenticationMethod userRequestToAuthenticationMethod(UserRequest userRequest) {
		AuthenticationMethod authMethod;
		if (userRequest instanceof LocalUserRequest) {
			authMethod = AuthenticationMethod.LOCAL;
		} else if (userRequest instanceof LdapUserRequest) {
			authMethod = AuthenticationMethod.LDAP;
		} else if (userRequest instanceof OAuthUserRequest) {
			authMethod = AuthenticationMethod.OAUTH;
		} else {
			throw new AssertionError("Unspected userRequest type: " + userRequest.getClass().getName());
		}
		return authMethod;
	}
	
	public static AuthenticationMethod userToAuthenticationMethod(User user) {
		AuthenticationMethod authMethod;
		if (user instanceof LocalUser) {
			authMethod = AuthenticationMethod.LOCAL;
		} else if (user instanceof LdapUser) {
			authMethod = AuthenticationMethod.LDAP;
		} else if (user instanceof OAuthUser) {
			authMethod = AuthenticationMethod.OAUTH;
		} else {
			throw new AssertionError("Unspected userRequest type: " + user.getClass().getName());
		}
		return authMethod;
	}

	private final LoginType loginType;

	private AuthenticationMethod(LoginType loginType) {
		this.loginType = loginType;
	}

	public LoginType getLoginType() {
		return loginType;
	}

}
