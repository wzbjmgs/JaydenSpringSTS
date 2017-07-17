package com.devopsbuddy.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Jayden on 7/13/2017.
 */
@Configuration
@EnableJpaRepositories(basePackages="com.devopsbuddy.backend.persistence.repositories")
@EntityScan(basePackages="com.devopsbuddy.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:${user.home}/Documents/Udemy/fullStack/devopsBuddy/application-common.properties")
public class ApplicationConfig {
}
