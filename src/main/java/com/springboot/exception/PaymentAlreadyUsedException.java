package com.springboot.exception;

public class PaymentAlreadyUsedException extends RuntimeException {

    public PaymentAlreadyUsedException(String message) {
        super(message);
    }
}