/**
 * @author CSE 110 - Team 12
 */
package processing;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import interfaces.ErrorMessagesInterface;

/**
 * Class for sending emails
 * Uses session from TLSUtil
 *
 */
public class EmailUtil {

	/**
	 * Utility method to send simple HTML email
	 * @param session from TLSUtil
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @return boolean true if email sent, false otherwise
	 */
	public boolean sendEmail(Session session, String toEmail, String fromEmail, String subject, String body){
		try
		{
			MimeMessage msg = new MimeMessage(session);
			//set message headers
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(fromEmail);

			msg.setReplyTo(InternetAddress.parse(fromEmail, false));

			msg.setSubject(subject, "UTF-8");

			msg.setText(body, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

			Transport.send(msg);  

			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
