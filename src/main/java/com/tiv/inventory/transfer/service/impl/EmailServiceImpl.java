package com.tiv.inventory.transfer.service.impl;

import com.tiv.inventory.transfer.common.BusinessCodeEnum;
import com.tiv.inventory.transfer.exception.BusinessException;
import com.tiv.inventory.transfer.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String EMAIL_TEMPLATE = "EmailTemplate";

    @Override
    public void sendTemplateEmail(String receiver, Map<String, Object> variables) {
        Context context = new Context();
        variables.forEach(context::setVariable);
        String htmlContent = templateEngine.process(EMAIL_TEMPLATE, context);
        this.sendHtmlEmail(receiver, htmlContent);
    }

    private void sendHtmlEmail(String receiver, String htmlContent) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject("AI智能库存调拨单通知");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("sendHtmlEmail--success, receiver: {}", receiver);
        } catch (Exception e) {
            log.error("sendHtmlEmail--fail, receiver: {}, htmlContent: {}", receiver, htmlContent, e);
            throw new BusinessException(BusinessCodeEnum.SYSTEM_ERROR);
        }
    }

}