package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "oauth_users")
public class OAuthUserEntity extends UserEntity {
	/**
	 * The login name of the user at the authentication provider.
	 */
	@Column(nullable = false)
	private String providerUsername;
	
	@ManyToOne
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private OAuthProvider oauthProvider;

	public OAuthUserEntity() {
	}
	
	public String getProviderUsername() {
		return providerUsername;
	}

	public void setProviderUsername(String providerUsername) {
		this.providerUsername = providerUsername;
	}

	public OAuthProvider getOAuthProvider() {
		return oauthProvider;
	}

	public void setOAuthProvider(OAuthProvider provider) {
		this.oauthProvider = provider;
	}
	
	@Override
	public void accept(UserEntityVisitor userEntityVisitor) {
		userEntityVisitor.visit(this);
	}
}
