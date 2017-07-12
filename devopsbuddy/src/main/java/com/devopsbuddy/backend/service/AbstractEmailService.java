package com.devopsbuddy.backend.service;

import com.devopsbuddy.web.domain.frontend.FeedBackPojo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by Jayden on 7/12/2017.
 */

@Service
public abstract class AbstractEmailService implements EmailService {

    @Value("${default.to.address}")
    private String defaultToAddress;

    /**
     * Creates a Simple Mail Message from feedback pojo.
     * @param feedback The Feedback Pojo.
     */

    protected SimpleMailMessage preparedSimpleMailMessageFromFeedbackPojo(FeedBackPojo feedback){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(defaultToAddress);
        simpleMailMessage.setFrom(feedback.getEmail());
        simpleMailMessage.setSubject("[DevOps Buddy]: Feedback received from " + feedback.getFirstName() + " " +
        feedback.getLastName() + "!");
        simpleMailMessage.setText(feedback.getFeedback());

        return simpleMailMessage;
    }

    @Override
    public void sendFeedbackEmail(FeedBackPojo feedback) {
        sendGenericEmailMessage(preparedSimpleMailMessageFromFeedbackPojo((feedback)));
    }
}
