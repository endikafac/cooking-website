package com.cookingwebsite.crud.email;

import java.util.Arrays;
import java.util.List;

import com.aspose.email.MailMessage;
import com.aspose.email.SecurityOptions;
import com.aspose.email.SmtpClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendEmails {

	private static final String SEPARATOR = ",";
	// Mail configurations
	private static final String HOST = "smtp.gmail.com";
	private static final Integer PORT = 465;
	private static final String USERNAME = "cooking.website.tc@gmail.com";
	private static final String FROM = USERNAME;
	private static final String PASSWORD = "n4UrWBJxgkLPCDv";
	
	public static void SendEmail(String to, String subject, String body) {

		try {
			SmtpClient client = new SmtpClient(SendEmails.HOST, SendEmails.PORT, SendEmails.USERNAME, SendEmails.PASSWORD);

			// Set Security options for the server
			client.setSecurityOptions(SecurityOptions.Auto);
			// client.setSecurityOptions(SecurityOptions.SSLAuto);

			// Create a new Email Message
			// MailMessage msg = new MailMessage(SendEmails.from, to, subject, body);
			MailMessage msg = new MailMessage(SendEmails.FROM, to);
			msg.setSubject(subject);
			msg.setHtmlBody(body);
			
			log.info("Sending message...");

			// Send the Message now
			client.send(msg);
			log.info("Message sent.");
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			log.error(e.getMessage());
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	public static void SendEmail(List<String> to, String subject, String body) {
		String allTo = String.join(SEPARATOR, to);
		SendEmails.SendEmail(allTo, subject, body);
		
	}
	
}
