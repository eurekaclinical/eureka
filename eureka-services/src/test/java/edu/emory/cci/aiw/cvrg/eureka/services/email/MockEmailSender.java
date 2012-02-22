package edu.emory.cci.aiw.cvrg.eureka.services.email;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * A mock implementation of the EmailSender interface that does nothing.
 * 
 * @author hrathod
 * 
 */
public class MockEmailSender implements EmailSender {

	@Override
	public void sendMessage(User inUser) throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}

}
