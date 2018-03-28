package com.ibc.notification.email.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;

import com.ibc.notification.ISendMessage;

public class SendMessageSmtp implements ISendMessage {

	Properties props;
	List<Message> messages;
	Session session;
	Transport t;

	public SendMessageSmtp() {
		messages = new ArrayList<Message>();
		props = System.getProperties();
	
		try {
			session = Session.getInstance(props, null);
			t = session.getTransport();
		} catch (NoSuchProviderException e) {
			
			e.printStackTrace();
		}
	}
	/*
	 * if (mailhost != null) props.put("mail." + prot + ".host", mailhost); if
	 * (auth) props.put("mail." + prot + ".auth", "true");
	 */

	/*
	 * Create a Provider representing our extended SMTP transport and set the
	 * property to use our provider.
	 *
	 * Provider p = new Provider(Provider.Type.TRANSPORT, prot,
	 * "smtpsend$SMTPExtension", "JavaMail demo", "no version"); props.put("mail." +
	 * prot + ".class", "smtpsend$SMTPExtension");
	 */

	// Get a Session object
	public void sendMessage() {
		try {
			t.connect();
			for (Message m : messages) {
				m.saveChanges();
				t.sendMessage(m, m.getAllRecipients());
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				t.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
