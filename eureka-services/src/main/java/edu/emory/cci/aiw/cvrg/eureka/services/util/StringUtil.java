/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provide string related methods.
 * 
 * @author hrathod
 * 
 */
public class StringUtil {

	/**
	 * Create an MD5 hash from the given string.
	 * 
	 * @param inData the data string to be hashed.
	 * @return A hash of the input data string.
	 * @throws NoSuchAlgorithmException If the hashing algorithm can not be
	 *             found.
	 */
	public static String md5(final String inData)
			throws NoSuchAlgorithmException {
		StringBuilder hexBuilder = new StringBuilder();
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(inData.getBytes());
		for (byte b : digest.digest()) {
			hexBuilder.append(Integer.toHexString(b & 0x00FF));
		}
		return hexBuilder.toString();
	}
}
