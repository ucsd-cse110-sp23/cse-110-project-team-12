package processing;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class TLSEmail {

	
	String fromEmail;
	String smtpHost;
	String tlsPort;
	String password;
	
	/**
	   Outgoing Mail (SMTP) Server
	   requires TLS or SSL
	   Use Authentication: Yes
	 */
	public Session startEmailSession() {

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost); //SMTP Host
		props.put("mail.smtp.port", tlsPort); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
             //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		Session session = Session.getInstance(props, auth);
		return session;
		
		
		
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}
	public void setSMTPHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public void setTLSPort(String tlsPort) {
		this.tlsPort = tlsPort;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
