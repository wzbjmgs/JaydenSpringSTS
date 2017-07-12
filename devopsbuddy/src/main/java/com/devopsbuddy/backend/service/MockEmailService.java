package com.devopsbuddy.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by Jayden on 7/12/2017.
 */

@Service
public class MockEmailService extends AbstractEmailService {

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);
    
    @Override
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage) {
        LOG.debug("Simulating an email service...");
        LOG.info(simpleMailMessage.toString());
        LOG.info("Email sent.");
    }
}
