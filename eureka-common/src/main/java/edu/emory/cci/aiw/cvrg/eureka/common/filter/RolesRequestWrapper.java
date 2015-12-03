/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.filter;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps an HttpServletRequest object, so that roles for the principal can be
 * implemented.
 * 
 * @author hrathod
 * 
 */
public class RolesRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RolesRequestWrapper.class);
	
	/**
	 * The original principal.
	 */
	private final Principal principal;
	/**
	 * The roles assigned to the principal;
	 */
	private final Set<String> roles;
	


	/**
	 * The original request.
	 */
	private final HttpServletRequest request;

	/**
	 * Create a wrapper with the given principal, role assigned to that
	 * principal, and the original request.
	 * 
	 * @param inRequest The original request.
	 * @param inPrincipal The request principal.
	 * @param inRoles The roles assigned to the principal.
	 */
	public RolesRequestWrapper(HttpServletRequest inRequest,
			Principal inPrincipal, Set<String> inRoles) {
		super(inRequest);
		this.request = inRequest;
		this.principal = inPrincipal;
		this.roles = inRoles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServletRequestWrapper#isUserInRole(java.lang.String
	 * )
	 */
	@Override
	public boolean isUserInRole(String inRole) {
		boolean result;
		if (this.roles == null) {
			result = this.request.isUserInRole(inRole);
		} else {
			result = this.roles.contains(inRole);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequestWrapper#getUserPrincipal()
	 */
	@Override
	public Principal getUserPrincipal() {
		Principal result;
		if (this.principal == null) {
			result = this.request.getUserPrincipal();
		} else {
			result = this.principal;
		}
		return result;
	}
	
	public Set<String> getRoles() {
		return roles;
	}


}
