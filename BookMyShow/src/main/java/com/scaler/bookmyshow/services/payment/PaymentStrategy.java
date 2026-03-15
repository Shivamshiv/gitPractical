package com.scaler.bookmyshow.services.payment;

import com.scaler.bookmyshow.models.Payment;

public interface PaymentStrategy {
    Payment generatePaymentLink(Long bookingId, double amount);
    Payment processPayment(Long bookingId, String referenceId);
}
