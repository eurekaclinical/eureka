package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * Exception thrown when the data provider can not properly parse or fetch the
 * data needed.
 * 
 * @author hrathod
 * 
 */
public class DataProviderException extends Exception {

	/**
	 * Needed for serialization purposes.
	 */
	private static final long serialVersionUID = -8824458710265012443L;

	/**
	 * Create the exception using a simple message.
	 * 
	 * @param message The message to set for the exception.
	 */
	DataProviderException(String message) {
		super(message);
	}

	/**
	 * Create the exception using a {@link Throwable} to be used as the root
	 * cause.
	 * 
	 * @param throwable The root cause for this exception.
	 */
	DataProviderException(Throwable throwable) {
		super(throwable);
	}
}
