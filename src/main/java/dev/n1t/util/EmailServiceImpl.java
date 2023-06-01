package dev.n1t.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ninetenbankmail@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendpassResetEmail(String to, String confirmationLink) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String subject = "Account Confirmation";

            // Generate the HTML content with the button
            String htmlContent = "<html><body>"
                    + "<p>Please click the button below to reset your password:</p>"
                    + "<a href=\"" + confirmationLink + "\">Reset password</a>"
                    + "</body></html>";

            helper.setFrom(new InternetAddress("ninetenemailserver@gmail.com", "NineTen Bank"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set HTML content to true

            emailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.printf("An email could not be sent to %s%n", to);
        }
    }
}