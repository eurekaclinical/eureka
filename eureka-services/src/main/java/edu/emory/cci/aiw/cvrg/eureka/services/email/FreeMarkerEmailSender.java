package edu.emory.cci.aiw.cvrg.eureka.services.email;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
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
	 * Default constructor, creates a FreeMarker configuration object.
	 * 
	 * @throws EmailException Thrown when the mail session can not be fetched
	 *             properly from the JNDI context.
	 */
	public FreeMarkerEmailSender() throws EmailException {
		this.configuration = new Configuration();
		this.configuration.setClassForTemplateLoading(
				FreeMarkerEmailSender.class, "/templates/");
		this.configuration.setObjectWrapper(new DefaultObjectWrapper());
		try {
			final Context initCtx = new InitialContext();
			final Context envCtx = (Context) initCtx.lookup("java:comp/env");
			this.session = (Session) envCtx.lookup("mail/Session");
		} catch (NamingException ne) {
			throw new EmailException(ne);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSender#sendMessage(
	 * java.lang.String)
	 */
	@Override
	public void sendMessage(final User inUser) throws EmailException {
		Template template;
		try {
			template = this.configuration.getTemplate("verification.ftl");
			Writer stringWriter = new StringWriter();
			template.process(inUser, stringWriter);
			String content = stringWriter.toString();
			System.out.println(content);
			MimeMessage message = new MimeMessage(this.session);
			// message subject
			message.setSubject("Eureka TESTING!");
			// message body
			message.setContent(content, "text/plain");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					inUser.getEmail()));
			Transport.send(message);

		} catch (IOException ioe) {
			throw new EmailException(ioe);
		} catch (TemplateException te) {
			throw new EmailException(te);
		} catch (MessagingException me) {
			throw new EmailException(me);
		}
	}

}
