package com.devopsbuddy.test.intergation;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Jayden on 7/17/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    private PasswordResetTokenService passwordRestTokenService;

    @Rule public TestName testName = new TestName();

    @Test
    public void testCreateNewTokenForUserEmail() throws Exception{
        User user = createUser(testName);
        PasswordResetToken passwordRestToken = passwordRestTokenService.createPasswordResetTokenForEmail(user.getEmail());
        Assert.assertNotNull(passwordRestToken);
        Assert.assertNotNull(passwordRestToken.getToken());
    }

    @Test
    public void testFindByToken() throws Exception{
        User user = createUser(testName);

        PasswordResetToken passwordRestToken = passwordRestTokenService.createPasswordResetTokenForEmail(user.getEmail());
        Assert.assertNotNull(passwordRestToken);
        Assert.assertNotNull(passwordRestToken.getToken());

        PasswordResetToken token = passwordRestTokenService.findByToken(passwordRestToken.getToken());
        Assert.assertNotNull(token);
    }
}
