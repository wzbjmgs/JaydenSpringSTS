package com.devopsbuddy.test.intergation;


import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jayden on 7/13/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepositoryIntergationTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private static final int BASIC_PLAN_ID = 1;
    private static final int BASIC_ROLE_ID = 1;

    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);

        Plan retrievePlan = planRepository.findOne(BASIC_PLAN_ID);
        Assert.assertNotNull(retrievePlan);
    }

    @Test
    public void testCreateRole() throws Exception{
        Role userRole = createBasicRole();
        roleRepository.save(userRole);

        Role retrieveRole = roleRepository.findOne(BASIC_ROLE_ID);
        Assert.assertNotNull(retrieveRole);
    }

    @Test
    public void createNewUser() throws Exception{
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);

        User basicUser = createBaiscUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole();
        Set<UserRole> userRoles = new HashSet<UserRole>();
        UserRole userRole = new UserRole();
        userRole.setUser(basicUser);
        userRole.setRole(basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);

        for(UserRole ur : userRoles){
            roleRepository.save(ur.getRole());
        }

        basicUser = userRepository.save(basicUser);

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

    private Plan createBasicPlan(){
        Plan plan = new Plan();
        plan.setId(BASIC_PLAN_ID);
        plan.setName("basic");
        return plan;
    }

    private Role createBasicRole(){
        Role role = new Role();
        role.setId(BASIC_ROLE_ID);
        role.setName("ROLE_USER");
        return role;
    }

    private User createBaiscUser(){
        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("zbwang7077@gmail.com");
        user.setFirstName("firstName");
        user.setLastname("lastName");
        user.setPhoneNumber("123456789");
        user.setCountry("GB");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfileImageUrl("http://sefwef");

        return user;
    }


}
