package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hrathod
 */
public class UserInfo {
	private Long id;
	private boolean active;
	private boolean verified;
	private String firstName;
	private String lastName;
	private String email;
	private String organization;
	private String verificationCode;
	private Date lastLogin;
	private String password;
	private Date passwordExpiration;
	private List<Long> roles = new ArrayList<>();
	private String title;
	private String department;

	public Long getId() {
		return id;
	}

	public void setId(Long inId) {
		id = inId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean inActive) {
		active = inActive;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean inVerified) {
		verified = inVerified;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String inFirstName) {
		firstName = inFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String inLastName) {
		lastName = inLastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String inEmail) {
		email = inEmail;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String inOrganization) {
		organization = inOrganization;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String inVerificationCode) {
		verificationCode = inVerificationCode;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date inLastLogin) {
		lastLogin = inLastLogin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String inPassword) {
		password = inPassword;
	}

	public Date getPasswordExpiration() {
		return passwordExpiration;
	}

	public void setPasswordExpiration(Date inPasswordExpiration) {
		passwordExpiration = inPasswordExpiration;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> inRoles) {
		roles = inRoles;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}
