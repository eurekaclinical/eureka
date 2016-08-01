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

import org.eurekaclinical.eureka.client.comm.LdapUser;
import org.eurekaclinical.eureka.client.comm.LocalUser;
import org.eurekaclinical.eureka.client.comm.OAuthUser;
import org.eurekaclinical.eureka.client.comm.User;
import org.eurekaclinical.eureka.client.comm.UserVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LocalUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RoleEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.AuthenticationMethodDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.LoginTypeDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.OAuthProviderDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.RoleDao;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserEntityFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Post
 */
public class UserToUserEntityVisitor implements UserVisitor {
	private final OAuthProviderDao oauthProviderDao;
	private final RoleDao roleDao;
	private UserEntity userEntity;
	private final UserEntityFactory userEntityFactory;
	private final LoginTypeDao loginTypeDao;
	private final AuthenticationMethodDao authenticationMethodDao;
	
	public UserToUserEntityVisitor(OAuthProviderDao inOAuthProviderDao,
			RoleDao inRoleDao, LoginTypeDao inLoginTypeDao,
			AuthenticationMethodDao inAuthenticationMethodDao) {
		this.oauthProviderDao = inOAuthProviderDao;
		this.roleDao = inRoleDao;
		this.loginTypeDao = inLoginTypeDao;
		this.authenticationMethodDao = inAuthenticationMethodDao;
		this.userEntityFactory = new UserEntityFactory(this.loginTypeDao, this.authenticationMethodDao);
	}
	
	@Override
	public void visit(LocalUser localUser) {
		LocalUserEntity localUserEntity = this.userEntityFactory.getLocalUserEntityInstance();
		populateUserEntityFields(localUserEntity, localUser);
		localUserEntity.setPassword(localUser.getPassword());
		localUserEntity.setPasswordExpiration(localUser.getPasswordExpiration());
		localUserEntity.setVerificationCode(localUser.getVerificationCode());
		localUserEntity.setVerified(localUser.isVerified());
		this.userEntity = localUserEntity;
	}

	@Override
	public void visit(LdapUser ldapUser) {
		this.userEntity = this.userEntityFactory.getLdapUserEntityInstance();
		populateUserEntityFields(this.userEntity, ldapUser);
	}

	@Override
	public void visit(OAuthUser oauthUser) {
		OAuthUserEntity oauthUserEntity = this.userEntityFactory.getOAuthUserEntityInstance();
		populateUserEntityFields(oauthUserEntity, oauthUser);
		oauthUserEntity.setProviderUsername(oauthUser.getProviderUsername());
		oauthUserEntity.setOAuthProvider(
				this.oauthProviderDao.retrieve(oauthUser.getOAuthProvider()));
		this.userEntity = oauthUserEntity;
	}

	public UserEntity getUserEntity() {
		return this.userEntity;
	}
	
	private void populateUserEntityFields(UserEntity userEntity, User user) {
		userEntity.setUsername(user.getUsername());
		userEntity.setEmail(user.getEmail());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setFullName(user.getFullName());
		userEntity.setDepartment(user.getDepartment());
		userEntity.setCreated(user.getCreated());
		userEntity.setOrganization(user.getOrganization());
		userEntity.setTitle(user.getTitle());
		List<Long> roleLongs = user.getRoles();
		List<RoleEntity> roles = new ArrayList<>(roleLongs.size());
		for (Long roleLong : roleLongs) {
			roles.add(this.roleDao.retrieve(roleLong));
		}
		userEntity.setRoles(roles);
		userEntity.setActive(user.isActive());
	}
}
