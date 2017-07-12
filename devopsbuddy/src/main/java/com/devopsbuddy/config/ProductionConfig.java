package com.devopsbuddy.config;

import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Jayden on 7/12/2017.
 */
@Configuration
@Profile("prod")
@PropertySource("file:${user.home}/Documents/Udemy/fullStack/devopsBuddy/application-prod.properties")
public class ProductionConfig {

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }

}
