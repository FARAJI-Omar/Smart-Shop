package com.smartshop.exception;

public class CannotModifyCashPaymentException extends RuntimeException {
    public CannotModifyCashPaymentException(String message) {
        super(message);
    }
}

