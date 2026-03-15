package com.scaler.bookmyshow.services.payment.strategies;

import com.scaler.bookmyshow.models.Payment;
import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.models.enums.PaymentStatus;
import com.scaler.bookmyshow.services.payment.PaymentStrategy;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentStrategy implements PaymentStrategy {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public Payment generatePaymentLink(Long bookingId, double amount) {
        try {
            Stripe.apiKey = this.stripeApiKey;
            
            // Stripe expects amount in subunits (Cents for USD/INR typically depends on currency base).
            // Defaulting to INR paise
            long amountInPaise = (long) (amount * 100);

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("inr")
                                                            .setUnitAmount(amountInPaise)
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("BookMyShow Ticket")
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .setQuantity(1L)
                                            .build()
                            )
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            // In a real app these should route to your frontend
                            .setSuccessUrl("http://localhost:8080/success")
                            .setCancelUrl("http://localhost:8080/cancel")
                            .putMetadata("bookingId", String.valueOf(bookingId))
                            .build();

            Session session = Session.create(params);

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPaymentProvider(PaymentProvider.STRIPE);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            // Storing the ID to fetch session later, or the URL directly for the controller if we added a property
            payment.setReferenceNumber(session.getId());
            payment.setPaymentUrl(session.getUrl());
            
            System.out.println("Stripe Link: " + payment.getPaymentUrl());
            return payment;
        } catch (StripeException e) {
            throw new RuntimeException("Failed to generate Stripe link: " + e.getMessage());
        }
    }

    @Override
    public Payment processPayment(Long bookingId, String referenceId) {
        // Implementation for webhook validation later
        Payment payment = new Payment();
        payment.setPaymentProvider(PaymentProvider.STRIPE);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setReferenceNumber(referenceId);
        return payment;
    }
}
