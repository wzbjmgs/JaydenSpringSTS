package com.devopsbuddy.test.intergation;

import com.devopsbuddy.backend.service.StripeService;
import com.devopsbuddy.enums.PlanEnum;
import com.devopsbuddy.utils.StripeUtils;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Jayden on 7/19/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StripeIntergationTest {

    /**The application logger**/
    private static final Logger LOG = LoggerFactory.getLogger(StripeIntergationTest.class);

    public static final String TEST_CC_NUMBER = "4242424242424242";

    public static final int TEST_CC_EXP_MONTH = 1;

    public static final String TEST_CC_CVC_NBR = "424";

    @Autowired
    private StripeService stripeService;

    @Autowired
    private String stripeKey;

    @Before
    public void init(){
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }

    @Test
    public void createStripeCustomer() throws Exception{
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put(StripeUtils.STRIPE_CARD_NUMBER_KEY, TEST_CC_NUMBER);
        cardParams.put(StripeUtils.STRIPE_EXPIRY_MONTH_KEY, TEST_CC_EXP_MONTH);
        cardParams.put(StripeUtils.STRIPE_EXPIRY_YEAR_KEY, LocalDate.now(Clock.systemUTC()).getYear() + 1);
        cardParams.put(StripeUtils.STRIPE_CVC_KEY, TEST_CC_CVC_NBR);
        tokenParams.put("card", cardParams);

        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("description", "Customer for test@example.com");
        customerParams.put("plan", PlanEnum.PRO.getId());

        String stripeCustomerId = stripeService.createCustomer(tokenParams,customerParams);
        Assert.assertThat(stripeCustomerId,notNullValue());

        Customer cu = Customer.retrieve(stripeCustomerId);
        Assert.assertNotNull(cu);
        cu.delete();

    }

}
