package com.smartshop.exception;

public class PaymentExceedsRemainingAmountException extends RuntimeException {
    public PaymentExceedsRemainingAmountException(String message) {
        super(message);
    }
}

