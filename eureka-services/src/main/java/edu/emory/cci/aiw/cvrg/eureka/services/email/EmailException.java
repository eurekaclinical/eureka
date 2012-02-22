package edu.emory.cci.aiw.cvrg.eureka.services.email;

/**
 * To be thrown when there are any errors in initializing an email sender, or in
 * sending the email.
 * 
 * @author hrathod
 * 
 */
public class EmailException extends Exception {
	/**
	 * Used when serializing.
	 */
	private static final long serialVersionUID = 8115982466783627054L;

	/**
	 * Create an exception with the given message.
	 * 
	 * @param message The message for the exception.
	 */
	public EmailException(final String message) {
		super(message);
	}

	/**
	 * Create an exception with the given Throwable as the root cause.
	 * 
	 * @param throwable The root cause.
	 */
	public EmailException(Throwable throwable) {
		super(throwable);
	}
}
