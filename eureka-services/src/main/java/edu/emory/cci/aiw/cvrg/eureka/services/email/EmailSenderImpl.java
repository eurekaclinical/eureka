package edu.emory.cci.aiw.cvrg.eureka.services.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Send emails to users when a notable event regarding the user occurs in the
 * system.
 * 
 * @author hrathod
 * 
 */
public class EmailSenderImpl implements EmailSender {
	/**
	 * The email session to be used to send the email.
	 */
	private final Session session;

	/**
	 * The default constructor.
	 * 
	 * @throws NamingException
	 */
	public EmailSenderImpl() throws NamingException {
		final Context initCtx = new InitialContext();
		final Context envCtx = (Context) initCtx.lookup("java:comp/env");
		this.session = (Session) envCtx.lookup("mail/Session");
	}

	/* (non-Javadoc)
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.email.EmailSenderIF#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(final String user) throws MessagingException {
		MimeMessage message = new MimeMessage(this.session);
		// message subject
		message.setSubject("Eureka TESTING!");
		// message body
		message.setContent("Hi, you have registered with Eureka!", "text/plain");
		message.addRecipient(Message.RecipientType.TO,
				new InternetAddress(user));
		Transport.send(message);
	}

}
