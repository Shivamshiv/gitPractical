package com.scaler.bookmyshow.services;

import com.scaler.bookmyshow.exceptions.InvalidBookingException;
import com.scaler.bookmyshow.models.Booking;
import com.scaler.bookmyshow.models.Payment;
import com.scaler.bookmyshow.models.enums.PaymentProvider;
import com.scaler.bookmyshow.models.enums.PaymentStatus;
import com.scaler.bookmyshow.repositories.BookingRepository;
import com.scaler.bookmyshow.repositories.PaymentRepository;
import com.scaler.bookmyshow.services.payment.PaymentStrategy;
import com.scaler.bookmyshow.services.payment.PaymentStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentStrategyFactory paymentStrategyFactory;

    @Mock
    private PaymentStrategy paymentStrategy;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initiatePayment_Success() {
        // Arrange
        Long bookingId = 1L;
        double amount = 500.0;
        PaymentProvider provider = PaymentProvider.STRIPE;

        Booking booking = new Booking();
        booking.setId(bookingId);

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(paymentStrategyFactory.getPaymentStrategy(provider)).thenReturn(paymentStrategy);
        when(paymentStrategy.generatePaymentLink(bookingId, amount)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment result = paymentService.initiatePayment(bookingId, amount, provider);

        // Assert
        assertNotNull(result);
        assertEquals(amount, result.getAmount());
        assertEquals(booking, result.getBooking());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(paymentStrategyFactory, times(1)).getPaymentStrategy(provider);
        verify(paymentStrategy, times(1)).generatePaymentLink(bookingId, amount);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void initiatePayment_BookingNotFound_ThrowsException() {
        // Arrange
        Long bookingId = 1L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidBookingException.class, () -> 
            paymentService.initiatePayment(bookingId, 500.0, PaymentProvider.STRIPE)
        );

        verify(paymentStrategyFactory, never()).getPaymentStrategy(any());
        verify(paymentRepository, never()).save(any());
    }
}
