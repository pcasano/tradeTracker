package com.tradeTracker.email;

import com.tradeTracker.configuration.Configuration;
import jakarta.activation.DataHandler;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MessageBuilder {

    private final Configuration configuration;
    protected final DecimalFormat df = new DecimalFormat("0.00");
    protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public MessageBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    protected void sendEmail(String table, String subject) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, null);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(configuration.getEmailData().getEmailSender());
            msg.setRecipients(
                    Message.RecipientType.TO,
                    configuration.getEmailData().getTargetEmail()
            );
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText("Hello, world!\n");
            msg.setContent(table, "text/html");
            Transport.send(
                    msg,
                    configuration.getEmailData().getEmailSender(),
                    configuration.getEmailData().getEmailSenderPass()
            );
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
        }
    }
}
