package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.config.AppProperties;
import com.github.xioshe.less.url.entity.EmailTemplate;
import com.github.xioshe.less.url.repository.EmailTemplateRepository;
import com.github.xioshe.less.url.service.common.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailTemplateRepository templateRepository;

    @Mock
    private AppProperties app;

    @InjectMocks
    private EmailService emailService;
    @BeforeEach
    void setup() {
        // Mock any necessary dependencies here
        ReflectionTestUtils.setField(emailService, "fromAddress", "me@example.com");
    }


    @Test
    void sendTemplateEmail_shouldSendEmailWithTemplate() throws Exception {
        String to = "test@example.com";
        String templateName = "testTemplate";
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "John");

        EmailTemplate template = new EmailTemplate();
        template.setSubject("Test Subject");
        template.setContent("Hello, {{name}}!");

        when(app.isMockEmail()).thenReturn(false);

        when(templateRepository.findByName(templateName)).thenReturn(template);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(to, templateName, variables);

        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendTemplateEmail_shouldNotSendEmailInNonProdEnvironment() {
        String to = "test@example.com";
        String templateName = "testTemplate";
        Map<String, Object> variables = new HashMap<>();

        when(app.isMockEmail()).thenReturn(true);

        when(templateRepository.findByName(templateName)).thenReturn(new EmailTemplate());

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(to, templateName, variables);

        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}