package com.devopsbuddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jayden on 7/11/2017.
 */
@Controller
public class CopyController {

    @GetMapping("/about")
    public String about(){
        return "copy/about";
    }

}
