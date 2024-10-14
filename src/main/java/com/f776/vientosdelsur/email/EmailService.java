package com.f776.vientosdelsur.email;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.verify.host}")
    private String host;

    private MimeMessage getMimeMessage() {
        return mailSender.createMimeMessage();
    }

    @Async
    @Override
    public void sendVerificationEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "nombre", name,
                    "email", to,
                    "urlVerificacion", host + "/verify?token=" + token));

            String html = templateEngine.process("verificar-email.html", context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setSubject("Confimación de nueva cuenta en Vientos del Sur ");
            helper.setFrom(fromEmail);
            helper.setTo(to);

            // HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(html, MediaType.TEXT_HTML_VALUE + "; charset=" + StandardCharsets.UTF_8.name());
            mimeMultipart.addBodyPart(messageBodyPart);

            // Images
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource("src/main/resources/static/logo.png");
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "logo");
            mimeMultipart.addBodyPart(imageBodyPart);

            message.setContent(mimeMultipart);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
