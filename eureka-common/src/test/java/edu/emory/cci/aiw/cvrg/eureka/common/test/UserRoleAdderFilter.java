/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.test;

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

import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author arpost
 */
@Singleton
public class UserRoleAdderFilter implements Filter {

	public UserRoleAdderFilter() {
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) sr;
		fc.doFilter(new UserRoleRequestWrapper(req), sr1);
	}

	@Override
	public void destroy() {
	}
    
}
