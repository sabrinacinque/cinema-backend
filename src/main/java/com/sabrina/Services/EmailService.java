package com.sabrina.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void inviaEmailConferma(String destinatario, String oggetto, String messaggio) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destinatario);
        email.setSubject(oggetto);
        email.setText(messaggio);
        mailSender.send(email);
    }
    
    
    public void inviaEmailConAllegatoHTML(String to, String subject, String htmlBody) {
        try {
          MimeMessage msg = mailSender.createMimeMessage();
          MimeMessageHelper helper = new MimeMessageHelper(msg, /* multipart*/ true, "UTF-8");
          helper.setTo(to);
          helper.setSubject(subject);
          // true = è HTML
          helper.setText(htmlBody, true);
          mailSender.send(msg);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    
    
}
