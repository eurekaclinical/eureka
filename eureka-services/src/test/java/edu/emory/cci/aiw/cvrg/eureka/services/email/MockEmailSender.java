/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.email;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;

/**
 * A mock implementation of the EmailSender interface that does nothing.
 * 
 * @author hrathod
 * 
 */
public class MockEmailSender implements EmailSender {

	@Override
	public void sendVerificationMessage(UserEntity inUser) throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#
	 * sendActivationMessage(edu.emory.cci.aiw.cvrg.eureka.common.entity.User)
	 */
	@Override
	public void sendActivationMessage(UserEntity inUser) throws EmailException {
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
	public void sendPasswordChangeMessage(UserEntity inUser) throws EmailException {
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
	public void sendPasswordResetMessage(UserEntity inUser, String inNewPassword)
			throws EmailException {
		// do nothing, we don't want any emails to actually be sent out.
	}
}
