package org.springframework.boot.autoconfigure.mail;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({MailSenderAutoConfiguration.class})
@ConditionalOnSingleCandidate(JavaMailSenderImpl.class)
@ConditionalOnProperty(prefix = "spring.mail", value = {"test-connection"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mail/MailSenderValidatorAutoConfiguration.class */
public class MailSenderValidatorAutoConfiguration {
    private final JavaMailSenderImpl mailSender;

    public MailSenderValidatorAutoConfiguration(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @PostConstruct
    public void validateConnection() {
        try {
            this.mailSender.testConnection();
        } catch (MessagingException ex) {
            throw new IllegalStateException("Mail server is not available", ex);
        }
    }
}
