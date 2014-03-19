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

/**
 *
 * @author Andrew Post
 */
public interface UserEntityVisitor {
	
	/**
	 * Processes a local user entity.
	 * @param localUserEntity a local user entity. Cannot be 
	 * <code>null</code>.
	 */
	void visit(LocalUserEntity localUserEntity);
	
	/**
	 * Processes an LDAP user entity.
	 * @param ldapUserEntity an LDAP user entity. Cannot be 
	 * <code>null</code>.
	 */
	void visit(LdapUserEntity ldapUserEntity);
	
	/**
	 * Processes an OAuth user entity.
	 * @param oauthUserEntity an OAuth user entity. Cannot be 
	 * <code>null</code>.
	 */
	void visit(OAuthUserEntity oauthUserEntity);
}
