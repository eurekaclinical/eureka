package edu.emory.cci.aiw.cvrg.eureka.services.datavalidator;

/**
 * Hold various validation messages that occur when verifying an input data
 * source.
 * 
 * @author hrathod
 * 
 */
public class ValidationEvent {

	/**
	 * The line number where the event occurred.
	 */
	private Long line;
	/**
	 * The type of event (usually the entity that caused the event)
	 */
	private String type;
	/**
	 * The text of the event.
	 */
	private String message;
	/**
	 * Whether the event is fatal.
	 */
	private boolean fatal;

	/**
	 * @return the line
	 */
	public Long getLine() {
		return this.line;
	}

	/**
	 * @param inLine the line to set
	 */
	public void setLine(Long inLine) {
		this.line = inLine;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param inType the type to set
	 */
	public void setType(String inType) {
		this.type = inType;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param inMessage the message to set
	 */
	public void setMessage(String inMessage) {
		this.message = inMessage;
	}

	/**
	 * @return the fatal
	 */
	public boolean isFatal() {
		return this.fatal;
	}

	/**
	 * @param inFatal the fatal to set
	 */
	public void setFatal(boolean inFatal) {
		this.fatal = inFatal;
	}

}
