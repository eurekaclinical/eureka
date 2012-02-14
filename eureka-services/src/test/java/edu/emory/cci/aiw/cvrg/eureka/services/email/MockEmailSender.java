package edu.emory.cci.aiw.cvrg.eureka.services.email;

import javax.mail.MessagingException;

/**
 * A mock implementation of the EmailSender interface that does nothing.
 * 
 * @author hrathod
 * 
 */
public class MockEmailSender implements EmailSender {

	@Override
	public void sendMessage(String inUser) throws MessagingException {
		// do nothing, we don't want any emails to actually be sent out.
	}

}
