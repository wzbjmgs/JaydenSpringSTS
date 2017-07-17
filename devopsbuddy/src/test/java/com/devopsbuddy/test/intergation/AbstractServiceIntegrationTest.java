package com.devopsbuddy.test.intergation;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnums;
import com.devopsbuddy.utils.UserUtils;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jayden on 7/17/2017.
 */
public abstract class AbstractServiceIntegrationTest {

    @Autowired
    private UserService userService;

    protected User createUser(TestName testName) {
        String username = testName.getMethodName();
        String email =testName.getMethodName() + "@devopsBuddy.com";

        Set<UserRole> userRoles = new HashSet<UserRole>();
        User basicUser = UserUtils.createBasicUser(username,email);
        userRoles.add(new UserRole(basicUser, new Role(RolesEnums.BASIC)));

        return userService.createUser(basicUser, PlanEnum.BASIC, userRoles);
    }
}
