/*import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
*/

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;



public class EmailSender {


    /**
     * Sends a simple email to the specified recipient.
     *
     * This method composes and sends an email using SMTP with the provided
     * recipient's address, subject, and content. It handles authentication
     * using predefined sender credentials.
     *
     * @param recipientEmail The email address of the recipient.
     * @param subject The subject line of the email.
     * @param content The body content of the email.
     * @return true if the email was sent successfully, false otherwise.
     */
    public static boolean sendEmail(String recipientEmail, String subject, String content) {
        // Sender's email credentials
        String senderEmail = "rokadesejal15@gmail.com";
        String senderPassword = "xkpk ricv qmha oirt";

        // SMTP server details
        String smtpHost = "smtp.gmail.com";
        int smtpPort = 587; // TLS port

        // Create properties for the session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));

            // Add recipient
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

            // Set email subject and content
            message.setSubject(subject);
            message.setText(content);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully!");
            return  true;

        } catch (SendFailedException e) {
            // Handle invalid email addresses
            System.out.println("Failed to send email: Invalid recipient address - " + recipientEmail);
            return  false;
        }catch (MessagingException e) {
                // Handle invalid email addresses
                System.out.println("Failed to send email: Invalid recipient address - " + recipientEmail);
                return false;
        }
    }



    /**
     * Sends emails with attachments to multiple recipients.
     *
     * This method composes and sends an email with an attachment to each recipient
     * specified in the provided list. It uses SMTP for sending the emails and
     * requires sender authentication.
     *
     * @param recipientEmails A list of email addresses of the recipients.
     * @param subject The subject line of the email.
     * @param content The body content of the email.
     * @param attachment The file to be attached to the email.
     */
        // Method to send emails with attachments to multiple recipients
    public static void sendEmailWithAttachment(List<String> recipientEmails, String subject, String content, File attachment) {
            String senderEmail = "rokadesejal15@gmail.com";
            String senderPassword = "xkpk ricv qmha oirt";

            // Set up mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Create a new session with an authenticator
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            try {
                // Loop through the list of recipient emails and send the email to each
                for (String recipientEmail : recipientEmails) {
                    // Create a new email message
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(senderEmail));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                    message.setSubject(subject);

                    // Body of the email
                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setText(content);

                    // Multipart to include both text and attachment
                    Multipart multipart = new MimeMultipart();
                    multipart.addBodyPart(messageBodyPart);

                    // Attachment part
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    FileDataSource source = new FileDataSource(attachment);
                    attachmentPart.setDataHandler(new DataHandler(source));
                    attachmentPart.setFileName(attachment.getName());
                    multipart.addBodyPart(attachmentPart);

                    // Set the content of the message to be the multipart
                    message.setContent(multipart);

                    // Send the email
                    Transport.send(message);
                    System.out.println("Quiz sent successfully to " + recipientEmail);
                }

            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("Error sending email: " + e.getMessage());
            }
        }


    public static void sendEmailWithAttachmentAns(List<String> recipientEmails, String subject, String content, File attachment) {
        String senderEmail = "rokadesejal15@gmail.com";
        String senderPassword = "xkpk ricv qmha oirt";

        // Set up mail server properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a new session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Loop through the list of recipient emails and send the email to each
            for (String recipientEmail : recipientEmails) {
                // Create a new email message
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(senderEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                message.setSubject(subject);

                // Body of the email
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(content);

                // Multipart to include both text and attachment
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                // Attachment part
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(attachment);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(attachment.getName());
                multipart.addBodyPart(attachmentPart);

                // Set the content of the message to be the multipart
                message.setContent(multipart);

                // Send the email
                Transport.send(message);
                System.out.println("Answers sent successfully to " + recipientEmail);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}

