package edu.emory.cci.aiw.cvrg.eureka.services.email;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * An interface that defines how emails can be sent out from the application.
 * 
 * @author hrathod
 * 
 */
public interface EmailSender {

	/**
	 * Send a message to the user. This is a test method so far, needs to be
	 * separated out into various methods to send different types of messages.
	 * 
	 * TODO: Remove this method and replace with more appropriate methods.
	 * 
	 * @param user To whom the email should be sent.
	 * @throws EmailException If the email can not be sent for any reason.
	 */
	public abstract void sendMessage(final User user) throws EmailException;

}