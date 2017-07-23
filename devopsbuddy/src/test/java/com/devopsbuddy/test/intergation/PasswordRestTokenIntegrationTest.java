package com.devopsbuddy.test.intergation;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Jayden on 7/17/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordRestTokenIntegrationTest extends AbstractIntegerationTest{

    @Value("${token.expiration.length.minutes}")
    private int expirationTimeInMinutes;

    @Autowired
    private PasswordResetTokenRepository passwordRestTokenRepository;

    @Rule public TestName testName = new TestName();

    @Before
    public void init(){
        Assert.assertFalse(expirationTimeInMinutes == 0);
    }

    @Test
    public void testTokenExpirationlenght() throws Exception{

        User user = createUser(testName);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());

        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        String token = UUID.randomUUID().toString();

        LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);

        PasswordResetToken passwordRestToken = createPasswordResetToken(token, user, now);

        LocalDateTime actualTime = passwordRestToken.getExpiryDate();
        userRepository.delete(user.getId());
        Assert.assertNotNull(actualTime);
        Assert.assertEquals(actualTime,expectedTime);
    }

    @Test
    public void testFindTokenByTokenValue() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user, now);

        PasswordResetToken retrievePasswordRestToken = passwordRestTokenRepository.findByToken(token);
        userRepository.delete(user.getId());
        Assert.assertNotNull(retrievePasswordRestToken);
        Assert.assertNotNull(retrievePasswordRestToken.getId());
        Assert.assertNotNull(retrievePasswordRestToken.getUser());
    }

    @Test
    public void testDeleteToken() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordRestToken = createPasswordResetToken(token, user, now);
        long tokenId = passwordRestToken.getId();
        passwordRestTokenRepository.delete(tokenId);

        PasswordResetToken shouldNotExistToken = passwordRestTokenRepository.findOne(tokenId);
        userRepository.delete(user.getId());
        Assert.assertNull(shouldNotExistToken);
    }

    @Test
    public void testCascadeDeleteFromUserEntity() throws Exception{
        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        PasswordResetToken passwordRestToken = createPasswordResetToken(token, user, now);
        userRepository.delete(user.getId());

        Set<PasswordResetToken> shouldBeEmpty = passwordRestTokenRepository.findAllByUserId(user.getId());
        Assert.assertTrue(shouldBeEmpty.isEmpty());
    }

    @Test
    public void testMultipleTokensAreReturnedWhenQueringBuUserId() throws Exception{
        User user = createUser(testName);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        String token1 = UUID.randomUUID().toString();
        String token2 = UUID.randomUUID().toString();
        String token3 = UUID.randomUUID().toString();

        Set<PasswordResetToken> tokens = new HashSet<PasswordResetToken>();
        tokens.add(createPasswordResetToken(token1, user, now));
        tokens.add(createPasswordResetToken(token2, user, now));
        tokens.add(createPasswordResetToken(token3, user, now));

        passwordRestTokenRepository.save(tokens);

        User founder = userRepository.findOne(user.getId());

        Set<PasswordResetToken> actualTokens = passwordRestTokenRepository.findAllByUserId(founder.getId());
        Assert.assertTrue(actualTokens.size() == tokens.size());
        List<String> tokenAsList = tokens.stream().map(prt->prt.getToken()).collect(Collectors.toList());
        List<String> actualTokensAsList = actualTokens.stream().map(prt->prt.getToken()).collect(Collectors.toList());
        userRepository.delete(user.getId());
        Assert.assertEquals(tokenAsList, actualTokensAsList);

    }

    private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now){
        PasswordResetToken passwordRestToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
        passwordRestTokenRepository.save(passwordRestToken);
        Assert.assertNotNull(passwordRestToken.getId());
        return passwordRestToken;
    }
}
