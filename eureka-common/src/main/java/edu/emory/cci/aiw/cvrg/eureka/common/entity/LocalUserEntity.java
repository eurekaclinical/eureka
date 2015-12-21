package edu.emory.cci.aiw.cvrg.eureka.common.entity;

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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "local_users")
public class LocalUserEntity extends UserEntity {
	
	/**
	 * The class logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			LocalUserEntity.class);
	
	/**
	 * The user's password.
	 */
	@Column(nullable = false)
	private String password;
	/**
	 * The user's verification code;
	 */
	private String verificationCode;
	
	private boolean verified;
	
	/**
	 * The password expiration date.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date passwordExpiration;
	
	public LocalUserEntity(LoginTypeEntity loginType, AuthenticationMethodEntity authenticationMethod) {
		super(loginType, authenticationMethod);
	}
	
	protected LocalUserEntity() {
		
	}

	/**
	 * Get the user's password
	 *
	 * @return A String containing the user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 *
	 * @param inPassword A String containing the user's password.
	 */
	public void setPassword(final String inPassword) {
		this.password = inPassword;
	}

	/**
	 * @return the verificationCode
	 */
	public String getVerificationCode() {
		return this.verificationCode;
	}

	/**
	 * @param inVerificationCode the verificationCode to set
	 */
	public void setVerificationCode(String inVerificationCode) {
		this.verificationCode = inVerificationCode;
	}
	
	/**
	 * Get the user's password expiration date.
	 *
	 * @return The user's password expiration date.
	 */
	public Date getPasswordExpiration() {
		return passwordExpiration;
	}

	/**
	 * Set the user's password expiration date.
	 *
	 * @param inPasswordExpiration The user's password expiration date.
	 */
	public void setPasswordExpiration(Date inPasswordExpiration) {
		passwordExpiration = inPasswordExpiration;
	}
	
	/**
	 * Get whether the user has been verified.
	 *
	 * @return True if the user has been verified, false otherwise.
	 */
	public boolean isVerified() {
		return this.verified;
	}

	/**
	 * Set whether the user has been verified.
	 *
	 * @param inVerified True if the user has been verified, false otherwise.
	 */
	public void setVerified(final boolean inVerified) {
		this.verified = inVerified;
	}
	
	@Override
	public void accept(UserEntityVisitor userEntityVisitor) {
		userEntityVisitor.visit(this);
	}
}
