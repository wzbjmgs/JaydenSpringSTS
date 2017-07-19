package com.devopsbuddy.test.unit;

import com.devopsbuddy.test.intergation.StripeIntergationTest;
import com.devopsbuddy.utils.StripeUtils;
import com.devopsbuddy.web.domain.frontend.ProAccountPayload;
import org.junit.Assert;
import org.junit.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.is;

/**
 * Created by Jayden on 7/19/2017.
 */
public class StripeUtilsUinitTest {
    @Test
    public void createStripeTokenParamsFromUserPayload() {

        ProAccountPayload payload = new ProAccountPayload();
        String cardNumber = StripeIntergationTest.TEST_CC_NUMBER;
        payload.setCardNumber(cardNumber);
        String cardCode = StripeIntergationTest.TEST_CC_CVC_NBR;
        payload.setCardCode(cardCode);
        String cardMonth = String.valueOf(StripeIntergationTest.TEST_CC_EXP_MONTH);
        payload.setCardMonth(cardMonth);
        String cardYear = String.valueOf(LocalDate.now(Clock.systemUTC()).getYear() + 1);
        payload.setCardYear(cardYear);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
        Assert.assertThat(cardNumber, is(cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY)));
        Assert.assertThat(cardMonth, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_MONTH_KEY))));
        Assert.assertThat(cardYear, is(String.valueOf(cardParams.get(StripeUtils.STRIPE_EXPIRY_YEAR_KEY))));
        Assert.assertThat(cardCode, is(cardParams.get(StripeUtils.STRIPE_CVC_KEY)));
    }
}
