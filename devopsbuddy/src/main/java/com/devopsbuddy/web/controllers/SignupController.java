package com.devopsbuddy.web.controllers;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.PlanService;
import com.devopsbuddy.backend.service.S3Service;
import com.devopsbuddy.backend.service.StripeService;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.enums.RolesEnums;
import com.devopsbuddy.exceptions.S3Exception;
import com.devopsbuddy.exceptions.StripeException;
import com.devopsbuddy.utils.StripeUtils;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.domain.frontend.BasicAccountPayload;
import com.devopsbuddy.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Jayden on 7/18/2017.
 */
@Controller
public class SignupController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private StripeService stripeService;

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(SignupController.class);

    public static final String SIGN_UP_MAPPING = "/signup";

    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";

    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    public static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";

    public static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";

    public static final String SIGNED_UP_MESSAGE_KEY = "signedUp";

    public static final String ERROR_MESSAGE_KEY = "message";

    public static final String GENERIC_ERROR_VIEW_NAME = "error/genericError";

    @GetMapping(value=SIGN_UP_MAPPING)
    public String signupGet(@RequestParam("planId") int planId, ModelMap model){

        if(planId!= PlanEnum.BASIC.getId() && planId != PlanEnum.PRO.getId()){
            throw new IllegalArgumentException("Plan id is not valid");
        }
        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

    @PostMapping(value = SIGN_UP_MAPPING)
    public String signUpPost(@RequestParam(name = "planId", required = true) int planId,
                             @RequestParam(name = "file", required = false) MultipartFile file,
                             @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload,
                             ModelMap model) throws IOException {

        if (planId != PlanEnum.BASIC.getId() && planId != PlanEnum.PRO.getId()) {
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
            return SUBSCRIPTION_VIEW_NAME;
        }

        this.checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if (model.containsKey(DUPLICATED_USERNAME_KEY)) {
            LOG.warn("The username already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Username already exist");
            duplicates = true;
        }

        if (model.containsKey(DUPLICATED_EMAIL_KEY)) {
            LOG.warn("The email already exists. Displaying error to the user");
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            errorMessages.add("Email already exist");
            duplicates = true;
        }

        if (duplicates) {
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return SUBSCRIPTION_VIEW_NAME;
        }


        // There are certain info that the user doesn't set, such as profile image URL, Stripe customer id,
        // plans and roles
        LOG.debug("Transforming user payload into User domain object");
        User user = UserUtils.fromWebUserToDomainUser(payload);

        // Stores the profile image on Amazon S3 and strore the URL in the user's record.
        if(file!=null && !file.isEmpty()){
            String profileImageUrl = s3Service.storeProfileImage(file,payload.getUsername());
            if(profileImageUrl!=null){
                user.setProfileImageUrl(profileImageUrl);
            }else{
                LOG.warn("There was a problem to uploading the profile image to S3. The user's profile will" +
                        "be created without image.");
            }
        }

        // Sets the Plan and the Roles (depending on the chosen plan)
        LOG.debug("Retrieving plan from the database");
        Plan selectedPlan = planService.findPlanById(planId);
        if (null == selectedPlan) {
            LOG.error("The plan id {} could not be found. Throwing exception.", planId);
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
            return SUBSCRIPTION_VIEW_NAME;
        }
        user.setPlan(selectedPlan);

        User registeredUser = null;

        // By default users get the BASIC ROLE
        Set<UserRole> roles = new HashSet<>();
        if (planId == PlanEnum.BASIC.getId()) {
            roles.add(new UserRole(user, new Role(RolesEnums.BASIC)));
            registeredUser = userService.createUser(user, PlanEnum.BASIC, roles);
        } else {
            roles.add(new UserRole(user, new Role(RolesEnums.PRO)));
            if (StringUtils.isEmpty(payload.getCardCode()) ||
                    StringUtils.isEmpty(payload.getCardNumber()) ||
                    StringUtils.isEmpty(payload.getCardMonth()) ||
                    StringUtils.isEmpty(payload.getCardYear())) {
                LOG.error("One or more credit card fields is null or empty. Returning error to the user");
                model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                model.addAttribute(ERROR_MESSAGE_KEY, "One of more credit card details is null or empty.");
                return SUBSCRIPTION_VIEW_NAME;

            }

            //if user has selected the pro account, creates the Stripe customer to store the script number id in the db
            Map<String, Object> stripeTokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description", "Devops Buddy customer. Username: " + payload.getUsername());
            customerParams.put("email", payload.getEmail());
            customerParams.put("plan", selectedPlan.getId());
            LOG.info("Subscribing the customer to plan {}",selectedPlan.getId());
            String stripeCustomerId = stripeService.createCustomer(stripeTokenParams, customerParams);
            LOG.info("Username: {} has been subscribed to Stripe", payload.getUsername());

            user.setStripeCustomerId(stripeCustomerId);

            registeredUser = userService.createUser(user, PlanEnum.PRO, roles);

            // Extra precaution in case the POST method is invoked programmatically

        }
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    registeredUser, null, registeredUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            LOG.info("User created successfully");

            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");
            return SUBSCRIPTION_VIEW_NAME;
    }

    @ExceptionHandler({StripeException.class, S3Exception.class})
    public ModelAndView signupException(HttpServletRequest request, Exception exception){
        LOG.error("Request {} raised exception {}", request.getRequestURL(), exception);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
        mav.setViewName(GENERIC_ERROR_VIEW_NAME);
        return mav;
    }

    private void checkForDuplicates(BasicAccountPayload payload, ModelMap model) {

        // Username
        if (userService.findByUsername(payload.getUsername()) != null) {
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);
        }
        if (userService.findByEmail(payload.getEmail()) != null) {
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
        }
    }

}
