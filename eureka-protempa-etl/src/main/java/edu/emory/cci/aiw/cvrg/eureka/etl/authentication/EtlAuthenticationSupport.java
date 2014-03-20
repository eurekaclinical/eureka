package edu.emory.cci.aiw.cvrg.eureka.etl.authentication;

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

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AbstractAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.UserPrincipalAttributes;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 *
 * @author Andrew Post
 */
public class EtlAuthenticationSupport extends AbstractAuthenticationSupport {
	private final EtlUserDao etlUserDao;
	
	public EtlAuthenticationSupport(EtlUserDao inEtlUserDao) {
		this.etlUserDao = inEtlUserDao;
	}

	public boolean isSameUser(HttpServletRequest servletRequest, EtlUser user) {
		return isSameUser(servletRequest, user.getUsername());
	}
	
	public EtlUser getEtlUser(HttpServletRequest servletRequest) {
		AttributePrincipal principal = getUserPrincipal(servletRequest);
		EtlUser user = this.etlUserDao.getByAttributePrincipal(principal);
		if (user == null) {
			user = new EtlUser();
			user.setUsername(principal.getName());
			this.etlUserDao.create(user);
		}
		return user;
	}
}
