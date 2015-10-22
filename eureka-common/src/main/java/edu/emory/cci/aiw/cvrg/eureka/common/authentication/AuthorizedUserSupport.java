package edu.emory.cci.aiw.cvrg.eureka.common.authentication;

/*
 * #%L
 * Eureka Protempa ETL
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.AuthorizedUserDao;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response.Status;
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
		AuthorizedUserEntity result = this.userDao.getByAttributePrincipal(principal);
		if (result == null) {
			throw new HttpStatusException(Status.FORBIDDEN, "User " + principal.getName() + " is unauthorized to use this resource");
		}
		return result;
	}
}
