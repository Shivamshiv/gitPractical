package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private final PaymentService paymentService;

    public WebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/stripe")
    public String receiveStripeEvents(@RequestBody String payload) {
        System.out.println("Received Stripe Webhook: " + payload);
        // Normally, you would use Stripe.Event API to construct and verify signatures here.
        // For learning purposes, we print the payload.
        // paymentService.processPaymentCallback(payload);
        return "OK";
    }

    @PostMapping("/razorpay")
    public String receiveRazorpayEvents(@RequestBody String payload) {
        System.out.println("Received Razorpay Webhook: " + payload);
        // Normally, you would verify the x-razorpay-signature header here.
        // For learning purposes, we print the payload.
        // paymentService.processPaymentCallback(payload);
        return "OK";
    }
}
