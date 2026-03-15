package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.InvalidBookingException;
import com.scaler.bookmyshow.models.Booking;
import com.scaler.bookmyshow.models.Payment;
import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.repositories.BookingRepository;
import com.scaler.bookmyshow.repositories.PaymentRepository;
import com.scaler.bookmyshow.services.payment.PaymentStrategy;
import com.scaler.bookmyshow.services.payment.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public PaymentService(PaymentRepository paymentRepository, 
                          BookingRepository bookingRepository,
                          PaymentStrategyFactory paymentStrategyFactory) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public Payment initiatePayment(Long bookingId, double amount, PaymentProvider provider) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        
        if (bookingOptional.isEmpty()) {
            throw new InvalidBookingException("Booking with ID: " + bookingId + " not found.");
        }
        
        Booking booking = bookingOptional.get();
                
        PaymentStrategy strategy = paymentStrategyFactory.getPaymentStrategy(provider);
        Payment payment = strategy.generatePaymentLink(bookingId, amount);
        payment.setBooking(booking);
        
        return paymentRepository.save(payment);
    }
}
