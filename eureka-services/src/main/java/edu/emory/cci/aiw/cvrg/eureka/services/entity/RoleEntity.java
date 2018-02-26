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
package edu.emory.cci.aiw.cvrg.eureka.services.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javax.persistence.Column;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A bean class to hold information related to roles in the system.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "roles")
public class RoleEntity implements org.eurekaclinical.standardapis.entity.RoleEntity {

	/**
	 * The role's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "ROLE_SEQ_GENERATOR", sequenceName = "ROLE_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "ROLE_SEQ_GENERATOR")
	private Long id;
	/**
	 * The role's name.
	 */
	@Column(unique = true, nullable = false)
	private String name;
	/**
	 * Is this role a default role? Default roles are assigned to all new users.
	 */
	private boolean defaultRole;

	/**
	 * Get the role's identification number.
	 *
	 * @return A {@link Long} representing the role's id.
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the role's identification number.
	 *
	 * @param inId The number representing the role's id.
	 */
	@Override
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the role's name.
	 *
	 * @return A String containing the role's name.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Set the role's name.
	 *
	 * @param inName A string containing the role's name.
	 */
	@Override
	public void setName(String inName) {
		this.name = inName;
	}

	/**
	 * Is this role a default role?
	 *
	 * @return True if the role is a default role, false otherwise.
	 */
	@Override
	public boolean isDefaultRole() {
		return this.defaultRole;
	}

	/**
	 * Set the role's default flag.
	 *
	 * @param inDefaultRole True or False, True indicating a default role, False
	 *            indicating a non-default role.
	 */
	@Override
	public void setDefaultRole(boolean inDefaultRole) {
		this.defaultRole = inDefaultRole;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RoleEntity)) return false;

		RoleEntity role = (RoleEntity) o;

		if (defaultRole != role.defaultRole) return false;
		if (!id.equals(role.id)) return false;
		if (!name.equals(role.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (defaultRole ? 1 : 0);
		return result;
	}
}
