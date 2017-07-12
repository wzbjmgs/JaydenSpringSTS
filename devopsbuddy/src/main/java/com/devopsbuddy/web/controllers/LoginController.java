package com.devopsbuddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jayden on 7/12/2017.
 */
@Controller
public class LoginController {

    /*The login view name*/
    public static final String LOGIN_VIEW_NAME = "user/login";

    @GetMapping("/login")
    public String login(){
        return LoginController.LOGIN_VIEW_NAME;
    }
}
