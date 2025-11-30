package com.smartshop.exception;

public class OrderHasPendingPaymentsException extends RuntimeException {
    public OrderHasPendingPaymentsException(String message) {
        super(message);
    }
}

