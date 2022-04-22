package com.brinkcommerce.api.stock;

import com.brinkcommerce.api.exception.BrinkException;

public class BrinkStockException extends BrinkException {


    public BrinkStockException(String message) {
        super(message);
    }

    public BrinkStockException(String message, Exception e) {
        super(message, e);
    }


}
