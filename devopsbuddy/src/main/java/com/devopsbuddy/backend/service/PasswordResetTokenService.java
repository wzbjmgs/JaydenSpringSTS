package com.devopsbuddy.backend.service;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Jayden on 7/17/2017.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordRestTokenRepository;

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeInMinutes;

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(PasswordResetTokenService.class);

    @Transactional
    public PasswordResetToken createPasswordResetTokenForEmail(String email){
        PasswordResetToken passwordRestToken = null;

        User user = userRepository.findByEmail(email);

        if(user != null){
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordRestToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
            passwordRestToken = passwordRestTokenRepository.save(passwordRestToken);
            LOG.debug("Successfully created token {} for user {} ",token, user.getUsername() );
        }else{
            LOG.warn("We couldn't find a user for the given email {} ", email);
        }

        return passwordRestToken;
    }

    public PasswordResetToken findByToken(String token) {
        return passwordRestTokenRepository.findByToken(token);
    }

}
