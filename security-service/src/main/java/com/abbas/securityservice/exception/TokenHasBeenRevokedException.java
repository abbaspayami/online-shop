package com.abbas.securityservice.exception;

public class TokenHasBeenRevokedException extends SecurityServiceException {

    public TokenHasBeenRevokedException(String message) {
        super(message);
    }
}
