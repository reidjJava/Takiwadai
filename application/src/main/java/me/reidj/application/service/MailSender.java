package me.reidj.application.service;

import me.reidj.client.util.Utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    private static final String RESOURCES_PATH = "application/src/main/resources/";

    private final Properties systemProperties = System.getProperties();
    private final Properties properties = Utils.loadFile(RESOURCES_PATH + "mail.properties");

    public MailSender() {
        systemProperties.setProperty("mail.smtp.host", properties.getProperty("mail.host"));
        systemProperties.setProperty("mail.smtp.port", properties.getProperty("mail.port"));
        systemProperties.setProperty("mail.smtp.ssl.enable", "true");
        systemProperties.setProperty("mail.smtp.auth", "true");
    }

    public void send(String to, String subject, String text) {
        Session session = Session.getInstance(
                systemProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(properties.getProperty("mail.username"), properties.getProperty("mail.password"));
                    }
                }
        );
        generateMessage(to, session, subject, text);
    }

    private void generateMessage(String to, Session session, String subject, String text) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.username")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Takiwadai | " + subject);
            message.setText(text);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
