package lol;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class LOLEmail {
	
	public static void send(String to, String task) {
		// Recipient's email ID needs to be mentioned.
		to = "aviralprakash@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "alert.lifeonline@gmail.com";

		// Assuming you are sending email from localhost
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.user", "alert.lifeonline@gmail.com");
		properties.setProperty("mail.password", "lifeonline99");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.user", "alert.lifeonline@gmail.com");
		properties.setProperty("mail.smtp.password", "lifeonline99");
		// Get the default Session object.

		Session session = Session.getDefaultInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"alert.lifeonline@gmail.com", "lifeonline99");// Specify
																				// the
																				// Username
																				// and
																				// the
																				// PassWord
					}
				});
		try {

			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject("Alert: You have an upcoming task");

			// Now set the actual message
			message.setText(task);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}