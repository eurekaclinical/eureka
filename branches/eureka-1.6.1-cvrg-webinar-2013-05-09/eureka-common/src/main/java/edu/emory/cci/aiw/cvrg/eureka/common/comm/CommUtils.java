/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 * Utility methods related to dealing with communication between different
 * layers of the application.
 *
 * @author hrathod
 *
 */
public final class CommUtils {

	/**
	 * A private default constructor, to disallow instantiation of this class.
	 */
	private CommUtils() {
		// do not allow instantiation of this class.
	}

	/**
	 * Get a Jersey client capable of making HTTPS requests. NOTE: This method
	 * returns a client with a trust manager that trusts all certificates. This
	 * is very bad form. This should be removed as soon as possible (when a real
	 * certificate is available for the machine).
	 *
	 * @return A Jersey client capable of making SSL requests.
	 */
//	public static Client getClient() {
//		ClientConfig clientConfig = new DefaultClientConfig();
//		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
//				Boolean.TRUE);
//		clientConfig.getClasses().add(ObjectMapperProvider.class);
//		return Client.create(clientConfig);
//	}

}
