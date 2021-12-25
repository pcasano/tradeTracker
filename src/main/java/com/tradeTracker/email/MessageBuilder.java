package com.tradeTracker.email;

import com.tradeTracker.configuration.Configuration;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MessageBuilder {

    private final Configuration configuration;

    public MessageBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    protected void sendEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(configuration.getEmailData().getEmailSender());
            msg.setRecipients(Message.RecipientType.TO,
                    configuration.getEmailData().getTargetEmail());
            msg.setSubject("JavaMail hello world example");
            msg.setSentDate(new Date());
            msg.setText("Hello, world!\n");
            msg.setContent("<html><body><table><tr><td>Bubu<td>Lala</tr></table></body></html>", "text/html");
            Transport.send(msg, configuration.getEmailData().getEmailSender(), configuration.getEmailData().getEmailSenderPass());
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
}
