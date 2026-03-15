package com.scaler.bookmyshow.exceptions;

public class InvalidBookingException extends RuntimeException {
    public InvalidBookingException(String message) {
        super(message);
    }
}
