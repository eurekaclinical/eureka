package edu.emory.cci.aiw.cvrg.eureka.services.util;

/**
 * Implements a test version of {@link PasswordGenerator}.  This generator
 * always returns a hard-coded string as the random password.
 */
public class PasswordGeneratorTestImpl implements PasswordGenerator {

	private static final String PASSWORD = "test1234";

	@Override
	public String generatePassword() {
		return PASSWORD;
	}
}
