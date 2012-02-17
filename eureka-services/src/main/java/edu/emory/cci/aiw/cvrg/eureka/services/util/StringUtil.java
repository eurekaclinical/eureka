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
