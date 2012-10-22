/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 * A bean holding information about a user's registration request.
 * 
 * @author hrathod
 * 
 */
public class UserRequest {
	/**
	 * The request's unique identifier.
	 */
	private Long id;
	/**
	 * The user's first name.
	 */
	private String firstName;
	/**
	 * The user's last name.
	 */
	private String lastName;
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
	 * The user's password.
	 */
	private String password;
	/**
	 * The user's password verification.
	 */
	private String verifyPassword;

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
	 * Get the user's password.
	 * 
	 * @return The user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 * 
	 * @param inPassword The user's password.
	 */
	public void setPassword(String inPassword) {
		this.password = inPassword;
	}

	/**
	 * Get the password verification.
	 * 
	 * @return The password verification.
	 */
	public String getVerifyPassword() {
		return this.verifyPassword;
	}

	/**
	 * Set the password verification.
	 * 
	 * @param inVerifyPassword The password verification.
	 */
	public void setVerifyPassword(String inVerifyPassword) {
		this.verifyPassword = inVerifyPassword;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserRequest [id=").append(this.id)
				.append(", firstName=").append(this.firstName)
				.append(", lastName=").append(this.lastName).append(", email=")
				.append(this.email).append(", verifyEmail=")
				.append(this.verifyEmail).append(", organization=")
				.append(this.organization).append(", password=")
				.append(this.password).append(", verifyPassword=")
				.append(this.verifyPassword).append("]");
		return builder.toString();
	}
}
