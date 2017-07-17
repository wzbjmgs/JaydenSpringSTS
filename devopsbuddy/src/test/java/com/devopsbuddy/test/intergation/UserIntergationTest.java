package com.devopsbuddy.test.intergation;


import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnums;
import com.devopsbuddy.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static com.devopsbuddy.enums.PlanEnum.*;

/**
 * Created by Jayden on 7/13/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntergationTest extends AbstractIntegerationTest{

    /*Get method return test name;*/
    @Rule public TestName testName = new TestName();

    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createPlan(BASIC);
        planRepository.save(basicPlan);

        Plan retrievePlan = planRepository.findOne(BASIC.getId());
        Assert.assertNotNull(retrievePlan);
    }

    @Test
    public void testCreateRole() throws Exception{
        Role userRole = createRole(RolesEnums.BASIC);
        roleRepository.save(userRole);

        Role retrieveRole = roleRepository.findOne(RolesEnums.BASIC.getId());
        Assert.assertNotNull(retrieveRole);
    }

    @Test
    public void createNewUser() throws Exception{

        String username = testName.getMethodName();
        String email =testName.getMethodName() + "@devopsBuddy.com";

        User basicUser = createUser(username,email);

        User newLyCreateUser = userRepository.findOne(basicUser.getId());
        Assert.assertNotNull(newLyCreateUser);
        Assert.assertTrue(newLyCreateUser.isEnabled());
        Assert.assertNotNull(newLyCreateUser.getPlan());
        Assert.assertNotNull(newLyCreateUser.getPlan().getId());
        Set<UserRole> newLyCreateUserUserRoles = newLyCreateUser.getUserRoles();
        for(UserRole ur: newLyCreateUserUserRoles){
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }
    }

    @Test
    public void testDeleteUser()throws Exception{
        String username = testName.getMethodName();
        String email =testName.getMethodName() + "@devopsBuddy.com";

        User basicUser = createUser(username,email);
        userRepository.delete(basicUser.getId());

    }
}
