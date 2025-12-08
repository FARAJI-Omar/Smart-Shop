package com.smartshop.exception;

public class CannotPayMoreThan2KInCashException extends RuntimeException {
    public CannotPayMoreThan2KInCashException(String message) {
        super(message);
    }
}
