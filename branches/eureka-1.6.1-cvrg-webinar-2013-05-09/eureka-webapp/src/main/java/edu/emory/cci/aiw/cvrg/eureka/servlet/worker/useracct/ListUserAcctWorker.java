/*
 * #%L
 * Eureka WebApp
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
package edu.emory.cci.aiw.cvrg.eureka.servlet.worker.useracct;

import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.servlet.worker.ServletWorker;

public class ListUserAcctWorker implements ServletWorker {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String passwordExpiration=new String();
		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");
		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		Principal principal = req.getUserPrincipal();
		String userName = principal.getName();
		User user = servicesClient.getUserByName(userName);
		
		Date now = Calendar.getInstance().getTime();
		Date expiration = user.getPasswordExpiration();
		
		if (expiration != null && now.after(expiration)) 
			passwordExpiration="Your password has expired.Please change the password.";
		else 
			passwordExpiration="";
		req.setAttribute("passwordExpiration", passwordExpiration);
		
		req.setAttribute("user", user);
		req.getRequestDispatcher("/protected/acct.jsp").forward(req, resp);
	}
}
