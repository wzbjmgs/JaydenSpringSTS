package com.devopsbuddy.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Jayden on 7/12/2017.
 */
@Controller
public class PayloadController {

    public static final String PAYLOAD_VIEW_NAME = "payload/payload";

    @GetMapping("/payload")
    public String payload(){
        return PAYLOAD_VIEW_NAME;
    }

}
