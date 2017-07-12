package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.web.domain.frontend.FeedBackPojo;
import org.hibernate.validator.constraints.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by Jayden on 7/11/2017.
 */
@Controller
public class ContactController {

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);

    public static final String FEEDBACK_MODEL_KEY = "feedback";

    private static final String CONTACT_US_VIEW_NAME = "contact/contact";

    @Autowired
    public EmailService emailService;

    @GetMapping("/contact")
    public String contactGet(ModelMap model){
        FeedBackPojo feedBackPojo = new FeedBackPojo();
        model.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedBackPojo);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

    @PostMapping("/contact")
    public String contactPost(@ModelAttribute(FEEDBACK_MODEL_KEY) FeedBackPojo feedback){
        LOG.debug("Feedback POJO content: {}", feedback);
//        LOG.debug("User.home path: {}", System.getProperty("user.home"));
        emailService.sendFeedbackEmail(feedback);
        return ContactController.CONTACT_US_VIEW_NAME;
    }

}
