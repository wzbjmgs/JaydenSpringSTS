package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.backend.service.I18NService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jayden on 7/18/2017.
 */
@Controller
public class ForgotMyPasswordController {

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

    public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

    public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

    public static final String MALI_SEND_KEY = "mailSend";

    public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

    public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

    @Autowired
    private I18NService i18NService;

    @Autowired
    private EmailService emailService;

    @Value("${Webmaster.email}")
    private String webMasterEmail;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @GetMapping(value=FORGOT_PASSWORD_URL_MAPPING)
    public String forgotPasswordGet(){
        return EMAIL_ADDRESS_VIEW_NAME;
    }

    @PostMapping(value = FORGOT_PASSWORD_URL_MAPPING)
    public String forgotPasswordPost(HttpServletRequest request,
                                     @RequestParam("email") String email,
                                     ModelMap modelMap){

        PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

        if(passwordResetToken == null){
            LOG.warn("Counldn't find a password reset token for email {} ", email);
        }else {

            User user = passwordResetToken.getUser();
            String token = passwordResetToken.getToken();

            String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
            LOG.debug("Reset password url {}", resetPasswordUrl);

            String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(" [Devopsbuddy]: How to Reset your Password");
            mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
            mailMessage.setFrom(webMasterEmail);

            emailService.sendGenericEmailMessage(mailMessage);
        }

        modelMap.addAttribute(MALI_SEND_KEY, true);

        return EMAIL_ADDRESS_VIEW_NAME;
    }

}
