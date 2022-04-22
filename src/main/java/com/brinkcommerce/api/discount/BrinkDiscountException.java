package com.brinkcommerce.api.discount;

import com.brinkcommerce.api.exception.BrinkException;

public class BrinkDiscountException extends BrinkException {

    public BrinkDiscountException(String message) {
        super(message);
    }

    public BrinkDiscountException(String message, Exception e) {
        super(message, e);
    }
}
