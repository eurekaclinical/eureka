package edu.emory.cci.aiw.cvrg.eureka.webapp.authentication;

/*
 * #%L
 * Eureka Protempa ETL
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

import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.webapp.dao.AuthorizedUserDao;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class AuthorizedUserSupport extends AbstractUserSupport {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizedUserSupport.class);
	
	private final AuthorizedUserDao userDao;
	
	public AuthorizedUserSupport(AuthorizedUserDao inUserDao) {
		this.userDao = inUserDao;
	}

	public boolean isSameUser(HttpServletRequest servletRequest, AuthorizedUserEntity user) {
		return isSameUser(servletRequest, user.getUsername());
	}
	
	/**
	 * Returns the user object, or if there isn't one, throws an exception.
	 * 
	 * @param servletRequest the HTTP servlet request.
	 * @return the user object.
	 * 
	 * @throws HttpStatusException if the logged-in user isn't in the user
	 * table, which means the user is not authorized to use 
	 * eureka-protempa-etl.
	 */
	public AuthorizedUserEntity getUser(HttpServletRequest servletRequest) {
		AttributePrincipal principal = getUserPrincipal(servletRequest);
		AuthorizedUserEntity result = this.userDao.getByPrincipal(principal);
		if (result == null) {
			throw new HttpStatusException(Status.FORBIDDEN, "User " + principal.getName() + " is unauthorized to use this resource");
		}
		return result;
	}
}
