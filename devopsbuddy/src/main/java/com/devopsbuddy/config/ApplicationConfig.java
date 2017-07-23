package com.devopsbuddy.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
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
@PropertySource("file:${user.home}/.devopsbuddy/application-common.properties")
@PropertySource("file:${user.home}/.devopsbuddy/application-common.properties")
@PropertySource("file:${user.home}/.devopsbuddy/stripe.properties")
public class ApplicationConfig {

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Bean
    public AmazonS3 s3Client(){

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .withForceGlobalBucketAccessEnabled(true)
                .withCredentials(new ProfileCredentialsProvider(awsProfileName))
                .build();

        return s3Client;
    }

}
