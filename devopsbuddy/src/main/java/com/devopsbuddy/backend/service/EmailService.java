package com.devopsbuddy.backend.service;

import com.devopsbuddy.web.domain.frontend.FeedBackPojo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by Jayden on 7/12/2017.
 */

public interface EmailService {
    /**
     * Sends an email with the content in the Feedback POJO
     * @param feedbackPojo The feedback POJO
     */

    public void sendFeedbackEmail(FeedBackPojo feedBackPojo);

    /**
     * end an email with the content of simple Mail Message object;
     * @param message The object containing the email content
     */
    public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage);
}
