/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import javax.servlet.http.HttpServletRequest;

/**
 * An interface that defines how emails can be sent out from the application.
 * 
 * @author hrathod
 * 
 */
public interface EmailSender {

	/**
	 * Send a verification email to the user. The verification email contains a
	 * link which the user can click to verify their registration with the
	 * application.
	 * 
	 * @param user To whom the email should be sent.
	 * @throws EmailException If the email can not be sent for any reason.
	 */
	public void sendVerificationMessage(User user) throws EmailException;

	/**
	 * Send an activation email to the user. The activation email is sent after
	 * the user has verified their identity, and as the system administrator is
	 * activating the account for use.
	 * 
	 * @param user To whom the email should be sent.
	 * @throws EmailException Thrown if the email can not be sent for any
	 *             reason.
	 */
	public void sendActivationMessage(User user) throws EmailException;

	/**
	 * Send a password change email to the user. The password change email is
	 * sent any time a user changes their password.
	 * 
	 * @param user To whom the email should be sent.
	 * @throws EmailException Thrown if the email can not be properly sent.
	 */
	public void sendPasswordChangeMessage(User user) throws EmailException;

	/**
	 * Send a password reset email to the user. The password reset email is sent
	 * when a user forgets his or her password, and asks the system to reset it.
	 * 
	 * @param user The user who is requesting the password reset.
	 * @param newPassword The new password, before hashing.
	 * @throws EmailException Thrown if the email can not be sent for any
	 *             reason.
	 */
	public void sendPasswordResetMessage(User user, String newPassword)
			throws EmailException;
}