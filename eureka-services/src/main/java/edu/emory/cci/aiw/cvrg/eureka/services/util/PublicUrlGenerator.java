/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;

/**
 * Generates Eureka!'s best guess at the public URL based on a servlet request
 * and the machine hostname.
 * 
 * @author arpost
 */
public class PublicUrlGenerator {

	/**
	 * Generates Eureka!'s best guess at the public URL.
	 * 
	 * @param request the {@link HttpServletRequest}, from which the port is
	 * extracted. If <code>null</code>, the port is assumed to be 80.
	 * @return a scheme, hostname and port (if port != 80).
	 */
	public static String generate(HttpServletRequest request) {
		int port = request != null ? request.getServerPort() : 80;
		try {
			InetAddress localhost = InetAddress.getLocalHost();
			return "http://" + localhost.getHostName()
					+ (port == 80 ? "" : ":" + port);
		} catch (UnknownHostException ex) {
			return "localhost";
		}
	}
}
