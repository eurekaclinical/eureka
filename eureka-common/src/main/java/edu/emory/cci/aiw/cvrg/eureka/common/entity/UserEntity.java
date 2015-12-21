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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A bean class to hold information about users in the system.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity implements UserEntityVisitable {

	/**
	 * The user's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "USER_SEQ_GENERATOR")
	private Long id;
	/**
	 * Is the user activated?
	 */
	private boolean active;
	/**
	 * The login name of the user.
	 */
	@Column(unique = true, nullable = false)
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
	 * The user's full name
	 */
	private String fullName;

	/**
	 * The user's title.
	 */
	private String title;

	/**
	 * The user's department.
	 */
	private String department;

	/**
	 * The user's email address.
	 */
	@Column(nullable = false)
	private String email;
	/**
	 * The user's organization.
	 */
	private String organization;

	/**
	 * The date when the account was created on Eureka.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date created;
	/**
	 * The last log-in date for the user.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private LoginTypeEntity loginType;
	
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "id", nullable = false)
	private AuthenticationMethodEntity authenticationMethod;
	
	/**
	 * A list of roles assigned to the user.
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
			targetEntity = Role.class)
	@JoinTable(name = "user_role",
			joinColumns = {
				@JoinColumn(name = "user_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "role_id")})
	private List<Role> roles = new ArrayList<>();

	/**
	 * Create an empty User object.
	 */
	protected UserEntity() {
		this.created = new Date();
	}
	
	protected UserEntity(LoginTypeEntity loginType, AuthenticationMethodEntity authenticationMethod) {
		this();
		assert loginType != null : "loginType cannot be null";
		assert authenticationMethod != null : "authenticationMethod cannot be null";
		this.loginType = loginType;
		this.authenticationMethod = authenticationMethod;
	}

	/**
	 * Get the user's unique identifier.
	 *
	 * @return A {@link Long} representing the user's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the user's unique identifier.
	 *
	 * @param inId A {@link Long} representing the user's unique identifier.
	 */
	public void setId(final Long inId) {
		this.id = inId;
	}

	/**
	 * Get whether the user is active or not.
	 *
	 * @return True if the user is active, false otherwise.
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * Set whether the user is active or not.
	 *
	 * @param inActive True if the user should be active, false otherwise.
	 */
	public void setActive(final boolean inActive) {
		this.active = inActive;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		if (created == null) {
			this.created = new Date();
		} else {
			this.created = created;
		}
	}

	/**
	 * Get the user's first name.
	 *
	 * @return A String containing the user's first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the user's first name.
	 *
	 * @param inFirstName A String containing the user's first name.
	 */
	public void setFirstName(final String inFirstName) {
		this.firstName = inFirstName;
	}

	/**
	 * Get the user's last name.
	 *
	 * @return A String containing the user's last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set the user's last name.
	 *
	 * @param inLastName A String containing the user's last name.
	 */
	public void setLastName(final String inLastName) {
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
	 * @return A String containing the user's email address.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the user's email address.
	 *
	 * @param inEmail A String containing the user's email address.
	 */
	public void setEmail(final String inEmail) {
		this.email = inEmail;
	}

	/**
	 * Get the user's organization.
	 *
	 * @return A String containing the user's organization.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * Set the user's organization.
	 *
	 * @param inOrganization A String containing the user's organization.
	 */
	public void setOrganization(final String inOrganization) {
		this.organization = inOrganization;
	}

	/**
	 * Get the last log-in date for the user.
	 *
	 * @return The user's last log-in date, or <code>null</code> if the user has
	 * never logged in.
	 */
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * Set the user's last log-in date.
	 *
	 * @param inLastLogin The last log-in date for the user.
	 */
	public void setLastLogin(Date inLastLogin) {
		this.lastLogin = inLastLogin;
	}

	/**
	 * Get all the roles assigned to the user.
	 *
	 * @return A list of {@link Role} objects.
	 */
	public List<Role> getRoles() {
		return this.roles;
	}

	/**
	 * Set the roles assigned to the current user.
	 *
	 * @param inRoles A Set of roles to be assigned to the user.
	 */
	public void setRoles(final List<Role> inRoles) {
		this.roles = inRoles;
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
	 * Set the user's title.
	 *
	 * @param inTitle The user's title.
	 */
	public void setTitle(String inTitle) {
		title = inTitle;
	}

	/**
	 * Get the user's Department.
	 *
	 * @return The user's Department.
	 */
	public String getDepartment() {
		return this.department;
	}

	/**
	 * Set the user's Department.
	 *
	 * @param inDepartment The user's department.
	 */
	public void setDepartment(String inDepartment) {
		department = inDepartment;
	}

	public LoginTypeEntity getLoginType() {
		return loginType;
	}

	void setLoginType(LoginTypeEntity loginType) {
		this.loginType = loginType;
	}

	public AuthenticationMethodEntity getAuthenticationMethod() {
		return authenticationMethod;
	}

	void setAuthenticationMethod(AuthenticationMethodEntity authMethod) {
		this.authenticationMethod = authMethod;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
