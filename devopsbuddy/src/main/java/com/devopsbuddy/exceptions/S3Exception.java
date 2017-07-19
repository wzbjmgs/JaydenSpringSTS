package com.devopsbuddy.exceptions;

/**
 * Created by Jayden on 7/19/2017.
 */
public class S3Exception extends RuntimeException {

    public S3Exception(Throwable e) {
        super(e);
    }

    public S3Exception(String s) {
        super(s);
    }
}
