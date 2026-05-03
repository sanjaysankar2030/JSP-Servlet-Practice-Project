package com.myapp.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * SendEmail.java
 * ---------------
 * Utility class to send emails using JavaMail API.
 * This is used to send the verification link to the user after signup.
 *
 * HOW IT WORKS:
 *   - We configure Gmail SMTP settings.
 *   - We create a session with authentication.
 *   - We compose the email and send it.
 *
 * IMPORTANT: You must use a Gmail account and enable:
 *   1. 2-Step Verification on your Google account
 *   2. Generate an "App Password" at: https://myaccount.google.com/apppasswords
 *      Use that App Password below (NOT your normal Gmail password)
 */
public class SendEmail {

    // ✅ Replace these with your Gmail address and App Password
    private static final String FROM_EMAIL = "youremail@gmail.com";
    private static final String EMAIL_PASSWORD = "your-app-password"; // 16-char App Password

    /**
     * Sends a plain HTML email.
     *
     * @param toEmail  The recipient's email address
     * @param subject  The email subject line
     * @param body     The HTML body of the email
     * @throws MessagingException if sending fails
     */
    public static void send(String toEmail, String subject, String body)
        throws MessagingException {
        // Step 1: Configure SMTP properties for Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Use TLS encryption
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Step 2: Create a mail session with authentication
        Session session = Session.getInstance(
            props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        FROM_EMAIL,
                        EMAIL_PASSWORD
                    );
                }
            }
        );

        // Step 3: Compose the email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(toEmail)
        );
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8"); // Send as HTML

        // Step 4: Send the email
        Transport.send(message);

        System.out.println("✅ Email sent successfully to: " + toEmail);
    }
}
