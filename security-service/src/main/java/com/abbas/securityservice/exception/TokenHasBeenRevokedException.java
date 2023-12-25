package com.abbas.securityservice.exception;

public class TokenHasBeenRevokedException extends RuntimeException {

    public TokenHasBeenRevokedException() {
    }

    public TokenHasBeenRevokedException(String message) {
        super(message);
    }
}
