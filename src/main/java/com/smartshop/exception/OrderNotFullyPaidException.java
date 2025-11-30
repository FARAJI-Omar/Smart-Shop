package com.smartshop.exception;

public class OrderNotFullyPaidException extends RuntimeException {
    public OrderNotFullyPaidException(String message) {
        super(message);
    }
}

