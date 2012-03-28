package edu.emory.cci.aiw.cvrg.eureka.services.thread;

/**
 * An exception to be thrown when a task can not be completed successfully.
 * 
 * @author hrathod
 * 
 */
class TaskException extends Exception {
	/**
	 * Used for serializing/deserializing.
	 */
	private static final long serialVersionUID = 1467398371312226421L;

	/**
	 * Creates an exception with the given error message.
	 * 
	 * @param message The message to associate with the exception.
	 */
	TaskException(String message) {
		super(message);
	}

	/**
	 * Creates an exception with the given Throwable as the root cause.
	 * 
	 * @param throwable The root cause.
	 */
	public TaskException(Throwable throwable) {
		super(throwable);
	}
}
