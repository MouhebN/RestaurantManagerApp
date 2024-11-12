package tn.esprit.restaurantmanagerapp.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {

    public void sendWelcomeEmail(String toEmail, String username) {
        String subject = "Welcome to Restaurant Manager App";
        String body = "Hi " + username + ",\n\nThank you for signing up with us! We are excited to have you on board.";

        // Send the email on a background thread using AsyncTask
        new SendEmailTask().execute(toEmail, subject, body);
    }

    // AsyncTask to send email on a background thread
    private static class SendEmailTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String toEmail = params[0];
            String subject = params[1];
            String body = params[2];

            String fromEmail = "salem.dahmani345@gmail.com";  // Your email
            String password = "miev okfz rxch ojcv";  // Your email password

            // Set up properties for the SMTP server (Gmail SMTP server settings)
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.googlemail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.starttls.enable", "true");  // TLS encryption
            properties.put("mail.smtp.connectiontimeout", "10000");  // Connection timeout in ms
            properties.put("mail.smtp.timeout", "10000");  // Timeout for reading response in ms

            // Create a session with authentication
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                // Create a new email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                message.setSubject(subject);
                message.setText(body);

                // Send the message
                Transport.send(message);
                Log.d("EmailSender", "Email Sent Successfully!");
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("EmailSender", "Error in sending email: " + e.getMessage());
            }

            return null;
        }
    }
}
