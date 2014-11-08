/**
 * Email functionality of LOL
 */
package lol;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * 
 * @author aviral
 *
 */
public class LOLEmail {

	public static void send(String to, String task) {

		// Sender's email ID needs to be mentioned
		String from = Constants.LOLEmailId;

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.user", Constants.LOLEmailId);
		properties.setProperty("mail.password", Constants.LOLEmailPasswd);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", Constants.LOLEmailHost);
		properties.setProperty("mail.smtp.port", Constants.LOLEmailPort);
		properties.setProperty("mail.smtp.user", Constants.LOLEmailId);
		properties.setProperty("mail.smtp.password", Constants.LOLEmailPasswd);
		// Get the default Session object.

		Session session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(Constants.LOLEmailId,
								Constants.LOLEmailPasswd); // user name and
															// password
					}
				});
		try {

			// default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Subject: header field
			message.setSubject("Alert: You have an upcoming task");

			// the actual message
			message.setText(task);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}