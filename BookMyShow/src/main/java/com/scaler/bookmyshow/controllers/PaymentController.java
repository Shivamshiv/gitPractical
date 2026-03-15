package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.PaymentRequestDto;
import com.scaler.bookmyshow.dtos.PaymentResponseDto;
import com.scaler.bookmyshow.models.Payment;
import com.scaler.bookmyshow.services.PaymentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponseDto initiatePayment(@RequestBody PaymentRequestDto requestDto) {
        Payment payment = paymentService.initiatePayment(
                requestDto.getBookingId(),
                requestDto.getAmount(),
                requestDto.getPaymentProvider()
        );

        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setPaymentId(payment.getReferenceNumber());
        responseDto.setPaymentLink(payment.getPaymentUrl());
        responseDto.setStatus(payment.getPaymentStatus().toString());

        return responseDto;
    }
}
