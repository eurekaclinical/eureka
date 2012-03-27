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
	public void sendVerificationMessage(User inUser) throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#
	 * sendActivationMessage(edu.emory.cci.aiw.cvrg.eureka.common.entity.User)
	 */
	@Override
	public void sendActivationMessage(User inUser) throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#
	 * sendPasswordChangeMessage
	 * (edu.emory.cci.aiw.cvrg.eureka.common.entity.User)
	 */
	@Override
	public void sendPasswordChangeMessage(User inUser) throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#
	 * sendPasswordResetMessage
	 * (edu.emory.cci.aiw.cvrg.eureka.common.entity.User, java.lang.String)
	 */
	@Override
	public void sendPasswordResetMessage(User inUser, String inNewPassword)
			throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}
}
