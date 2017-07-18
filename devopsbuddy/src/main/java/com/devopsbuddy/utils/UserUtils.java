package com.devopsbuddy.utils;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Jayden on 7/13/2017.
 */
public class UserUtils {

    private UserUtils(){
        throw new AssertionError("Non instantiable");
    }

    public static User createBasicUser(String username, String email){
        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("firstName");
        user.setLastname("lastName");
        user.setPhoneNumber("123456789");
        user.setCountry("GB");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfileImageUrl("http://sefwef");

        return user;
    }

    public static String createPasswordResetUrl(HttpServletRequest request, long userId, String token) {
        String passwordResetUrl =
                request.getScheme()+
                        "://"+
                        request.getServerName()+
                        ":" +
                        request.getServerPort() +
                        request.getContextPath()+
                        ForgotMyPasswordController.CHANGE_PASSWORD_PATH+
                        "?id="+
                        userId+
                        "&token="+
                        token;
        return passwordResetUrl;
    }
}
