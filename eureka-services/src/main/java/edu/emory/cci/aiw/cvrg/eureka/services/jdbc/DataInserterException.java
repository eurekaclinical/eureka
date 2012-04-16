package edu.emory.cci.aiw.cvrg.eureka.services.jdbc;

/**
 * Thrown if there are any issues when inserting data from the input source to
 * the Protempa data source.
 *
 * @author hrathod
 */
public class DataInserterException extends Exception {

	/**
	 * Needed for serializing and de-serializing this class.
	 */
	private static final long serialVersionUID = -1361760705327325126L;

	/**
	 * Create exception with the given message.
	 *
	 * @param message The message for the exception.
	 */
	DataInserterException(String message) {
		super(message);
	}

	/**
	 * Create exception with the given {@link Throwable}r as the root cause.
	 *
	 * @param throwable The root cause.
	 */
	public DataInserterException(Throwable throwable) {
		super(throwable);
	}
}
