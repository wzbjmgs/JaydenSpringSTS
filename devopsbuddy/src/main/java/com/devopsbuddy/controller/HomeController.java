package com.devopsbuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jayden on 7/10/2017.
 */

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

}
