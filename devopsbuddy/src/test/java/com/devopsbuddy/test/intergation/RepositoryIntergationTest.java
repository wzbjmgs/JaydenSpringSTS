package com.devopsbuddy.test.intergation;


import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnums;
import com.devopsbuddy.utils.UserUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Temporal;
import java.util.HashSet;
import java.util.Set;

import static com.devopsbuddy.enums.PlanEnum.*;

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

    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(roleRepository);
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createBasicPlan(BASIC);
        planRepository.save(basicPlan);

        Plan retrievePlan = planRepository.findOne(BASIC.getId());
        Assert.assertNotNull(retrievePlan);
    }

    @Test
    public void testCreateRole() throws Exception{
        Role userRole = createBasicRole(RolesEnums.BASIC);
        roleRepository.save(userRole);

        Role retrieveRole = roleRepository.findOne(RolesEnums.BASIC.getId());
        Assert.assertNotNull(retrieveRole);
    }

    @Test
    public void createNewUser() throws Exception{
        User basicUser = createUser();

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
        User basicUser = createUser();
        userRepository.delete(basicUser.getId());

    }

    private User createUser(){
        Plan basicPlan = createBasicPlan(BASIC);
        planRepository.save(basicPlan);

        User basicUser = UserUtils.createBasicUser();
        basicUser.setPlan(basicPlan);

        Role basicRole = createBasicRole(RolesEnums.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<UserRole>();
        UserRole userRole = new UserRole(basicUser,basicRole);
        userRoles.add(userRole);

        basicUser.getUserRoles().addAll(userRoles);
        basicUser = userRepository.save(basicUser);

        return basicUser;
    }

    private Plan createBasicPlan(PlanEnum planEnum){
        return new Plan(planEnum);
    }

    private Role createBasicRole(RolesEnums rolesEnums){
        return new Role(rolesEnums);
    }


}
