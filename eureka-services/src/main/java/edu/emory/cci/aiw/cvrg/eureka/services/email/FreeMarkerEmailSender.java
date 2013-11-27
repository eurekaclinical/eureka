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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.common.props.PublicUrlGenerator;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Implements the EmailSender interface with FreeMarker templates.
 *
 * @author hrathod
 *
 */
public class FreeMarkerEmailSender implements EmailSender {

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
	 * The request object.
	 */
	private final HttpServletRequest request;

	/**
	 * Default constructor, creates a FreeMarker configuration object. Should
	 * be called in the context of a request.
	 *
	 * @param inServiceProperties The application configuration object.
	 * @param inSession The mail session to use when sending a message.
	 */
	@Inject
	public FreeMarkerEmailSender(
			final ServiceProperties inServiceProperties,
			final Session inSession,
			@Context HttpServletRequest request) {
		this.serviceProperties = inServiceProperties;
		this.session = inSession;
		this.configuration = new Configuration();
		this.configuration.setClassForTemplateLoading(this.getClass(),
				"/templates/");
		this.configuration.setObjectWrapper(new DefaultObjectWrapper());
		this.request = request;
	}
	
	/**
	 * Sends a verification email.
	 * @param inUser The user to verify.
	 * @throws EmailException 
	 */
	@Override
	public void sendVerificationMessage(final User inUser) 
			throws EmailException {
		Map<String, Object> params = new HashMap<>();
		String verificationUrl = this.serviceProperties.getVerificationUrl(this.request);
		params.put("verificationUrl", verificationUrl);
		sendMessage(inUser, "verification.ftl",
				this.serviceProperties.getVerificationEmailSubject(), params);
	}
	
	@Override
	public void sendActivationMessage(final User inUser) throws EmailException {
		Map<String, Object> params = new HashMap<>();
		String applicationUrl = this.serviceProperties.getApplicationUrl(this.request);
		params.put("applicationUrl", applicationUrl);
		sendMessage(inUser, "activation.ftl",
				this.serviceProperties.getActivationEmailSubject(), params);
	}
	
	@Override
	public void sendPasswordChangeMessage(User inUser) throws EmailException {
		sendMessage(inUser, "password.ftl",
				this.serviceProperties.getPasswordChangeEmailSubject());
	}
	
	@Override
	public void sendPasswordResetMessage(User inUser, String inNewPassword)
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
	 *            contents of the email.
	 * @param subject The subject for the email being sent.
	 * @param params The parameters to be used to substitute values in the email
	 *            template.
	 * @throws EmailException Thrown if there are any errors in generating
	 *             content from the template, composing the email, or sending
	 *             the email.
	 */
	private void sendMessage(final User inUser, final String templateName,
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
	 *            contents of the email.
	 * @param subject The subject for the email being sent.
	 * @throws EmailException Thrown if there are any errors in generating
	 *             content from the template, composing the email, or sending
	 *             the email.
	 */
	private void sendMessage(final User inUser, 
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
	 *            contents of the email.
	 * @param subject The subject for the email being sent.
	 * @param emailAddress Sends the email to this address.
	 * @param params The template is merged with these parameters to generate
	 *            the content of the email.
	 * @throws EmailException Thrown if there are any errors in generating
	 *             content from the template, composing the email, or sending
	 *             the email.
	 */
	private void sendMessage(final String templateName, final String subject,
			final String emailAddress, final Map<String, Object> params)
			throws EmailException {
		Writer stringWriter = new StringWriter();
		try {
			Template template = this.configuration.getTemplate(templateName);
			template.process(params, stringWriter);
		} catch (TemplateException e) {
			throw new EmailException(e);
		} catch (IOException e) {
			throw new EmailException(e);
		}

		String content = stringWriter.toString();
		MimeMessage message = new MimeMessage(this.session);
		try {
			InternetAddress fromEmailAddress = null;
			String fromEmailAddressStr = 
					this.serviceProperties.getFromEmailAddress();
			if (fromEmailAddressStr != null) {
				fromEmailAddress = new InternetAddress(fromEmailAddressStr);
			}
			if (fromEmailAddress == null) {
				fromEmailAddress = 
						InternetAddress.getLocalAddress(this.session);
			}
			if (fromEmailAddress == null) {
				fromEmailAddress = new InternetAddress("no-reply@" + 
						PublicUrlGenerator.generate(this.request));
			}
			message.setFrom(fromEmailAddress);
			message.setSubject(subject);
			message.setContent(content, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					emailAddress));
			message.setSender(fromEmailAddress);
			Transport.send(message);
		} catch (AddressException e) {
			throw new EmailException(e);
		} catch (MessagingException e) {
			throw new EmailException(e);
		}
	}
	
//	private void responseUrl() {
//		if (result == null) {
//			try {
//				result = InetAddress.getLocalHost().getHostName();
//			} catch (UnknownHostException ex) {
//				LOGGER.warn("Eureka! could not determine the server's " +
//						"hostname. Falling back to http://localhost");
//				result = "http://localhost";
//			}
//		}
//		int port = this.servletRequest.getServerPort();
//		if (port != 80) {
//			result += ":" + port;
//		}
//		return result;
//	}
}
