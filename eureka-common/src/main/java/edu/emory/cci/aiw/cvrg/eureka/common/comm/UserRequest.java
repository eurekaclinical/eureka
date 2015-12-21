/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.LoginType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * A bean holding information about a user's registration request.
 *
 * @author hrathod
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LocalUserRequest.class, name = "LOCAL"),
        @JsonSubTypes.Type(value = OAuthUserRequest.class, name = "OAUTH"),
        @JsonSubTypes.Type(value = LdapUserRequest.class, name = "LDAP")
})
public abstract class UserRequest implements UserRequestVisitable {

	/**
	 * The request's unique identifier.
	 */
	private Long id;

	/**
	 * The user's unique username.
	 */
	private String username;

	/**
	 * The user's first name.
	 */
	private String firstName;
	/**
	 * The user's last name.
	 */
	private String lastName;
	/**
	 * The user's full name.
	 */
	private String fullName;
	/**
	 * The user's email address.
	 */
	private String email;
	/**
	 * The user's email address verification.
	 */
	private String verifyEmail;
	/**
	 * The user's organization.
	 */
	private String organization;

	/**
	 * The user's title. Added later on.
	 */
	private String title;
	/**
	 * The user's department. Added later on.
	 */
	private String department;
	
	private LoginType loginType;

	/**
	 * Default constructor, calls super()
	 */
	public UserRequest() {
		super();
	}

	/**
	 * Get the request's unique identifier.
	 *
	 * @return The request's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the user's unique identifier.
	 *
	 * @param inId The unique identifier for the request.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Gets the user's unique username.
	 *
	 * @return the user's unique username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the user's unique username.
	 *
	 * @param inUsername the user's unique username.
	 */
	public void setUsername(String inUsername) {
		this.username = inUsername;
	}

	/**
	 * Get the user's first name.
	 *
	 * @return user's first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the user's first name.
	 *
	 * @param inFirstName the user's first name.
	 */
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/**
	 * Get the user's last name.
	 *
	 * @return The user's last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set the user's last name.
	 *
	 * @param inLastName The user's last name.
	 */
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Get the user's email address.
	 *
	 * @return The user's email address.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the user's email address.
	 *
	 * @param inEmail The user's email address.
	 */
	public void setEmail(String inEmail) {
		this.email = inEmail;
	}

	/**
	 * Set the user's email address verification.
	 *
	 * @return The user's email address verification.
	 */
	public String getVerifyEmail() {
		return this.verifyEmail;
	}

	/**
	 * Set the user's email address verification.
	 *
	 * @param inVerifyEmail The user's email address verification.
	 */
	public void setVerifyEmail(String inVerifyEmail) {
		this.verifyEmail = inVerifyEmail;
	}

	/**
	 * Get the user's organization.
	 *
	 * @return The user's organization.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * Set the user's organization.
	 *
	 * @param inOrganization The user's organization.
	 */
	public void setOrganization(String inOrganization) {
		this.organization = inOrganization;
	}

	/**
	 * Get the user's title.
	 *
	 * @return The user's title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set the user's title
	 *
	 * @param inTitle The user's title.
	 */
	public void setTitle(String inTitle) {
		this.title = inTitle;
	}

	/**
	 * Get the user's department.
	 *
	 * @return The user's department.
	 */
	public String getDepartment() {
		return this.department;
	}

	/**
	 * Set the user's department.
	 *
	 * @param inDepartment The user's department.
	 */
	public void setDepartment(String inDepartment) {
		this.department = inDepartment;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	protected void setLoginType(LoginType inLoginType) {
		this.loginType = inLoginType;
	}
	
	/**
	 * Validate a {@link UserRequest} object. Two rules are implemented: 1) The
	 * email addresses in the two email fields must match, and 2) The passwords
	 * in the two password fields must match.
	 *
	 * @return an array of validation error messages, or an empty array if
	 * validation succeeded.
	 */
	public String[] validate() {
		List<String> result = new ArrayList<>();
		
		if (this.username == null) {
			result.add("Username unspecified");
		}
		if (this.email == null && this.verifyEmail == null) {
			result.add("Email unspecified");
		} else if ((this.email == null) || (this.verifyEmail == null) || (!this.email
				.equals(
						this.verifyEmail))) {
			result.add("Mismatched emails");
		}
		
		return result.toArray(new String[result.size()]);
	}
	
	public abstract edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod authenticationMethod();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
