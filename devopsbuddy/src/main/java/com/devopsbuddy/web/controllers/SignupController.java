package com.devopsbuddy.web.controllers;

import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Jayden on 7/18/2017.
 */
@Controller
public class SignupController {

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    public static final String SIGN_UP_MAPPING = "/signup";

    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";

    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    @GetMapping(value=SIGN_UP_MAPPING)
    public String signupGet(@RequestParam("planId") int planId, ModelMap model){

        if(planId!= PlanEnum.BASIC.getId() && planId != PlanEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan id is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

}
