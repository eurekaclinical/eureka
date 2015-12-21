/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.UserEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the EmailSender interface with FreeMarker templates.
 *
 * @author hrathod
 *
 */
public class FreeMarkerEmailSender implements EmailSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerEmailSender.class);

	/**
	 * The FreeMarker configuration object.
	 */
	private final Configuration configuration;
	/**
	 * The mail session used to send the emails.
	 */
	private final Session session;
	/**
	 * The application configuration properties.
	 */
	private final ServiceProperties serviceProperties;

	/**
	 * Default constructor, creates a FreeMarker configuration object. Should be
	 * called in the context of a request.
	 *
	 * @param inServiceProperties The application configuration object.
	 * @param inSession The mail session to use when sending a message.
	 */
	@Inject
	public FreeMarkerEmailSender(
			final ServiceProperties inServiceProperties,
			final Session inSession) {
		this.serviceProperties = inServiceProperties;
		this.session = inSession;
		this.configuration = new Configuration();
		this.configuration.setClassForTemplateLoading(this.getClass(),
				"/templates/");
		this.configuration.setObjectWrapper(new DefaultObjectWrapper());
	}

	/**
	 * Sends a verification email.
	 *
	 * @param inUser The user to verify.
	 * @throws EmailException
	 */
	@Override
	public void sendVerificationMessage(final UserEntity inUser)
			throws EmailException {
		Map<String, Object> params = new HashMap<>();
		String verificationUrl = this.serviceProperties.getVerificationUrl();
		params.put("verificationUrl", verificationUrl);
		sendMessage(inUser, "verification.ftl",
				this.serviceProperties.getVerificationEmailSubject(), params);
	}

	@Override
	public void sendActivationMessage(final UserEntity inUser) throws EmailException {
		Map<String, Object> params = new HashMap<>();
		String applicationUrl = this.serviceProperties.getApplicationUrl();
		params.put("applicationUrl", applicationUrl);
		sendMessage(inUser, "activation.ftl",
				this.serviceProperties.getActivationEmailSubject(), params);
	}

	@Override
	public void sendPasswordChangeMessage(UserEntity inUser) throws EmailException {
		sendMessage(inUser, "password.ftl",
				this.serviceProperties.getPasswordChangeEmailSubject());
	}

	@Override
	public void sendPasswordResetMessage(UserEntity inUser, String inNewPassword)
			throws EmailException {
		Map<String, Object> params = new HashMap<>();
		params.put("plainTextPassword", inNewPassword);
		sendMessage(inUser, "password_reset.ftl",
				this.serviceProperties.getPasswordResetEmailSubject(), params);
	}

	/**
	 * Send an email to the given user with the given subject line, using
	 * contents generated from the given template.
	 *
	 * @param inUser The user to whom the message should be sent.
	 * @param templateName The name of the template used to generate the
	 * contents of the email.
	 * @param subject The subject for the email being sent.
	 * @param params The parameters to be used to substitute values in the email
	 * template.
	 * @throws EmailException Thrown if there are any errors in generating
	 * content from the template, composing the email, or sending the email.
	 */
	private void sendMessage(final UserEntity inUser, final String templateName,
			final String subject, Map<String, Object> params)
			throws EmailException {
		params.put("user", inUser);
		params.put("config", this.serviceProperties);
		sendMessage(templateName, subject, inUser.getEmail(), params);
	}

	/**
	 * Send an email to the given user with the given subject line, using
	 * contents generated from the given template.
	 *
	 * @param inUser The user to whom the message should be sent.
	 * @param templateName The name of the template used to generate the
	 * contents of the email.
	 * @param subject The subject for the email being sent.
	 * @throws EmailException Thrown if there are any errors in generating
	 * content from the template, composing the email, or sending the email.
	 */
	private void sendMessage(final UserEntity inUser,
			final String templateName,
			final String subject) throws EmailException {
		Map<String, Object> params = new HashMap<>();
		sendMessage(inUser, templateName, subject, params);
	}

	/**
	 * Send an email to the given email address with the given subject line,
	 * using contents generated from the given template and parameters.
	 *
	 * @param templateName The name of the template used to generate the
	 * contents of the email.
	 * @param subject The subject for the email being sent.
	 * @param emailAddress Sends the email to this address.
	 * @param params The template is merged with these parameters to generate
	 * the content of the email.
	 * @throws EmailException Thrown if there are any errors in generating
	 * content from the template, composing the email, or sending the email.
	 */
	private void sendMessage(final String templateName, final String subject,
			final String emailAddress, final Map<String, Object> params)
			throws EmailException {
		Writer stringWriter = new StringWriter();
		try {
			Template template = this.configuration.getTemplate(templateName);
			template.process(params, stringWriter);
		} catch (TemplateException | IOException e) {
			throw new EmailException(e);
		}

		String content = stringWriter.toString();
		MimeMessage message = new MimeMessage(this.session);
		try {
			InternetAddress fromEmailAddress = null;
			String fromEmailAddressStr
					= this.serviceProperties.getFromEmailAddress();
			if (fromEmailAddressStr != null) {
				fromEmailAddress = new InternetAddress(fromEmailAddressStr);
			}
			if (fromEmailAddress == null) {
				fromEmailAddress
						= InternetAddress.getLocalAddress(this.session);
			}
			if (fromEmailAddress == null) {
				try {
					fromEmailAddress = new InternetAddress("no-reply@"
							+ InetAddress.getLocalHost().getCanonicalHostName());
				} catch (UnknownHostException ex) {
					fromEmailAddress = new InternetAddress("no-reply@localhost");
				}
			}
			message.setFrom(fromEmailAddress);
			message.setSubject(subject);
			message.setContent(content, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					emailAddress));
			message.setSender(fromEmailAddress);
			Transport.send(message);
		} catch (MessagingException e) {
			LOGGER.error("Error sending the following email message:");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				message.writeTo(out);
				out.close();
			} catch (IOException | MessagingException ex) {
				try {
					out.close();
				} catch (IOException ignore) {
				}
			}
			LOGGER.error(out.toString());
			throw new EmailException(e);
		}
	}

}
