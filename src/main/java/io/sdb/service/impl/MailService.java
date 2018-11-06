package io.sdb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  @Async
  public void sendMail(String title, String content, String email) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setSubject(title);
    message.setTo(email);
    message.setText(content);
    mailSender.send(message);
  }
}
