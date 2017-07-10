package com.devopsbuddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jayden on 7/10/2017.
 */

@Controller
public class indexController {

    @GetMapping("/")
    public String home(){
        return "index";
    }
}
