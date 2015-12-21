package edu.emory.cci.aiw.cvrg.eureka.services.entity;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.LoginType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthenticationMethodEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LdapUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LoginTypeEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthenticationMethodDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LoginTypeDao;

/**
 *
 * @author Andrew Post
 */
public final class UserEntityFactory {
	private final LoginTypeDao loginTypeDao;
	private final AuthenticationMethodDao authenticationMethodDao;
	
	public UserEntityFactory(LoginTypeDao inLoginTypeDao, AuthenticationMethodDao inAuthenticationMethodDao) {
		if (inLoginTypeDao == null) {
			throw new IllegalArgumentException("inLoginTypeDao cannot be null");
		}
		if (inAuthenticationMethodDao == null) {
			throw new IllegalArgumentException("inAuthenticationMethodDao cannot be null");
		}
		this.loginTypeDao = inLoginTypeDao;
		this.authenticationMethodDao = inAuthenticationMethodDao;
	}
	
	public LocalUserEntity getLocalUserEntityInstance() {
		LoginTypeEntity loginType = this.loginTypeDao.getByName(LoginType.INTERNAL);
		AuthenticationMethodEntity authenticationMethod = 
				this.authenticationMethodDao.getByName(AuthenticationMethod.LOCAL);
		return new LocalUserEntity(loginType, authenticationMethod);
	}
	
	public LdapUserEntity getLdapUserEntityInstance() {
		LoginTypeEntity loginType = this.loginTypeDao.getByName(LoginType.INTERNAL);
		AuthenticationMethodEntity authenticationMethod = 
				this.authenticationMethodDao.getByName(AuthenticationMethod.LDAP);
		return new LdapUserEntity(loginType, authenticationMethod);
	}
	
	public OAuthUserEntity getOAuthUserEntityInstance() {
		LoginTypeEntity loginType = this.loginTypeDao.getByName(LoginType.PROVIDER);
		AuthenticationMethodEntity authenticationMethod = 
				this.authenticationMethodDao.getByName(AuthenticationMethod.OAUTH);
		return new OAuthUserEntity(loginType, authenticationMethod);
	}
}
