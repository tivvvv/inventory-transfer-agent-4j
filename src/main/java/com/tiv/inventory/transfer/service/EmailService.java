package com.tiv.inventory.transfer.service;

import java.util.Map;

public interface EmailService {

    void sendTemplateEmail(String receiver, Map<String, Object> variables);

}