package com.devopsbuddy.utils;

import com.devopsbuddy.backend.persistence.domain.backend.User;

/**
 * Created by Jayden on 7/13/2017.
 */
public class UserUtils {

    private UserUtils(){
        throw new AssertionError("Non instantiable");
    }

    public static User createBasicUser(){
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
