package com.smartshop.exception;

public class PromoCodeAlreadyUsedException extends RuntimeException {
    public PromoCodeAlreadyUsedException(String message) {
        super(message);
    }
}

