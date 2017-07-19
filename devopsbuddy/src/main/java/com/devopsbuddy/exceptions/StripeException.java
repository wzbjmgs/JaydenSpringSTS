package com.devopsbuddy.exceptions;

/**
 * Created by Jayden on 7/19/2017.
 */
public class StripeException extends RuntimeException {
    public StripeException(Throwable e) {
        super(e);
    }
}
