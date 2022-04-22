package com.brinkcommerce.api.exception;

public class BrinkException extends RuntimeException {
    public BrinkException(String msg) {
        super(msg);
    }

    public BrinkException(String msg, Exception e) {
        super(msg, e);
    }
}

// TODO: Add http code to excepion(s)