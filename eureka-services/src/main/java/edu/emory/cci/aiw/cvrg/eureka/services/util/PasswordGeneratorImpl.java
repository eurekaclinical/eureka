package edu.emory.cci.aiw.cvrg.eureka.services.util;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * An implementation of {@link PasswordGenerator} that uses a secure random
 * number to generate the password.
 *
 * @author hrathod
 */
public class PasswordGeneratorImpl implements PasswordGenerator {

	/**
	 * A secure random number generator to be used to create passwords.
	 */
	private static final SecureRandom RANDOM = new SecureRandom();

	@Override
	public String generatePassword() {
		final int length = 15 + RANDOM.nextInt(10);
		return new BigInteger(130, RANDOM).toString(32).substring(0, length);
	}
}
