package edu.emory.cci.aiw.cvrg.eureka.common.test;

/**
 *
 * @author hrathod
 */
public class TestDataException extends Exception {

	/**
	 * Used for serialization and de-serialization.
	 */
	private static final long serialVersionUID = 2821409571465766682L;

	/**
	 * Create an exception with the given Throwable as the root cause.
	 *
	 * @param throwable The root cause of the exception.
	 */
	public TestDataException(Throwable throwable) {
		super(throwable);
	}
}
