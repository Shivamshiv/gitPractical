package com.scaler.bookmyshow.services.payment;

import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.services.payment.strategies.RazorpayPaymentStrategy;
import com.scaler.bookmyshow.services.payment.strategies.StripePaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    private final RazorpayPaymentStrategy razorpayPaymentStrategy;
    private final StripePaymentStrategy stripePaymentStrategy;

    public PaymentStrategyFactory(RazorpayPaymentStrategy razorpayPaymentStrategy,
                                  StripePaymentStrategy stripePaymentStrategy) {
        this.razorpayPaymentStrategy = razorpayPaymentStrategy;
        this.stripePaymentStrategy = stripePaymentStrategy;
    }

    public PaymentStrategy getPaymentStrategy(PaymentProvider provider) {
        return switch (provider) {
            case RAZORPAY -> razorpayPaymentStrategy;
            case STRIPE -> stripePaymentStrategy;
            default -> throw new IllegalArgumentException("Unknown payment provider: " + provider);
        };
    }
}
