package edu.emory.cci.aiw.cvrg.eureka.services.util;

/*
 * #%L
 * Eureka Common
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.LdapUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.LocalUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.OAuthUserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequestVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RoleEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.util.StringUtil;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthenticationMethodDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LoginTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.OAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserEntityFactory;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 *
 * @author Andrew Post
 */
public class UserRequestToUserEntityVisitor implements UserRequestVisitor {
	private UserEntity userEntity;
	private final OAuthProviderDao oauthProviderDao;
	private final RoleDao roleDao;
	private final UserEntityFactory userEntityFactory;
	private final LoginTypeDao loginTypeDao;
	private final AuthenticationMethodDao authenticationMethodDao;

	public UserRequestToUserEntityVisitor(OAuthProviderDao inOAuthProviderDao,
			RoleDao inRoleDao, LoginTypeDao inLoginTypeDao,
			AuthenticationMethodDao inAuthenticationMethodDao) {
		this.oauthProviderDao = inOAuthProviderDao;
		this.roleDao = inRoleDao;
		this.loginTypeDao = inLoginTypeDao;
		this.authenticationMethodDao = inAuthenticationMethodDao;
		this.userEntityFactory = new UserEntityFactory(this.loginTypeDao, this.authenticationMethodDao);
	}
	
	@Override
	public void visit(LocalUserRequest localUserRequest) {
		LocalUserEntity localUserEntity = this.userEntityFactory.getLocalUserEntityInstance();
		populateUserEntityFields(localUserEntity, localUserRequest);
		
		try {
			localUserEntity.setPassword(StringUtil.md5(localUserRequest.getPassword()));
		} catch (NoSuchAlgorithmException e1) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, e1);
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 12);
		localUserEntity.setPasswordExpiration(cal.getTime());
		localUserEntity.setVerificationCode(UUID.randomUUID().toString());
		localUserEntity.setVerified(false);
		this.userEntity = localUserEntity;
	}

	@Override
	public void visit(LdapUserRequest ldapUserRequest) {
		this.userEntity = this.userEntityFactory.getLdapUserEntityInstance();
		populateUserEntityFields(this.userEntity, ldapUserRequest);
	}

	@Override
	public void visit(OAuthUserRequest oauthUserRequest) {
		OAuthUserEntity oauthUserEntity = this.userEntityFactory.getOAuthUserEntityInstance();
		populateUserEntityFields(oauthUserEntity, oauthUserRequest);
		oauthUserEntity.setProviderUsername(oauthUserRequest.getProviderUsername());
		oauthUserEntity.setOAuthProvider(this.oauthProviderDao.getByName(oauthUserRequest.getOAuthProvider()));
		this.userEntity = oauthUserEntity;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}
	
	private void populateUserEntityFields(UserEntity userEntity, UserRequest userRequest) {
		userEntity.setUsername(userRequest.getUsername());
		userEntity.setEmail(userRequest.getEmail());
		userEntity.setFirstName(userRequest.getFirstName());
		userEntity.setLastName(userRequest.getLastName());
		userEntity.setFullName(userRequest.getFullName());
		userEntity.setDepartment(userRequest.getDepartment());
		userEntity.setCreated(new Date());
		userEntity.setOrganization(userRequest.getOrganization());
		userEntity.setRoles(getDefaultRoles());
		userEntity.setTitle(userRequest.getTitle());
		userEntity.setActive(false);
	}
	
	/**
	 * Get a set of default roles to be added to a newly created user.
	 *
	 * @return A list of default roles.
	 */
	private List<RoleEntity> getDefaultRoles() {
		List<RoleEntity> defaultRoles = new ArrayList<>();
		for (RoleEntity role : this.roleDao.getAll()) {
			if (Boolean.TRUE.equals(role.isDefaultRole())) {
				defaultRoles.add(role);
			}
		}
		return defaultRoles;
	}

}
