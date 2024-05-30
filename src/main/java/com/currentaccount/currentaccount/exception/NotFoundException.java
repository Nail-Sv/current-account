package com.currentaccount.currentaccount.exception;

/**
 * This exception represents absence of a resource that is needed for a client request
 */
public class NotFoundException extends BaseException{

    public NotFoundException(String message) {
        super(message);
    }

}
