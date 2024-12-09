package com.kelvin.ms_app.service;

import com.kelvin.ms_app.model.EmailRequest;
import com.kelvin.ms_app.model.ObjectResponse;
import com.kelvin.ms_app.model.mailAlertObj;


import com.kelvin.ms_app.util.JsonUtil;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private Environment env;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    ObjectResponse<?> response = new ObjectResponse<>();


    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = env.getProperty("application.mail.username");
            String password = env.getProperty("application.mail.password");
            return new PasswordAuthentication(username, password);
        }


    }

    @Override
    public ResponseEntity<?> handleEmail(EmailRequest request) {
        ObjectResponse objectResponse = new ObjectResponse();
        mailAlertObj alertObj= new mailAlertObj();

        alertObj.setMailto(request.getEmail());
        alertObj.setContentImg(request.getImageBase64());
        alertObj.setUsername(request.getUsername());

        objectResponse = this.sendEmail(alertObj);

        return new ResponseEntity<>(objectResponse, HttpStatus.OK);
    }

    @Override
    public ObjectResponse<?> sendEmail(mailAlertObj request) {
        ObjectResponse objectResponse = new ObjectResponse();

        try {
            // Get email server properties
            String emailHost = env.getProperty("application.mail.host");
            String emailPort = env.getProperty("application.mail.port");
            String useTls = env.getProperty("application.mail.use-tls");
            String useSsl = env.getProperty("application.mail.use-ssl");
            String isAuth = env.getProperty("application.mail.auth");

            Properties props = new Properties();
            props.put("application.mail.host", emailHost);
            props.put("application.mail.port", emailPort);
            props.put("application.mail.auth", isAuth);
            props.put("application.mail.use-tls", useTls);
            props.put("application.mail.use-ssl", useSsl);

            System.out.println(emailHost);
            System.out.println(isAuth);

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);

            //Generating the mailObj
            request.setMailsubject("Budget Report for: " + request.getUsername());
            request.setMailcontent("<pre><font face=\"Calibri\"><span style=\"font-size: 11pt\">\r\n"
                    + "Dear Recipients,\r\n"
                    + "\r\n"
                    + "Kindly review the summarized report.\r\n"
                    + "\r\n"
                    + "For any clarifications, please read our FAQs on <a href=\"http://localhost:4200/\">http://localhost:4200/</a>.\r\n"
                    + "\r\n");


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(env.getProperty("application.mail.username")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getMailto()));
            message.setSubject(request.getMailsubject());

            // Create MimeBodyPart for the image
            MimeBodyPart imagePart = new MimeBodyPart();
            byte[] imageBytes = Base64.getDecoder().decode(request.getContentImg().split(",")[1]); // Assuming "data:image/png;base64," prefix
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            imagePart.setDataHandler(new DataHandler(new ByteArrayDataSource(inputStream, "image/png")));
            imagePart.setHeader("Content-ID", "<image>");

            // Create MimeBodyPart for the base64 content
            MimeBodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(request.getMailcontent(), "text/html");

            // Create a multipart message
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(contentPart);
            multipart.addBodyPart(imagePart);

            // Set the multipart message to the email
            message.setContent(multipart);

            Transport.send(message);

            objectResponse.setSuccess(true);
            objectResponse.setMessage("Email sent successfully");
        } catch (Exception e) {
            logger.error("sendEmail error: {}", e);
            objectResponse.setSuccess(false);
            objectResponse.setMessage("Failed to send email: " + e.getMessage());
        }

        return objectResponse;
    }
}



