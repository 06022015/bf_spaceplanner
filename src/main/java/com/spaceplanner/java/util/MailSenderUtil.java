package com.spaceplanner.java.util;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;


public class MailSenderUtil {


    private Session session;
    private String fromInetAddress;

    public MailSenderUtil(Properties properties) {
        final String username = properties.getProperty("mail.smtp.user");
        final String password = properties.getProperty("mail.smtp.password");
        fromInetAddress = properties.getProperty(("mail.smtp.from.email"));
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private static Session getSession() {
        Properties props = new Properties();
        final String username = CommonUtil.getProperty("mail.smtp.user");
        final String password = CommonUtil.getProperty("mail.smtp.password");
        props.put("mail.smtp.starttls.enable", CommonUtil.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", CommonUtil.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", CommonUtil.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", CommonUtil.getProperty("mail.smtp.auth"));
        //props.put("mail.smtp.submitter ", "mdashifqureshi@yahoo.com");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        return session;
    }


    public void send(SimpleMailMessage simpleMailMessage, VelocityEngine velocityEngine, String templateName, Map<String, Object> model)
            throws MessagingException, VelocityException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromInetAddress));
        for (String to : simpleMailMessage.getTo()) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }
        msg.setSubject(simpleMailMessage.getSubject());
        String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
        msg.setText(body);
        Transport.send(msg);
    }
}
