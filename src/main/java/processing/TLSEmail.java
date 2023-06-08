/**
 * @author CSE 110 - Team 12
 */
package processing;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * Outgoing Mail (SMTP) Server
 * requires TLS or SSL
 * Use Authentication: Yes
 */
public class TLSEmail {

	//fields for starting session
	String fromEmail;
	String smtpHost;
	String tlsPort;
	String password;

	/**
	 * Starts email session
	 * 
	 * @return the Session to send email with
	 */
	public Session startEmailSession() {

		//configure session
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);               //SMTP Host
		props.put("mail.smtp.port", tlsPort);                //TLS Port
		props.put("mail.smtp.auth", "true");                 //enable authentication
		props.put("mail.smtp.starttls.enable", "true");      //enable STARTTLS

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

	/**
	 * Setter for fromEmail field
	 * 
	 * @param fromEmail
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * Setter for SMTPHost field
	 * 
	 * @param smtpHost
	 */
	public void setSMTPHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Setter for TLSPort field
	 * @param tlsPort
	 */
	public void setTLSPort(String tlsPort) {
		this.tlsPort = tlsPort;
	}

	/**
	 * Setter for password field
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
