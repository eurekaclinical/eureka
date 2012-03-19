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

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ApplicationProperties;
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
	private final ApplicationProperties applicationProperties;

	/**
	 * Default constructor, creates a FreeMarker configuration object.
	 * 
	 * @param inApplicationProperties The application configuration object.
	 * @param inSession The mail session to use when sending a message.
	 */
	@Inject
	public FreeMarkerEmailSender(
			final ApplicationProperties inApplicationProperties,
			final Session inSession) {
		this.applicationProperties = inApplicationProperties;
		this.session = inSession;
		this.configuration = new Configuration();
		this.configuration.setClassForTemplateLoading(this.getClass(),
				"/templates/");
		this.configuration.setObjectWrapper(new DefaultObjectWrapper());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#sendMessage(
	 * java.lang.String)
	 */
	@Override
	public void sendVerificationMessage(final User inUser)
			throws EmailException {
		sendMessage(inUser, "verification.ftl",
				this.applicationProperties.getVerificationEmailSubject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#
	 * sendActivationMessage(edu.emory.cci.aiw.cvrg.eureka.common.entity.User)
	 */
	@Override
	public void sendActivationMessage(final User inUser) throws EmailException {
		sendMessage(inUser, "activation.ftl",
				this.applicationProperties.getActivationEmailSubject());
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
		sendMessage(inUser, "password.ftl",
				this.applicationProperties.getPasswordChangeEmailSubject());
	}

	/**
	 * Send an email to the given use, using contents generated from the given
	 * template.
	 * 
	 * @param inUser The user to whom the message should be sent.
	 * @param templateName The name of the template used to generate the
	 *            contents of the email.
	 * @param subject The subject for the email being sent.
	 * @throws EmailException Thrown if there are any errors in generating
	 *             content from the template, composing the email, or sending
	 *             the email.
	 */
	private void sendMessage(final User inUser, final String templateName,
			final String subject) throws EmailException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user", inUser);
		params.put("config", this.applicationProperties);
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
			message.setSubject(subject);
			message.setContent(content, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					inUser.getEmail()));
			Transport.send(message);
		} catch (AddressException e) {
			throw new EmailException(e);
		} catch (MessagingException e) {
			throw new EmailException(e);
		}
	}
}
