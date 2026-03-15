package com.scaler.bookmyshow.services.payment.strategies;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.scaler.bookmyshow.models.Payment;
import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.models.enums.PaymentStatus;
import com.scaler.bookmyshow.services.payment.PaymentStrategy;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentStrategy implements PaymentStrategy {

    private final RazorpayClient razorpayClient;

    public RazorpayPaymentStrategy(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public Payment generatePaymentLink(Long bookingId, double amount) {
        try {
            // Amount is in currency subunits (paise for INR). So multiply by 100.
            long amountInPaise = (long) (amount * 100);
            
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaise);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("accept_partial", true);
            paymentLinkRequest.put("first_min_partial_amount", 100);
            paymentLinkRequest.put("expire_by", (System.currentTimeMillis() / 1000) + 86400); // 24 hours expiration
            paymentLinkRequest.put("reference_id", bookingId.toString() + "-" + System.currentTimeMillis()); // Ensure uniqueness in test env
            paymentLinkRequest.put("description", "Payment for BookMyShow Ticket");
            
            JSONObject customer = new JSONObject();
            customer.put("name", "User " + bookingId);
            customer.put("contact", "+919876543210");
            customer.put("email", "user@example.com");
            paymentLinkRequest.put("customer", customer);
            
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);
            
            paymentLinkRequest.put("reminder_enable", true);
            paymentLinkRequest.put("callback_url", "https://scaler.com/");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLinkResponse = razorpayClient.paymentLink.create(paymentLinkRequest);

            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPaymentProvider(PaymentProvider.RAZORPAY);
            payment.setPaymentStatus(PaymentStatus.PENDING);
            payment.setReferenceNumber(paymentLinkResponse.get("id").toString());
            payment.setPaymentUrl(paymentLinkResponse.get("short_url").toString());
            
            // To pass back the actual generated link temporarily using the reference number, 
            // since our DB schema didn't have a 'paymentLink' field explicitly designed.
            // Alternatively, the controller will manually fetch it based on referenceID, 
            // but for simplicity, let's keep the short_url logic attached to the controller or via logging.
            System.out.println("Razorpay Link: " + payment.getPaymentUrl());
            
            return payment;
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to generate Razorpay link: " + e.getMessage());
        }
    }

    @Override
    public Payment processPayment(Long bookingId, String referenceId) {
        // Implementation for webhook validation later
        Payment payment = new Payment();
        payment.setPaymentProvider(PaymentProvider.RAZORPAY);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setReferenceNumber(referenceId);
        return payment;
    }
}
