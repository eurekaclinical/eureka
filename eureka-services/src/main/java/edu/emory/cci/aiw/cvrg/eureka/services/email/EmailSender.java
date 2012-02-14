package edu.emory.cci.aiw.cvrg.eureka.services.email;

import javax.mail.MessagingException;

public interface EmailSender {

	/**
	 * Send a message to the user.  This is a test method so far, needs to be 
	 * separated out into various methods to send different types of messages.
	 * 
	 * TODO:  Remove this method and replace with more appropriate methods.
	 * 
	 * @param user
	 * @throws MessagingException
	 */
	public abstract void sendMessage(final String user)
			throws MessagingException;

}